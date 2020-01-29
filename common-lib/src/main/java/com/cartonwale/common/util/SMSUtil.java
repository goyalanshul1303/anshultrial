package com.cartonwale.common.util;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cartonwale.common.model.SMSRequestBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SMSUtil {

	private static final String twoFactorURL = "https://2factor.in/API/V1/";

	private static final String API_KEY = "4a80b40a-3ba6-11ea-9fa5-0200cd936042";

	private static final String tSMS = twoFactorURL + API_KEY + "/ADDON_SERVICES/SEND/TSMS";

	private RestTemplate restTemplate;

	private static SMSUtil INSTANCE;

	private SMSUtil(RestTemplate restTemplate) {

		this.restTemplate = restTemplate;
	}

	public boolean sendSMS(SMSRequestBody smsBody) {

		ResponseEntity<String> response = ServiceUtil.call(HttpMethod.POST, null, null, null, tSMS,
				getSMSBodyAsString(smsBody), restTemplate);

		if (HttpStatus.OK.equals(response.getStatusCode()))
			return true;
		
		return false;
	}

	private String getSMSBodyAsString(SMSRequestBody body) {
		ObjectMapper mapper = new ObjectMapper();

		String json = null;
		try {
			json = mapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
		return json;
	}

	public static SMSUtil getInstance(RestTemplate restTemplate) {

		if (INSTANCE == null) {

			synchronized (INSTANCE) {
				if (INSTANCE == null) {
					INSTANCE = new SMSUtil(restTemplate);
				}
			}
		}

		return INSTANCE;
	}
}
