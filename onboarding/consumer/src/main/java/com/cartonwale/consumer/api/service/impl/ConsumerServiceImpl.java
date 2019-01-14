package com.cartonwale.consumer.api.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartonwale.common.model.Mail;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.common.util.MailUtil;
import com.cartonwale.common.util.PasswordGenerator;
import com.cartonwale.consumer.api.dao.ConsumerDao;
import com.cartonwale.consumer.api.model.Consumer;
import com.cartonwale.consumer.api.service.ConsumerService;

@Service
public class ConsumerServiceImpl extends GenericServiceImpl<Consumer> implements ConsumerService {
	
	@Autowired
	private ConsumerDao consumerDao;
	
	@PostConstruct
	void init() {
		init(Consumer.class, consumerDao);
	}
	
	@Override
	public Consumer add(Consumer consumer) {
		Consumer created = super.add(consumer);
		MailUtil.sendMail(new Mail(consumer.getEmail(), "Your Account Details", "anshul.golu@gmail.com", getMailBody(consumer)));
		return created;
	}
	
	private String getMailBody(Consumer consumer) {
		
		return "UserId: " + consumer.getConsumerName() + "\nPassword: " + PasswordGenerator.generatePassword();
	}

	@Override
	public Consumer edit(Consumer consumer) {
		
		return super.edit(consumer);
	}
	
}
