package com.reservation.service;

import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;


@Service
public class EmailService {
	
	@Value("${sendgrid.api-key}")
	private String sendGridApiKey;
	
	public String sendEmail(String toEmail, String subject, String contentText) {
		Email from = new Email("tisha-anil.sorte@capgemini.com");
		Email to = new Email(toEmail);
		Content content = new Content("text/plain",contentText);
		Mail mail = new Mail(from, subject, to, content);
		
		SendGrid sg = new SendGrid(sendGridApiKey);
		Request request = new Request();
		
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			return "Email sent with status: " + response.getStatusCode();
		} catch(IOException ex) {
			return "Error sending email: " + ex.getMessage();
		}
	}
	
}
