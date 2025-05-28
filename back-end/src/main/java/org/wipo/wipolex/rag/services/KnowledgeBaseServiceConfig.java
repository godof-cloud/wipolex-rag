package org.wipo.wipolex.rag.services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
@Configuration
public class KnowledgeBaseServiceConfig {

    @Value("${aws.region}")
    private String region;
    
    // Change property name to match your configuration file
    @Value("${aws.accessKeyId}")  // This should match exactly with your properties file
    private String accessKeyId;
    
    // Change property name to match your configuration file
    @Value("${aws.secretKey}")    // This should match exactly with your properties file
    private String secretKey;
    
   
    
	@PostConstruct
	public void init() {
	 // Debug output to verify values
        System.out.println("AWS Region: " + region);
        System.out.println("Access Key Present: " + (accessKeyId != null && !accessKeyId.isEmpty()));
        System.out.println("Secret Key Present: " + (secretKey != null && !secretKey.isEmpty()));
	}
	@Bean
	public BedrockAgentRuntimeClient bedrockAgentClient() {
		AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(accessKeyId, secretKey,
				"IQoJb3JpZ2luX2VjEJb//////////wEaCXVzLWVhc3QtMSJGMEQCID1Sa0HQpjpMjmy2aHLjFibjXAzrtQm/t8F8Icirw549AiASVbNIu5AwEEx7okwE8MqznK+JwW3GjDMxhVehfys08SqZAghfEAEaDDIzNjY5NTYyMzQ5MyIMrDcDiTNveK4OIQOSKvYB7uJWT0Sn6sCkYKeo0dGqB08S594fAkAsFW7G5yEA46EfodfdvtaFrbo2MIdLbK1sJvJVnQccfQ59s+G/7n3e9lv9FX2Eh7eY6j5cYT+8yOJGb75ndrFNX8mdWkbSqNJZpE+mdXZGHcwWyPdrYAhQuslyuW3NLZ4pTAmkvr3z+xmiI0WPoZgNwPaMpIC0j2xZJK6HYmHLRyYyYqRsdD/19PlXI634adrQ2/+/XRr+ExAH8Wa+S9bKa6xUPeW+Genzrmx3s48828vGS2rPU5Ks5U7Is9RbTj0juwPw2vGTNRfPpljmOX9pxni+UlTKBwqZT0c49JPbMNX51sEGOp4BRcahlzEPMK9jtwj2/vCGfUWwE+0ejWXEnhozxY1/+5Q7Ryh2+wfKtf4QFa6d63aXfcldHj88Me+d/xHiAnYPxCdmAdDsJQYUy38Rl4PThtQl1gVE86mzrJ1YYa8Z7UdM8fWUH1CS2GNIwhTiBxFA0mM4/gKP162buSnsKvIjhRgW6B9vzl/uQnKtF+1AoR/ythPPxLis4iS98P0OoEQ=");
		StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(sessionCredentials);
		return BedrockAgentRuntimeClient.builder().region(Region.of(region)).credentialsProvider(credentialsProvider)
				.build();
	}
//	@Bean
//    public BedrockRuntimeClient bedrockClient() {
//		AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(accessKeyId, secretKey,
//				"IQoJb3JpZ2luX2VjEJb//////////wEaCXVzLWVhc3QtMSJGMEQCID1Sa0HQpjpMjmy2aHLjFibjXAzrtQm/t8F8Icirw549AiASVbNIu5AwEEx7okwE8MqznK+JwW3GjDMxhVehfys08SqZAghfEAEaDDIzNjY5NTYyMzQ5MyIMrDcDiTNveK4OIQOSKvYB7uJWT0Sn6sCkYKeo0dGqB08S594fAkAsFW7G5yEA46EfodfdvtaFrbo2MIdLbK1sJvJVnQccfQ59s+G/7n3e9lv9FX2Eh7eY6j5cYT+8yOJGb75ndrFNX8mdWkbSqNJZpE+mdXZGHcwWyPdrYAhQuslyuW3NLZ4pTAmkvr3z+xmiI0WPoZgNwPaMpIC0j2xZJK6HYmHLRyYyYqRsdD/19PlXI634adrQ2/+/XRr+ExAH8Wa+S9bKa6xUPeW+Genzrmx3s48828vGS2rPU5Ks5U7Is9RbTj0juwPw2vGTNRfPpljmOX9pxni+UlTKBwqZT0c49JPbMNX51sEGOp4BRcahlzEPMK9jtwj2/vCGfUWwE+0ejWXEnhozxY1/+5Q7Ryh2+wfKtf4QFa6d63aXfcldHj88Me+d/xHiAnYPxCdmAdDsJQYUy38Rl4PThtQl1gVE86mzrJ1YYa8Z7UdM8fWUH1CS2GNIwhTiBxFA0mM4/gKP162buSnsKvIjhRgW6B9vzl/uQnKtF+1AoR/ythPPxLis4iS98P0OoEQ=");
//		StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(sessionCredentials);
//        return BedrockRuntimeClient.builder()
//            .region(Region.of(region))
//            .credentialsProvider(credentialsProvider)
//            .build();
//    }
	
}
