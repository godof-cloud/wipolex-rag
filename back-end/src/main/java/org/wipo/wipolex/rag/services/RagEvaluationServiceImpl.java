package org.wipo.wipolex.rag.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.text.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private AiModelService aiModelService;
	
	@Override
	public String generateEvaluationFile(List<String> questions) {
        try {
        	
        	List<ConversationTurn> turns = new ArrayList<>();
        	
        	for (String question : questions) {
				LLMResponse llmResponse = processQuestion(question);
				
				if (Objects.nonNull(llmResponse) && Objects.nonNull(llmResponse.details())) {
					turns.addAll(llmResponse.details().conversationTurns());
				}
			}
        	
                
            Evaluation evaluationFile = new Evaluation(turns);
            return objectMapper.writeValueAsString(evaluationFile);
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
			return null;
		} catch (JsonProcessingException e) {
			return null;
		}
                
        // 8. Create and return the conversation turn
        return llmResponseParsed;
    }
    
}
