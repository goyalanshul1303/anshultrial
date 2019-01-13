package com.cartonwale.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.cartonwale.common.model.Mail;

public class MailUtil {
	
	public static void sendMail(Mail mail){
		
		Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);

	    try {
	      Message msg = new MimeMessage(session);
	      msg.setFrom(new InternetAddress(mail.getFromAddress(), "Cartonwale Admin"));
	      msg.addRecipient(Message.RecipientType.TO,
	                       new InternetAddress(mail.getToAddress(), "Mr. User"));
	      msg.setSubject(mail.getSubject());
	      msg.setText(mail.getBody());
	      Transport.send(msg);
	    } catch (AddressException e) {
	      // ...
	    } catch (MessagingException e) {
	      // ...
	    } catch (UnsupportedEncodingException e) {
	      // ...
	    }
	}

}
