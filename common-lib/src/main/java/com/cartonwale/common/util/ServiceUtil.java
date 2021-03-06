package com.cartonwale.common.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ServiceUtil<T> {

	public static ResponseEntity<String> call(HttpMethod method, String authToken, List<MediaType> accepts,
			HttpHeaders headers, String serviceUrl, String body, RestTemplate restTemplate) {

		return call(method, authToken, accepts, headers, serviceUrl, body, restTemplate, null);

	}
	
	public static ResponseEntity<String> call(HttpMethod method, String authToken, List<MediaType> accepts,
			HttpHeaders headers, String serviceUrl, String body, RestTemplate restTemplate, MediaType contentType) {

		if (headers == null)
			headers = new HttpHeaders();

		if (accepts == null)
			accepts = Arrays.asList(MediaType.APPLICATION_JSON);

		headers.add("Authorization", authToken);
		headers.setAccept(accepts);
		
		if(contentType == null)
			contentType = MediaType.APPLICATION_JSON;
		
		headers.setContentType(contentType);

		HttpEntity<String> entity;
		if (body != null) {
			
			entity = new HttpEntity<String>(body, headers);
		} else {
			entity = new HttpEntity<String>(headers);
		}

		ResponseEntity<String> responseEntity = restTemplate.exchange(serviceUrl, method, entity,
				String.class);

		return responseEntity;

	}
	
	public static ResponseEntity<String> call(HttpMethod method, String authToken, List<MediaType> accepts,
			HttpHeaders headers, String serviceUrl, LinkedMultiValueMap<String, String> map, RestTemplate restTemplate, MediaType contentType) {

		if (headers == null)
			headers = new HttpHeaders();

		if (accepts == null)
			accepts = Arrays.asList(MediaType.APPLICATION_JSON);

		headers.add("Authorization", authToken);
		headers.setAccept(accepts);
		
		if(contentType == null)
			contentType = MediaType.APPLICATION_JSON;
		
		headers.setContentType(contentType);

		HttpEntity<LinkedMultiValueMap<String, String>> entity = null;
		if (map != null) {
			
			entity = new HttpEntity<LinkedMultiValueMap<String, String>>(map, headers);
		} 

		ResponseEntity<String> responseEntity = restTemplate.exchange(serviceUrl, method, entity,
				String.class);

		return responseEntity;

	}
	
	public static <T> ResponseEntity<T> callByType(HttpMethod method, String authToken, List<MediaType> accepts,
			HttpHeaders headers, String serviceUrl, String body, RestTemplate restTemplate, ParameterizedTypeReference<T> type) {

		if (headers == null)
			headers = new HttpHeaders();

		if (accepts == null)
			accepts = Arrays.asList(MediaType.APPLICATION_JSON);

		headers.add("Authorization", authToken);
		headers.setAccept(accepts);
		

		HttpEntity<String> entity;
		if (body != null) {
			headers.setContentType(MediaType.APPLICATION_JSON);
			entity = new HttpEntity<String>(body, headers);
		} else {
			entity = new HttpEntity<String>(headers);
		}

		ResponseEntity<T> responseEntity = restTemplate.exchange(serviceUrl, method, entity,
				type);

		return responseEntity;

	}
	
}
