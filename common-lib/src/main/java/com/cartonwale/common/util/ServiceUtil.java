package com.cartonwale.common.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ServiceUtil<T> {

	public static <T> ResponseEntity<String> call(HttpMethod method, String authToken, List<MediaType> accepts,
			HttpHeaders headers, String serviceUrl, T body, RestTemplate restTemplate) {

		if (headers == null)
			headers = new HttpHeaders();

		if (accepts == null)
			accepts = Arrays.asList(MediaType.APPLICATION_JSON);

		headers.add("Authorization", authToken);
		headers.setAccept(accepts);

		HttpEntity<T> entity = new HttpEntity<T>(body, headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(serviceUrl, method, entity,
				String.class);

		return responseEntity;

	}
}
