package com.siteminder.tlc.validators;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.siteminder.tlc.Email;
import com.siteminder.tlc.EmailUser;

public class ContentValidationRuleTest {

	Email email;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		email = new Email();
		EmailUser eu0 = new EmailUser();
		eu0.setName("abc");
		eu0.setEmail("abc@mail.com");
		email.setSender(eu0);

		EmailUser eu1 = new EmailUser();
		eu1.setEmail("def@mail.com");
		eu1.setName("def");
		email.getToRecipients().add(eu1);

	}

	@Test
	public void tesContentMissing() throws InvalidParameterException {

		email.setSubject("Testing Subject");
		/* Content Missing */

		thrown.expect(InvalidParameterException.class);
		thrown.expectMessage("Content parameter is missing. Please provide Content.");

		new ContentValidationRule().validate(email);
	}

	@Test
	public void tesContentEmpty() throws InvalidParameterException {

		email.setSubject("Testing Subject");
		email.setContent(""); 		/* Content Empty */

		thrown.expect(InvalidParameterException.class);
		thrown.expectMessage("Content parameter should not be empty.");

		new ContentValidationRule().validate(email);
	}

	@Test
	public void tesContent() throws InvalidParameterException {
		Email email = new Email();

		email.setSubject("Testing Subject");
		email.setContent("Testing Content");

		new ContentValidationRule().validate(email);
	}

}
