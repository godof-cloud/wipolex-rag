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
		AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create("ASIATOHBIS5CXXM3JJWZ",
				"0QFp5fnxQCJXy9qFu/P4MIdoOZJXQL11rR2fgrgJ",
				"IQoJb3JpZ2luX2VjEKf//////////wEaCXVzLWVhc3QtMSJHMEUCIDpSWSWx2fS939sOQ1yz5f7mr7HH4HeUMCEvJv7BrFFeAiEAi7g3GuXl0zEiZyNNTDT/FSrRNRkpmCtbjRiR1JkkqxAqmQIIcBABGgwyMzY2OTU2MjM0OTMiDGdjEUtiW1scn7kayyr2AS4m2vSIlRYGd0PJa9XeItYqbkg8YYudYp3CJyDPw4OxcwA3iJwFKMbnjGZxK2rp+k+1+Jnj5lzWzcE7o+YVfCngMLFD8xqFsCsSZr+RLm+QdzJfnnu3gjxGr0/0tMSVI8UQREPRLcDEV4W758cTtmTfDqqsjgQDNhj9C/MZq+wz3amkjZT9Jp36Qz6ICU/NzA0+HsUpYYxkct/VwRxDIUoeRJDLPA3wkSSPKmvWkLbcXX8S8+wIVIqRHaceXLl8Yi14/4N82y1SLych1pO4oi5hZCol08oOr39j46wFLMo+0X8I1lYq81qwIi9+gT9UtHt+PbIimjDP39rBBjqdAdXLWOdZo3Epls3a01V2rC+1Um2JQNluWW1V/9wF+KVG3mvzHR4eTWLmLovucTaWSc3J98tSdsuyAaELVN9z/ID8VZwCAtCtoUc7Nk9EoC2kg/kNhA8FfCKlMdNGgRm0a7BcSStqJodXpbNbVuU5RK15MFy6ttWCU9AIusZ7nEB8j9/uvSBtENOuC+NOGQhEKvT0WxrgsA/6jzV8y0Y=");
		StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(sessionCredentials);

		this.bedrockAgentRuntimeClient = BedrockAgentRuntimeClient.builder().region(Region.of("us-west-2"))
				.credentialsProvider(credentialsProvider).build();
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