package org.wipo.wipolex.rag.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.text.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wipo.wipolex.rag.controller.AiAssistantController;
import org.wipo.wipolex.rag.model.Citation;
import org.wipo.wipolex.rag.model.ContentItem;
import org.wipo.wipolex.rag.model.ConversationTurn;
import org.wipo.wipolex.rag.model.Evaluation;
import org.wipo.wipolex.rag.model.GeneratedResponsePart;
import org.wipo.wipolex.rag.model.LLMResponse;
import org.wipo.wipolex.rag.model.RetrievedReference;
import org.wipo.wipolex.rag.model.Span;
import org.wipo.wipolex.rag.model.TextResponsePart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RagEvaluationServiceImpl implements RagEvaluationService {

	private static final Logger log = LoggerFactory.getLogger(RagEvaluationServiceImpl.class);
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private AiModelService aiModelService;
	
	@Override
	public String generateEvaluationFile(String question) {
        try {
			LLMResponse llmResponse = processQuestion(question);
			if (Objects.nonNull(llmResponse) && Objects.nonNull(llmResponse.details())) {
				return objectMapper.writeValueAsString(llmResponse.details());
			}
			
			return "{}";
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error processing LLM response", e);
		} catch (NullPointerException e) {
			throw new RuntimeException("LLM response or details are null", e);
        } catch (Exception e) {
            throw new RuntimeException("Error generating evaluation file", e);
        }
    }
	
	private LLMResponse processQuestion(String question) {
        LLMResponse llmResponseParsed;
		try {
			String llmResponse = aiModelService.queryLLMwithKnowledgeBase(question);
			
			llmResponseParsed = objectMapper.readValue(llmResponse, LLMResponse.class);
		} catch (JsonMappingException e) {
			log.error(e.getMessage(), e);
			return null;
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			return null;
		}
                
        // 8. Create and return the conversation turn
        return llmResponseParsed;
    }
    
}
