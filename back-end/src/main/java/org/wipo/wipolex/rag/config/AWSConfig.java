package org.wipo.wipolex.rag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

@Configuration
public class AWSConfig {

	// Change property name to match your configuration file
    @Value("${aws.accessKeyId}")  // This should match exactly with your properties file
    private String accessKeyId;
    
    // Change property name to match your configuration file
    @Value("${aws.secretKey}")    // This should match exactly with your properties file
    private String secretKey;
    
    @Value("${aws.session-token}")   
    private String sessionToken;
    
	@Bean
	public BedrockRuntimeClient bedrockClient() {
		 AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(accessKeyId, secretKey, sessionToken);
			StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(sessionCredentials);

			return BedrockRuntimeClient.builder().region(Region.of("us-west-2"))
					.credentialsProvider(credentialsProvider).build();
	    }
}
