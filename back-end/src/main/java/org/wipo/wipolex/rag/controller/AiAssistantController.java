package org.wipo.wipolex.rag.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wipo.wipolex.rag.model.LLMRequest;
import org.wipo.wipolex.rag.model.LLMResponse;
import org.wipo.wipolex.rag.services.AiModelService;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class AiAssistantController {
	private static final Logger log = LoggerFactory.getLogger(AiAssistantController.class);

	private final AiModelService aiModelService;
	private final ObjectMapper objectMapper;

	public AiAssistantController(AiModelService aiModelService, ObjectMapper objectMapper) {
		this.aiModelService = aiModelService;
		this.objectMapper = objectMapper;
	}

	@PostMapping("/api/{user}/inquire")
	String inquire(@PathVariable("user") String user, @RequestBody LLMRequest request) {
		try {
			log.info("inside inquire**");
			// Retrieve information from knowledge base
			String answer = aiModelService.queryLLMwithKnowledgeBase(request.question());
			LLMResponse llmResponse = objectMapper.readValue(answer, LLMResponse.class);
			
			StringBuilder responseBuilder = new StringBuilder();
			
			responseBuilder.append(llmResponse.answer()).append("<br/><br/>");
			
			if (!CollectionUtils.isEmpty(llmResponse.references())) {
				responseBuilder.append("<b>References</b>").append("<br/>");
				llmResponse.references().forEach(reference -> responseBuilder.append(reference.title()).append(" <b>[").append(reference.refNum()).append("]</b><br/>"));
			}
			
			return responseBuilder.toString();
		} catch (IOException e) {
			throw new RuntimeException("Error processing LLM response", e);
		}
		
	}
}
