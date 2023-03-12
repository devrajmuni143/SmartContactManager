package com.smart.smartcontactmanager.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

	

	public static boolean sendEmail(String msg,String subject,String to) {
		
		boolean f=false;
		
		String host="smtp.gmail.com";
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		//step - 1 : getting session....
		
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("muni.ajitkumar143@gmail.com", "tntvdibxbqdktlls");
			}
			
		});
		System.out.println(session);
		session.setDebug(true);
		//step - 2 : Compose the Message....
		
		MimeMessage message = new MimeMessage(session);
		try {
			//set from
			message.setFrom("muni.ajitkumar143@gmail.com");
			// set to
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			//adding subject
			message.setSubject(subject);
			//adding message
			//message.setText(msg);
			message.setContent(msg,"text/html");
			//step - 3 : Sending message using Transport class
			Transport.send(message);
			
			f=true;
			System.out.println("Mail sent successfully..");
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return f;
	}
	
	
}
