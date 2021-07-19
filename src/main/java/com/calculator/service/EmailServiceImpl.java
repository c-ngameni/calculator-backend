package com.calculator.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;

import com.calculator.exception.CalculatorException;

@Service
public class EmailServiceImpl implements EmailService {

	private String username = "7dfafc2a58b30f";

	private String password = "89b1e387ff93f0";

	@Override
	public void sendEmail(String recipient, String subject, String message) throws CalculatorException {

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", true);
		// properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.mailtrap.io");
		properties.put("mail.smtp.port", "2525");
		properties.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(username, password);
			}
		});

		Message mimeMessage = new MimeMessage(session);
		try {
			mimeMessage.setFrom(new InternetAddress("calculator@gmail.com"));
			mimeMessage.setRecipients(RecipientType.TO, InternetAddress.parse(recipient));
			mimeMessage.setSubject(subject);

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(message, "text/html");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			mimeMessage.setContent(multipart);

			Transport.send(mimeMessage);
		} catch (MessagingException e) {
			throw new CalculatorException(e.getMessage());
		}
	}

}
