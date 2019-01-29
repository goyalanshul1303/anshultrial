package com.cartonwale.provider.api.service.impl;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cartonwale.common.model.User;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.common.util.ServiceUtil;
import com.cartonwale.provider.api.dao.ProviderDao;
import com.cartonwale.provider.api.model.Provider;
import com.cartonwale.provider.api.service.ProviderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProviderServiceImpl extends GenericServiceImpl<Provider> implements ProviderService {
	
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
		
		return super.add(provider);
	}
	
	@Override
	public Provider edit(Provider provider) {
		
		return super.edit(provider);
	}
	
	@Override
	public void addProviderUser(Provider provider, String authToken) {

		User user = getProviderUser(provider);

		ResponseEntity<String> responseEntity = ServiceUtil.call(HttpMethod.POST, authToken,
				Arrays.asList(MediaType.APPLICATION_JSON), null, "http://AUTH-SERVICE/providers",
				getProviderUserAsString(user), restTemplate);

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
		user.setUsername(provider.getCompanyPAN());
		user.setEntityId(provider.getId());
		return user;
	}
	
}
