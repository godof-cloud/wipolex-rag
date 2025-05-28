package org.wipo.wipolex.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/*@SpringBootApplication(exclude = {
		BedrockTitanEmbeddingAutoConfiguration.class
	})*/
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
        .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        return objectMapper;
    }
	
	
   /* @Bean
    public BedrockClient bedrockClient() {
        return BedrockClient.builder()
                .region(Region.US_WEST_2)  // Adjust region if needed
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
    
    @Bean
    public BedrockRuntimeClient bedrockRuntimeClient() {
        return BedrockRuntimeClient.builder()
                .region(Region.US_WEST_2)  // Adjust region if needed
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
    
    @Bean
    public BedrockAgentRuntimeClient bedrockAgentRuntimeClient() {
        return BedrockAgentRuntimeClient.builder()
                .region(Region.US_WEST_2)  // Adjust region if needed
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }*/
}