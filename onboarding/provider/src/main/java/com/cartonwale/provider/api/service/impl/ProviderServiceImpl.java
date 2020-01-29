package com.cartonwale.provider.api.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.cartonwale.common.exception.BadRequestException;
import com.cartonwale.common.model.SMSRequestBody.SMSBodyBuilder;
import com.cartonwale.common.model.User;
import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.common.util.SMSTemplates;
import com.cartonwale.common.util.SMSUtil;
import com.cartonwale.common.util.ServiceUtil;
import com.cartonwale.provider.api.dao.ProviderDao;
import com.cartonwale.provider.api.model.Product;
import com.cartonwale.provider.api.model.Provider;
import com.cartonwale.provider.api.service.ProviderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProviderServiceImpl extends GenericServiceImpl<Provider> implements ProviderService {
	
	private Logger logger = LoggerFactory.getLogger(ProviderServiceImpl.class);
	
	@Autowired
	private ProviderDao productDao;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PostConstruct
	void init() {
		init(Provider.class, productDao);
	}
	
	@Override
	public Provider add(Provider provider) {
		
		Provider createdProvider = super.add(provider);
		sendConfirmation(createdProvider);
		return createdProvider;
	}

	@Override
	public Provider edit(Provider provider) {
		
		return super.edit(provider);
	}
	
	@Override
	public void addProviderUser(Provider provider, String authToken) {

		User user = getProviderUser(provider);

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = ServiceUtil.call(HttpMethod.POST, authToken,
					Arrays.asList(MediaType.APPLICATION_JSON), null, "http://AUTH-SERVICE/providers",
					getProviderUserAsString(user), restTemplate);
		} catch (HttpClientErrorException ex) {
			delete(provider);
			if(HttpStatus.FORBIDDEN.equals(ex.getStatusCode()))
				throw new BadRequestException("Email or Phone already registered");
			else
				throw new BadRequestException("Some exception occurred while creating user");
		}
		
		logger.info("User Created for Provider: " + provider.getId());

	}
	
	private String getProviderUserAsString(User user) {
		ObjectMapper mapper = new ObjectMapper();

		String json = null;
		try {
			json = mapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
		return json;
	}

	private User getProviderUser(Provider provider) {
		User user = new User();
		user.setEmail(provider.getEmail());
		user.setFirstName(provider.getContactName());
		user.setLastName("");
		//user.setPassword(PasswordGenerator.generatePassword());
		user.setRegisteredOn(new Date());
		user.setStatus(0);
		user.setUsername(provider.getEmail());
		user.setEntityId(provider.getId());
		user.setCompanyName(provider.getCompanyName());
		user.setContactNumber(provider.getPhones().get(0).getNumber());
		return user;
	}

	@Override
	public List<Product> getProducts(String authToken) {
		
		ResponseEntity<List<Product>> responseEntity = ServiceUtil.callByType(HttpMethod.GET, authToken,
				Arrays.asList(MediaType.APPLICATION_JSON), null, "http://PRODUCT-SERVICE/provider/"+SecurityUtil.getAuthUserDetails().getEntityId(),
				"", restTemplate, new ParameterizedTypeReference<List<Product>>() {});
		return responseEntity.getBody();
	}
	
	private void sendConfirmation(Provider createdProvider) {
		
		SMSBodyBuilder builder = new SMSBodyBuilder(SMSTemplates.PROVIDER_CREATED, createdProvider.getPhones().get(0).getNumber());
		SMSUtil.getInstance(restTemplate).sendSMS(builder.VAR1(createdProvider.getContactName()).VAR2(createdProvider.getEmail()).build());
	}
	
}
