package org.wipo.wipolex.rag.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.wipo.wipolex.rag.tools.AgentTool;

@Controller 
public class AiAssistantController {

	private final ChatClient chatClient;
	
	private final Map<String, PromptChatMemoryAdvisor> advisorMap = new ConcurrentHashMap<>();
	private final QuestionAnswerAdvisor questionAnswerAdvisor;
	private final AgentTool agentTool;
	
	AiAssistantController(ChatClient chatClient, VectorStore vectorStore, AgentTool agentTool){
		this.chatClient = chatClient;
		this.questionAnswerAdvisor = new QuestionAnswerAdvisor(vectorStore);
		this.agentTool = agentTool;
	}
	
	@GetMapping("/{user}/inquire")
	String inquire(@PathVariable("user") String user, @RequestParam String question) {
		var advisor = this.advisorMap.computeIfAbsent(user, memoryAdvisor -> PromptChatMemoryAdvisor.builder(new InMemoryChatMemory()).build());
		
		return chatClient.prompt()
						 .user(question)
						 .advisors(advisor, this.questionAnswerAdvisor)
						 .tools(agentTool)
						 .call()
						 .content();
	}
}
