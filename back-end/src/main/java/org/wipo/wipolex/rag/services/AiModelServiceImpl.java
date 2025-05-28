package org.wipo.wipolex.rag.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockagentruntime.model.KnowledgeBaseRetrievalResult;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveResponse;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

@Service
public class AiModelServiceImpl implements AiModelService {
	
	private static final Logger log = LoggerFactory.getLogger(AiModelServiceImpl.class);

	@Autowired
	private KnowledgeBaseService knowledgeBaseService;
	
	@Autowired
	private BedrockRuntimeClient bedrockClient;
	
	@Override
	public String queryLLMwithKnowledgeBase(String question) {
		RetrieveResponse retrieveResponse = knowledgeBaseService.retrieveFromKnowledgeBase(question);
		log.info("retrieveResponse" + retrieveResponse);
		// Format the retrieved information as context
		String knowledgeContext = formatRetrievedInformation(retrieveResponse);
		// Send the query along with the knowledge context to the agent
		return invokeAgentWithContext(question, knowledgeContext);
	}
	
	private String formatRetrievedInformation(RetrieveResponse retrieveResponse) {
        StringBuilder contextBuilder = new StringBuilder();
        contextBuilder.append("Here is relevant information from the knowledge base:\n\n");
        
        if (retrieveResponse.retrievalResults().isEmpty()) {
            contextBuilder.append("No relevant information found in the knowledge base.");
        } else {
            int i = 1;
            for (KnowledgeBaseRetrievalResult result : retrieveResponse.retrievalResults()) {
                contextBuilder.append("Source ").append(i).append(":\n");
                contextBuilder.append(result.content().text());
                contextBuilder.append("\n\n");
                i++;
            }
        }
        
        return contextBuilder.toString();
    }
	private String invokeAgentWithContext(String userQuery, String knowledgeContext) {
	    try {
	        // Create the messages format payload for Claude 3
	        Map<String, Object> requestPayload = new HashMap<>();
	        
	        // User message with knowledge context and query
	        Map<String, Object> userMessage = new HashMap<>();
	        userMessage.put("role", "user");
	        userMessage.put("content", "I'll provide you with knowledge context and a user query. Please respond to the user query using only the information from the knowledge context.\n\n" +
	                                  "Knowledge Context:\n" + knowledgeContext + 
	                                  "\n\nUser Query: " + userQuery);
	        
	        // Add messages to the request
	        List<Map<String, Object>> messages = new ArrayList<>();
	        messages.add(userMessage);
	        
	        requestPayload.put("messages", messages);
	        requestPayload.put("anthropic_version", "bedrock-2023-05-31");
	        requestPayload.put("max_tokens", 4000);
	        requestPayload.put("temperature", 0.7);
	        
	        // Convert payload to JSON string
	        String jsonRequest = new ObjectMapper().writeValueAsString(requestPayload);
	        
	        // Log the request for debugging
	        log.debug("Claude request payload: {}", jsonRequest);
	        
	        // Create request for invoking the model
	        InvokeModelRequest request = InvokeModelRequest.builder()
	            .modelId("anthropic.claude-3-5-haiku-20241022-v1:0")
	            .body(SdkBytes.fromUtf8String(jsonRequest))
	            .contentType("application/json")
	            .build();
	        
	        // Invoke the model
	        InvokeModelResponse response = bedrockClient.invokeModel(request);
	        
	        // Parse and extract the assistant's response
	        String responseBody = response.body().asUtf8String();
	        log.debug("Claude response: {}", responseBody);
	        
	        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
	        String assistantResponse = null;
	        
	        // Handle the specific response structure for Claude models
	        if (jsonNode.has("content")) {
	            if (jsonNode.get("content").isArray() && jsonNode.get("content").size() > 0) {
	                // Format for Claude 3 models
	                assistantResponse = jsonNode.path("content").path(0).path("text").asText();
	            } else {
	                // Alternative path
	                assistantResponse = jsonNode.path("content").asText();
	            }
	        } else if (jsonNode.has("completion")) {
	            // Format for older Claude models
	            assistantResponse = jsonNode.path("completion").asText();
	        } else {
	            assistantResponse = "Unable to parse the model response: " + responseBody;
	        }
	        
	        return assistantResponse;
	        
	    } catch (Exception e) {
	        log.error("Error getting response from knowledge base and agent: ", e);
	        e.printStackTrace();
	        return "I encountered an error while retrieving information. " + e.getMessage();
	    }
	}

}
