/*package org.wipo.wipolex.rag.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockagent.BedrockAgentClient;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;

@Configuration
public class BedrockKnowledgeBaseConfig {
    @Bean
    public BedrockAgentRuntimeClient bedrockAgentClient(
            @Value("${spring.ai.bedrock.aws.region}") String region,
            @Value("${spring.ai.bedrock.aws.access-key}") String accessKey,
            @Value("${spring.ai.bedrock.aws.secret-key}") String secretKey) {
            
        return BedrockAgentRuntimeClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .build();
    }
}*/
