package org.wipo.wipolex.rag.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wipo.wipolex.rag.services.KnowledgeBaseService;
import org.wipo.wipolex.rag.services.RagEvaluationService;

import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

@RestController
public class EvaluationController {

private static final Logger log = LoggerFactory.getLogger(AiAssistantController.class);
	
	// Change property name to match your configuration file
    @Value("${aws.accessKeyId}")  // This should match exactly with your properties file
    private String accessKeyId;
    
    // Change property name to match your configuration file
    @Value("${aws.secretKey}")    // This should match exactly with your properties file
    private String secretKey;
    
    @Value("${aws.session-token}")   
    private String sessionToken;
    
    private final RagEvaluationService evaluationService;
    
	private final KnowledgeBaseService knowledgeBaseService; // Required

	private final BedrockRuntimeClient bedrockClient;
	  
	 @Autowired
	    public EvaluationController(KnowledgeBaseService knowledgeBaseService, RagEvaluationService evaluationService, BedrockRuntimeClient bedrockClient) {
	        this.knowledgeBaseService = knowledgeBaseService;
	        this.evaluationService = evaluationService;
	        this.bedrockClient = bedrockClient;
	    }
	
		@GetMapping("/api/evaluation")
		ResponseEntity<String> inquire(@RequestBody List<String> questions) {
			String evaluationJson = evaluationService.generateEvaluationFile(questions);
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setContentDispositionFormData("attachment", "rag-evaluation.json");
	        
	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(evaluationJson);
		}
	
}
