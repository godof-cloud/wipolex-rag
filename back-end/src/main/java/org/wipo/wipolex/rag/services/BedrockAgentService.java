/*package org.wipo.wipolex.rag.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BedrockAgentService {
	private static final Logger log = LoggerFactory.getLogger(BedrockAgentService.class);
    private final BedrockRuntimeClient bedrockClient;
    private final String systemPrompt;
    private final String modelId = "anthropic.claude-3-5-haiku-20241022-v1:0"; 
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BedrockAgentService(BedrockRuntimeClient bedrockClient, String systemPrompt) {
        this.bedrockClient = bedrockClient;
        this.systemPrompt = systemPrompt;
    }

    public String invokeAgentWithContext(String userQuery, String knowledgeContext) {
        try {
            // Create the messages format payload for Claude 3
            Map<String, Object> requestPayload = new HashMap<>();
            
            List<Map<String, String>> messages = new ArrayList<>();
            
            // Add user message with knowledge context
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", "Knowledge Context:\n" + knowledgeContext + 
                                     "\n\nUser Query: " + userQuery);
            messages.add(userMessage);
            
            // Apply configurations
            requestPayload.put("messages", messages);
            requestPayload.put("system", systemPrompt); // Claude 3 models support system parameter
            requestPayload.put("max_tokens", 4000);
            requestPayload.put("temperature", 0.2); // Lower for more factual responses
            requestPayload.put("top_p", 0.9);
            
            // Convert payload to JSON string
            String jsonRequest = objectMapper.writeValueAsString(requestPayload);
            
            // Create request for invoking the model
            InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId(modelId)
                .body(SdkBytes.fromUtf8String(jsonRequest))
                .contentType("application/json")
                .build();
            
            // Invoke the model
            InvokeModelResponse response = bedrockClient.invokeModel(request);
            
            // Parse and extract the assistant's response
            String responseBody = response.body().asUtf8String();
            
            // Parse the JSON response
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
            
            if (responseMap.containsKey("content")) {
                List<Map<String, Object>> contentList = (List<Map<String, Object>>) responseMap.get("content");
                if (!contentList.isEmpty() && contentList.get(0).containsKey("text")) {
                    return (String) contentList.get(0).get("text");
                }
            }
            
            return "Unable to parse the response: " + responseBody;
            
        } catch (Exception e) {
            log.error("Error getting response from knowledge base and agent: ", e);
            return "I encountered an error while retrieving information: " + e.getMessage();
        }
    }
    
    // You can add additional methods here for different types of interactions
    
    // For example, a method to handle conversations with history
    public String continueConversation(List<Map<String, String>> conversationHistory, 
                                      String userQuery, 
                                      String knowledgeContext) {
        try {
            // Create a deep copy of the conversation history
            List<Map<String, String>> messages = new ArrayList<>(conversationHistory);
            
            // Add the latest user message with knowledge context
            Map<String, String> latestMessage = new HashMap<>();
            latestMessage.put("role", "user");
            latestMessage.put("content", "Knowledge Context:\n" + knowledgeContext + 
                                       "\n\nUser Query: " + userQuery);
            messages.add(latestMessage);
            
            // Rest of implementation similar to invokeAgentWithContext
            // ...
            
            return "Response to the conversation";
        } catch (Exception e) {
            log.error("Error continuing conversation: ", e);
            return "I encountered an error in our conversation: " + e.getMessage();
        }
    }
}*/