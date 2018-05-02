package com.siteminder.tlc.validators;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.siteminder.tlc.Email;
import com.siteminder.tlc.EmailUser;

public class SenderValidationRuleTest {

	Email email;

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setup() {
		email = new Email();

		EmailUser eu1 = new EmailUser();
		eu1.setEmail("def@mail.com");
		eu1.setName("def");
		email.getToRecipients().add(eu1);

		email.setSubject("Testing Subject");
		email.setContent("Testing Content");
	}

	@Test
	public void testSenderEmail() throws InvalidParameterException {

		EmailUser eu0 = new EmailUser();
		eu0.setName("abc");
		/* Invalid Email */
		eu0.setEmail("abc@@@def");
		email.setSender(eu0);

		thrown.expect(InvalidParameterException.class);
		thrown.expectMessage("The sender email address is invalid.");

		new SenderValidationRule().validate(email);

	}
	
	@Test
	public void testSenderName() throws InvalidParameterException {

		EmailUser eu0 = new EmailUser();
		/* Invalid Name */
		eu0.setName("abc//\\def");
		eu0.setEmail("abc@mail.com");
		email.setSender(eu0);

		thrown.expect(InvalidParameterException.class);
		thrown.expectMessage("The sender name is invalid.");

		new SenderValidationRule().validate(email);

	}	

}
