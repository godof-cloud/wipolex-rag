package org.wipo.wipolex.rag.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.text.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wipo.wipolex.rag.model.Citation;
import org.wipo.wipolex.rag.model.ContentItem;
import org.wipo.wipolex.rag.model.ConversationTurn;
import org.wipo.wipolex.rag.model.Evaluation;
import org.wipo.wipolex.rag.model.GeneratedResponsePart;
import org.wipo.wipolex.rag.model.Output;
import org.wipo.wipolex.rag.model.Prompt;
import org.wipo.wipolex.rag.model.ReferenceResponse;
import org.wipo.wipolex.rag.model.RetrievalResult;
import org.wipo.wipolex.rag.model.RetrievedPassages;
import org.wipo.wipolex.rag.model.RetrievedReference;
import org.wipo.wipolex.rag.model.Span;
import org.wipo.wipolex.rag.model.TextResponsePart;

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
            List<ConversationTurn> turns = questions.stream()
                .map(this::processQuestion)
                .collect(Collectors.toList());
                
            Evaluation evaluationFile = new Evaluation(turns);
            return objectMapper.writeValueAsString(evaluationFile);
        } catch (Exception e) {
            throw new RuntimeException("Error generating evaluation file", e);
        }
    }
	
	private ConversationTurn processQuestion(String question) {
        // 1. Create the prompt with the question
        ContentItem questionContent = new ContentItem(question);
        Prompt prompt = new Prompt(List.of(questionContent));
        
        // 2. Retrieve relevant passages using your existing RAG system
        List<Document> retrievedDocs = retriever.retrieve(question);
        
        // 3. Get response from LLM
        String llmResponse = chatClient.call(new UserMessage(question)).getResult().getOutput().getContent();
        
        // 4. Create retrieved passages
        List<RetrievalResult> retrievalResults = retrievedDocs.stream()
                .map(doc -> new RetrievalResult(new ContentItem(doc.getContent().toString())))
                .collect(Collectors.toList());
        RetrievedPassages retrievedPassages = new RetrievedPassages(retrievalResults);
        
        // 5. Generate citations
        List<Citation> citations = generateCitations(llmResponse, retrievedDocs);
        
        // 6. Create output
        Output output = new Output(
                llmResponse,
                "third-party-model",
                "third-party-RAG",
                retrievedPassages,
                citations
        );
        
        // 7. Create reference response
        ContentItem referenceContent = new ContentItem(llmResponse);
        ReferenceResponse reference = new ReferenceResponse(List.of(referenceContent));
        
        // 8. Create and return the conversation turn
        return new ConversationTurn(prompt, List.of(reference), output);
    }
    
    private List<Citation> generateCitations(String response, List<Document> retrievedDocs) {
        List<Citation> citations = new ArrayList<>();
        
        // Simple approach: split response into chunks and associate with retrieved docs
        String[] words = response.split("\\s+");
        int chunkSize = words.length / Math.max(1, retrievedDocs.size());
        
        for (int i = 0; i < words.length; i += chunkSize) {
            int end = Math.min(i + chunkSize, words.length);
            String chunk = String.join(" ", Arrays.copyOfRange(words, i, end));
            
            // Create span
            Span span = new Span(i, end);
            
            // Create text response part
            TextResponsePart textPart = new TextResponsePart(span, chunk);
            
            // Create generated response part
            GeneratedResponsePart generatedPart = new GeneratedResponsePart(textPart);
            
            // Add retrieved references
            List<RetrievedReference> references = new ArrayList<>();
            int docIndex = Math.min(i / chunkSize, retrievedDocs.size() - 1);
            if (docIndex >= 0 && docIndex < retrievedDocs.size()) {
                ContentItem content = new ContentItem(retrievedDocs.get(docIndex).getContent().toString());
                RetrievedReference reference = new RetrievedReference(content);
                references.add(reference);
            }
            
            // Create citation
            Citation citation = new Citation(generatedPart, references);
            citations.add(citation);
        }
        
        return citations;
    }

}
