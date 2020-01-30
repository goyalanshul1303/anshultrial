package com.cartonwale.common.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cartonwale.common.model.SMSRequestBody;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SMSUtil {
	
	private final Logger logger = LoggerFactory.getLogger(SMSUtil.class);

	private static final String twoFactorURL = "https://2factor.in/API/V1/";

	private static final String API_KEY = "4a80b40a-3ba6-11ea-9fa5-0200cd936042";

	private static final String tSMS = twoFactorURL + API_KEY + "/ADDON_SERVICES/SEND/TSMS";

	private RestTemplate restTemplate;

	private static SMSUtil INSTANCE;

	private SMSUtil(RestTemplate restTemplate) {

		this.restTemplate = restTemplate;
	}

	public boolean sendSMS(SMSRequestBody smsBody) {

		ResponseEntity<String> response;
		try {
			response = ServiceUtil.call(HttpMethod.POST, null, null, null, tSMS,
					getSMSBodyAsMap(smsBody), new RestTemplate(), MediaType.MULTIPART_FORM_DATA);
			
			if (HttpStatus.OK.equals(response.getStatusCode()))
				return true;
			
			return false;
		} catch(Exception e) {
			
			logger.error(e.getMessage());
			return false;
		}

		
	}

	private LinkedMultiValueMap<String, String> getSMSBodyAsMap(SMSRequestBody body) {
		ObjectMapper mapper = new ObjectMapper();

		LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		
		Map<String, String> maps = mapper.convertValue(body, new TypeReference<Map<String,String>>() {});
		parameters.setAll(maps);			
		
		return parameters;
	}

	public static SMSUtil getInstance(RestTemplate restTemplate) {

		if (INSTANCE == null) {

			synchronized (SMSUtil.class) {
				if (INSTANCE == null) {
					INSTANCE = new SMSUtil(restTemplate);
				}
			}
		}

		return INSTANCE;
	}
}
