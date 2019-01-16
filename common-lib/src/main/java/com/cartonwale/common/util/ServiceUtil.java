package com.cartonwale.common.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ServiceUtil {

	public static ResponseEntity<String> call(HttpMethod method, String authToken, List<MediaType> accepts,
			HttpHeaders headers, String serviceUrl, String body, RestTemplate restTemplate) {

		if (headers == null)
			headers = new HttpHeaders();

		if (accepts == null)
			accepts = Arrays.asList(MediaType.APPLICATION_JSON);

		headers.add("Authorization", authToken);
		headers.setAccept(accepts);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(body, headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(serviceUrl, method, entity,
				String.class);

		return responseEntity;

	}
}
