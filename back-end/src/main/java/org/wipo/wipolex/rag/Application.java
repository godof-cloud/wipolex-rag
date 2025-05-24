package org.wipo.wipolex.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	ChatClient chatClient(ChatClient.Builder builder, VectorStore vectorStore) {
		var systemPrompt = """
				Your are an AI powered lawyer that helps users to find useful information about ...
				""";
		
		return builder
				.defaultSystem(systemPrompt)
				.build();
	}
	
}
