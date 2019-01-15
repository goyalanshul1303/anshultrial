package com.cartonwale.common.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ServiceUtil {

	@Autowired
	private static RestTemplate restTemplate;
	
	@Value("${jwt.header}")
    private static String tokenHeader;

	public static ResponseEntity<String> call(HttpMethod method, String authToken, List<MediaType> accepts,
			HttpHeaders headers, String serviceUrl, String body) {

		if (headers == null)
			headers = new HttpHeaders();

		if (accepts == null)
			accepts = Arrays.asList(MediaType.APPLICATION_JSON);

		headers.add("Authorization", authToken);
		headers.setAccept(accepts);

		HttpEntity<String> entity = new HttpEntity<String>(body, headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(serviceUrl, method, entity,
				String.class);

		return responseEntity;

	}
	
	public static String getTokenHeader(){
		return tokenHeader;
	}
}
