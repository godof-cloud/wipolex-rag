package org.wipo.wipolex.rag.services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
@Configuration
public class KnowledgeBaseServiceConfig {

    @Value("${aws.region}")
    private String region;
    
    // Change property name to match your configuration file
    @Value("${aws.accessKeyId}")  // This should match exactly with your properties file
    private String accessKeyId;
    
    // Change property name to match your configuration file
    @Value("${aws.secretKey}")    // This should match exactly with your properties file
    private String secretKey;
    
    @Value("${aws.session-token}")    // This should match exactly with your properties file
    private String sessionToken;
    
   
    
	@PostConstruct
	public void init() {
	 // Debug output to verify values
        System.out.println("AWS Region: " + region);
        System.out.println("Access Key Present: " + (accessKeyId != null && !accessKeyId.isEmpty()));
        System.out.println("Secret Key Present: " + (secretKey != null && !secretKey.isEmpty()));
	}
	@Bean
	public BedrockAgentRuntimeClient bedrockAgentClient() {
		AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(accessKeyId, secretKey,sessionToken);
		StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(sessionCredentials);
		return BedrockAgentRuntimeClient.builder().region(Region.of(region)).credentialsProvider(credentialsProvider)
				.build();
	}
	
}
