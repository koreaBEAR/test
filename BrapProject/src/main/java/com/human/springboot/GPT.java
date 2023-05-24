package com.human.springboot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GPT {
	private final RestTemplate restTemplate;
    private final String apiKey;
	
	public GPT(@Value("${openai.api.apiKey}") String apiKey) {
		this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;
	}
	 public String generateResponse(String message) {
	        String url = "https://api.openai.com/v1/engines/davinci-codex/completions";
	        HttpHeaders headers = new HttpHeaders();
	        headers.setBearerAuth(apiKey);
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        Map<String, Object> body = new HashMap<>();
	        body.put("prompt", message);
	        body.put("max_tokens", 100);

	        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

	        ResponseEntity<ChatCompletionResponse> responseEntity = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                requestEntity,
	                ChatCompletionResponse.class
	        );

	        if (responseEntity.getStatusCode() == HttpStatus.OK) {
	            ChatCompletionResponse response = responseEntity.getBody();
	            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
	                return response.getChoices().get(0).getText().trim();
	            }
	        }

	        return "응답 생성에 실패했습니다.";
	    }
}
