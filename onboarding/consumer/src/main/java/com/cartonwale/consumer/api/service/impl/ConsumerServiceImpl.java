package com.cartonwale.consumer.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cartonwale.common.exception.BadRequestException;
import com.cartonwale.common.model.User;
import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.common.util.ServiceUtil;
import com.cartonwale.consumer.api.dao.ConsumerDao;
import com.cartonwale.consumer.api.model.Consumer;
import com.cartonwale.consumer.api.model.Product;
import com.cartonwale.consumer.api.service.ConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConsumerServiceImpl extends GenericServiceImpl<Consumer> implements ConsumerService {

	@Autowired
	private ConsumerDao consumerDao;
	
	@Autowired
	private RestTemplate restTemplate;

	@PostConstruct
	void init() {
		init(Consumer.class, consumerDao);
	}

	@Override
	public Consumer add(Consumer consumer) {

		return super.add(consumer);
	}

	private String getConsumerUserAsString(User user) {
		ObjectMapper mapper = new ObjectMapper();

		String json = null;
		try {
			json = mapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
		return json;
	}

	private User getConsumerUser(Consumer consumer) {
		User user = new User();
		user.setEmail(consumer.getEmail());
		user.setFirstName(consumer.getContactName());
		user.setLastName("");
		//user.setPassword(PasswordGenerator.generatePassword());
		user.setRegisteredOn(new Date());
		user.setStatus(0);
		user.setUsername(consumer.getCompanyPAN());
		user.setEntityId(consumer.getId());
		user.setCompanyName(consumer.getConsumerName());
		return user;
	}

	@Override
	public Consumer edit(Consumer consumer) {

		return super.edit(consumer);
	}

	@Override
	public void addConsumerUser(Consumer consumer, String authToken) {

		User user = getConsumerUser(consumer);

		ResponseEntity<String> responseEntity = ServiceUtil.call(HttpMethod.POST, authToken,
				Arrays.asList(MediaType.APPLICATION_JSON), null, "http://AUTH-SERVICE/consumers",
				getConsumerUserAsString(user), restTemplate);
		
		if(!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
			delete(consumer);
			if(HttpStatus.FORBIDDEN.equals(responseEntity.getStatusCode()))
				throw new BadRequestException("Email already Registered");
			else
				throw new BadRequestException("Some exception occurred while creating user");
		}

	}
	
	@Override
	public List<Product> getProducts(String authToken) {
		
		List<Product> products = new ArrayList<Product>();
		Consumer consumer = getById(SecurityUtil.getAuthUserDetails().getEntityId());
		
		if(consumer.getBoundedProviderId() != null && !consumer.getBoundedProviderId().isEmpty()) {
			ResponseEntity<List<Product>> responseEntity = ServiceUtil.callByType(HttpMethod.GET, authToken,
					Arrays.asList(MediaType.APPLICATION_JSON), null, "http://PRODUCT-SERVICE/provider/"+consumer.getBoundedProviderId(),
					"", restTemplate, new ParameterizedTypeReference<List<Product>>() {});
			products.addAll(responseEntity.getBody());
		} else {
			ResponseEntity<List<Product>> responseEntity = ServiceUtil.callByType(HttpMethod.GET, authToken,
					Arrays.asList(MediaType.APPLICATION_JSON), null, "http://PRODUCT-SERVICE/consumer/"+SecurityUtil.getAuthUserDetails().getEntityId(),
					"", restTemplate, new ParameterizedTypeReference<List<Product>>() {});
			products.addAll(responseEntity.getBody());
		}
		
		return products;
	}

}
