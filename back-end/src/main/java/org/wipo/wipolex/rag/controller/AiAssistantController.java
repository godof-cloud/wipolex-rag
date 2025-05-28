package org.wipo.wipolex.rag.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wipo.wipolex.rag.services.KnowledgeBaseService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockagentruntime.model.KnowledgeBaseRetrievalResult;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveResponse;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;
@RestController
public class AiAssistantController {
	private static final Logger log = LoggerFactory.getLogger(AiAssistantController.class);
	
	// Change property name to match your configuration file
    @Value("${aws.accessKeyId}")  // This should match exactly with your properties file
    private String accessKeyId;
    
    // Change property name to match your configuration file
    @Value("${aws.secretKey}")    // This should match exactly with your properties file
    private String secretKey;
    
    @Value("${aws.session-token}")   
    private String sessionToken;
    
	private final KnowledgeBaseService knowledgeBaseService; // Required

	private BedrockRuntimeClient bedrockClient;
	  
	 @Autowired
	    public AiAssistantController(KnowledgeBaseService knowledgeBaseService, BedrockRuntimeClient bedrockClient) {
	        this.knowledgeBaseService = knowledgeBaseService;
	        this.bedrockClient = bedrockClient;
	    }
	
		@GetMapping("/api/{user}/inquire")
		String inquire(@PathVariable("user") String user, @RequestParam String question) {
			log.info("inside inquire**");
			// Retrieve information from knowledge base
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
	        String systemPrompt = "You are an AI assistant that processes knowledge context and responds to queries. " +
	                "EXTREMELY IMPORTANT: Your response MUST ALWAYS be formatted as a valid JSON object with EXACTLY two fields:\n" +
	                "1. 'answer': A thorough answer to the user's query based ONLY on the provided knowledge context\n" +
	                "2. 'references': An array of source identifiers (e.g. 'Source 1', 'Source 2') that you referenced in your answer\n\n" +
	                "Example of the ONLY valid response format:\n" +
	                "{\n" +
	                "  \"answer\": \"Your detailed answer goes here...\",\n" +
	                "  \"references\": [\"Source 1\", \"Source 2\"]\n" +
	                "}\n\n" +
	                "DO NOT include any other fields in your JSON. DO NOT structure your answer as JSON within JSON. " +
	                "The entire response must be valid JSON that can be directly parsed.";
	        requestPayload.put("system", systemPrompt);
	      /*  Map<String, Object> systemMessage = new HashMap<>();
	        systemMessage.put("role", "assistant");
	        systemMessage.put("content", "CRITICAL INSTRUCTION - HIGHEST PRIORITY:Always respond with valid JSON containing 'answer' and 'references' keys.");
	        */
	        // User message with knowledge context and query
	        Map<String, Object> userMessage = new HashMap<>();
	        userMessage.put("role", "user");
	        userMessage.put("content", "I'll provide you with knowledge context and a user query. Please respond to the user query using only the information from the knowledge context.\n\n" +
	                                  "Knowledge Context:\n" + knowledgeContext + 
	                                  "\n\nUser Query: " + userQuery);
	        
	        // Add messages to the request
	        List<Map<String, Object>> messages = new ArrayList<>();
	       // messages.add(systemMessage);
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
	 private String createModelPrompt(String prompt) {
	        // For Claude/Anthropic models
	        return "{\n" +
	               "  \"prompt\": \"" + escapeJsonString(prompt) + "\",\n" +
	               "  \"max_tokens_to_sample\": 2000,\n" +
	               "  \"temperature\": 0.7,\n" +
	               "  \"top_p\": 0.9\n" +
	               "}";
	    }
	 // Helper method to properly escape JSON strings
	    private String escapeJsonString(String input) {
	        return input.replace("\\", "\\\\")
	                    .replace("\"", "\\\"")
	                    .replace("\n", "\\n")
	                    .replace("\r", "\\r")
	                    .replace("\t", "\\t");
	    }
	    private String parseModelResponse(String responseJson) {
	        try {
	            // Parse using your preferred JSON library
	            // This is a simplified example using basic string extraction
	            // You should use proper JSON parsing in production
	            int startIndex = responseJson.indexOf("\"completion\":") + 14;
	            int endIndex = responseJson.indexOf("\"", startIndex);
	            
	            if (startIndex >= 14 && endIndex > startIndex) {
	                return responseJson.substring(startIndex, endIndex);
	            } else {
	                return "Error parsing model response";
	            }
	        } catch (Exception e) {
	           // log.error("Error parsing model response: ", e);
	            return "Error parsing model response: " + e.getMessage();
	        }
	    }
}
