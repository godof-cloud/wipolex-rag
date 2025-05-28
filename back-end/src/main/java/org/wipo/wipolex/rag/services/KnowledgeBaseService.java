package org.wipo.wipolex.rag.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.KnowledgeBaseQuery;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveResponse;

@Service
public class KnowledgeBaseService {
	private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseService.class);
	private final BedrockAgentRuntimeClient bedrockAgentRuntimeClient;

	public KnowledgeBaseService(BedrockAgentRuntimeClient bedrockAgentRuntimeClient) {
		this.bedrockAgentRuntimeClient = bedrockAgentRuntimeClient;
	}

	@Value("${aws.bedrock.knowledgebase.id:UVS5FXQQJR}")
	private String knowledgeBaseId;

	@Value("${aws.bedrock.model.id:anthropic.claude-3-5-haiku-20241022-v1:0}")
	private String modelId;

	public RetrieveResponse retrieveFromKnowledgeBase(String query) {
		try {
			// Create retrieve request with input as a separate object
			RetrieveRequest retrieveRequest = RetrieveRequest.builder().knowledgeBaseId(knowledgeBaseId)
					.retrievalQuery(KnowledgeBaseQuery.builder().text(query).build()).build();
			// Execute retrieve request
			return bedrockAgentRuntimeClient.retrieve(retrieveRequest);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

}