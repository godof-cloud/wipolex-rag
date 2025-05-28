/*package org.wipo.wipolex.rag.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

@Configuration
public class BedrockAgentConfig {

    private static final String SYSTEM_PROMPT = """
            Your are an AI powered lawyer that helps users to find useful information about ...
            You should only provide factual information based on the knowledge context provided.
            If you don't know the answer, say so rather than making up information.
            Always cite specific sections or references from the knowledge context when possible.
            """;

    @Bean
    public BedrockRuntimeClient bedrockRuntimeClient() {
        return BedrockRuntimeClient.builder()
                .region(Region.US_WEST_2) 
                .build();
    }
    
    @Bean
    public BedrockAgentService bedrockAgentService(BedrockRuntimeClient bedrockRuntimeClient) {
        return new BedrockAgentService(bedrockRuntimeClient, SYSTEM_PROMPT);
    }
}*/
