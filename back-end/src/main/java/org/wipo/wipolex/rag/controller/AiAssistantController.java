package org.wipo.wipolex.rag.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wipo.wipolex.rag.services.BedrockKnowledgeBaseService;
import org.wipo.wipolex.rag.tools.AgentTool;

@RestController
public class AiAssistantController {

	private final ChatClient chatClient;
//	private BedrockKnowledgeBaseService bedrockKnowledgeBaseService;
	
	private final Map<String, PromptChatMemoryAdvisor> advisorMap = new ConcurrentHashMap<>();
//	private final QuestionAnswerAdvisor questionAnswerAdvisor;
	private final AgentTool agentTool;
	
	AiAssistantController(ChatClient chatClient, VectorStore vectorStore, AgentTool agentTool){
		this.chatClient = chatClient;
//		this.questionAnswerAdvisor = new QuestionAnswerAdvisor(vectorStore);
		this.agentTool = agentTool;
//		this.bedrockKnowledgeBaseService = bedrockKnowledgeBaseService;
	}
	
	@GetMapping("/{user}/inquire")
	String inquire(@PathVariable("user") String user, @RequestParam String question) {
		System.out.println("test________");
		
		
//		Advisor advisor = this.advisorMap.computeIfAbsent(user, memoryAdvisor -> PromptChatMemoryAdvisor.builder(new InMemoryChatMemory()).build());

//		System.out.println(bedrockKnowledgeBaseService.getContext(question));
		return chatClient.prompt()
						 .user(question)
//						 .advisors(questionAnswerAdvisor)
						 .tools(agentTool)
						 .call()
						 .content();
	}
}
