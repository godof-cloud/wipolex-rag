package org.wipo.wipolex.rag.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.KnowledgeBaseQuery;
import software.amazon.awssdk.services.bedrockagentruntime.model.KnowledgeBaseRetrievalConfiguration;
import software.amazon.awssdk.services.bedrockagentruntime.model.KnowledgeBaseRetrievalResult;
import software.amazon.awssdk.services.bedrockagentruntime.model.KnowledgeBaseVectorSearchConfiguration;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveResponse;

@Service
public class BedrockKnowledgeBaseService {

	private final BedrockAgentRuntimeClient bedrockAgentRuntimeClient;
	
//	@Value("${aws.knowledge-base-id}")
//    private String knowledgeBaseId;

	ObjectMapper requestMapper = new ObjectMapper();
	
    // Initialize the knowledgebase configuration
    KnowledgeBaseVectorSearchConfiguration knowledgeBaseVectorSearchConfiguration = KnowledgeBaseVectorSearchConfiguration.builder()
            .numberOfResults(10)
            .build();
    KnowledgeBaseRetrievalConfiguration knowledgeBaseRetrievalConfiguration = KnowledgeBaseRetrievalConfiguration.builder()
            .vectorSearchConfiguration(knowledgeBaseVectorSearchConfiguration)
            .build();
    
	 public BedrockKnowledgeBaseService(BedrockAgentRuntimeClient bedrockAgentClient) {
	        this.bedrockAgentRuntimeClient = bedrockAgentClient;
	    }
	 
	 public String getContext(String query) {
	        try {
	        	
	            // Form the request for bedrock knowledgebase
	            KnowledgeBaseQuery knowledgeBaseQuery = KnowledgeBaseQuery.builder()
	                    .text(query)
	                    .build();
	            
	            RetrieveRequest retrieveRequest = RetrieveRequest.builder()
	                    .knowledgeBaseId("UVS5FXQQJR")
	                    .retrievalQuery(knowledgeBaseQuery)
	                    .retrievalConfiguration(knowledgeBaseRetrievalConfiguration)
	                    .build();

	            // Invoke the bedrock knowledgebase
	            RetrieveResponse retrieveResponse = bedrockAgentRuntimeClient.retrieve(retrieveRequest);
	        	
	            // Extract the bedrock results and return the results
	                if(retrieveResponse.hasRetrievalResults()) {
	                    ArrayNode responseNode = requestMapper.createArrayNode();
	                    for(KnowledgeBaseRetrievalResult result: retrieveResponse.retrievalResults()) {
	                        responseNode.add(result.content().text());
	                    }
	                    
	                    return responseNode.toString();
	                }
	            
	            // Step 1: Retrieve relevant content from Knowledge Base
//	            RetrieveRequest retrieveRequest = RetrieveRequest.builder()
//	                .knowledgeBaseId(knowledgeBaseId)
//	                .retrievalQuery(RetrievalQuery.builder()
//	                    .text(query)
//	                    .build())
//	                .build();
	                
//	            RetrieveResponse retrieveResponse = bedrockAgentRuntimeClient.retrieve(retrieveRequest);
//	            
//	            // Step 2: Format retrieved content as context
//	            List<RetrievedReference> references = retrieveResponse.retrievalResults();
//	            StringBuilder context = new StringBuilder();
//	            
//	            for (RetrievedReference ref : references) {
//	                context.append("Source: ").append(ref.location().text()).append("\n");
//	                context.append("Content: ").append(ref.content().text()).append("\n\n");
//	            }
//	            
//	            // Step 3: Build enhanced prompt with retrieved context
//	            String enhancedPrompt = "Based on the following information:\n\n" + 
//	                context.toString() + 
//	                "\n\nAnswer this question: " + query;
	                
	            // Step 4: Send to Bedrock model
//	            Prompt prompt = new Prompt(enhancedPrompt);
//	            Generation generation = chatClient.call(prompt).getResult();
//	            
//	            return generation.getOutput().getContent();
	        } catch (Exception e) {
	            throw new RuntimeException("Error querying knowledge base: " + e.getMessage(), e);
	        }
	        
	        return null;
	    }

 }
