package org.wipo.wipolex.rag.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wipo.wipolex.rag.model.LLMRequest;
import org.wipo.wipolex.rag.services.AiModelService;

@RestController
public class AiAssistantController {
	private static final Logger log = LoggerFactory.getLogger(AiAssistantController.class);

	private final AiModelService aiModelService;

	public AiAssistantController(AiModelService aiModelService) {
		this.aiModelService = aiModelService;
	}

	@PostMapping("/api/{user}/inquire")
	String inquire(@PathVariable("user") String user, @RequestBody LLMRequest request) {
		log.info("inside inquire**");
		// Retrieve information from knowledge base
		return aiModelService.queryLLMwithKnowledgeBase(request.question());
	}
}
