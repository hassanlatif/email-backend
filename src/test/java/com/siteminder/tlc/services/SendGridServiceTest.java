package com.siteminder.tlc.services;

import static org.junit.Assert.*;

import org.junit.Test;

import com.siteminder.tlc.Email;
import com.siteminder.tlc.EmailUser;
import com.siteminder.tlc.services.SendGridService;

public class SendGridServiceTest {

	@Test
	public void testEmailBuilder() {
		String API_KEY = "Dummy";
		String API_URL = "Dummy";
		
		SendGridService sgService = new SendGridService(API_URL, API_KEY);
		
		Email email = new Email();
		
		EmailUser eu0 = new EmailUser();
		eu0.setName("abc");
		eu0.setEmail("abc@mail.com");
		
		email.setSender(eu0);		
		EmailUser eu1 = new EmailUser();
		eu1.setEmail("def@mail.com");
		eu1.setName("def");
		email.getToRecipients().add(eu1);
		EmailUser eu2 = new EmailUser();
		eu2.setEmail("ghi@mail.com");
		eu2.setName("ghi");
		email.getToRecipients().add(eu2);

		EmailUser eu3 = new EmailUser();
		eu3.setEmail("jkl@mail.com");
		eu3.setName("jkl");
		email.getCcRecipients().add(eu3);
		EmailUser eu4 = new EmailUser();
		eu4.setEmail("mno@mail.com");
		eu4.setName("mno");
		email.getCcRecipients().add(eu4);
		
		EmailUser eu5 = new EmailUser();
		eu5.setEmail("pqr@mail.com");
		eu5.setName("pqr");
		email.getBccRecipients().add(eu5);
		EmailUser eu6 = new EmailUser();
		eu6.setEmail("stu@mail.com");
		eu6.setName("stu");
		email.getBccRecipients().add(eu6);

		
		email.setSubject("Testing Subject");
		email.setContent("Testing Text");
		
		String strEmail = sgService.buildEmailJSON(email);
		
		String strTest = "{\"personalizations\":"
				+ "[{\"cc\":"
				+ "[{\"name\":\"jkl\",\"email\":\"jkl@mail.com\"},{\"name\":\"mno\",\"email\":\"mno@mail.com\"}],"
				+ "\"bcc\":"
				+ "[{\"name\":\"pqr\",\"email\":\"pqr@mail.com\"},{\"name\":\"stu\",\"email\":\"stu@mail.com\"}],"
				+ "\"to\":"
				+ "[{\"name\":\"def\",\"email\":\"def@mail.com\"},{\"name\":\"ghi\",\"email\":\"ghi@mail.com\"}]}],"
				+ "\"subject\":\"Testing Subject\",\"from\":{\"name\":\"abc\",\"email\":\"abc@mail.com\"},"
				+ "\"content\":[{\"type\":\"text/plain\",\"value\":\"Testing Text\"}]}";

		
		System.out.println("Compare Test JSON: " + strTest);
		
		assertEquals(strTest, strEmail);	}

}
