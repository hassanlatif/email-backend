package com.siteminder.tlc.validators;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.siteminder.tlc.Email;
import com.siteminder.tlc.EmailUser;

public class RecipientsValidationRuleTest {

	Email email;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		email = new Email();

		EmailUser eu0 = new EmailUser();
		eu0.setName("abc//\\def");
		eu0.setEmail("abc@mail.com");
		email.setSender(eu0);

		email.setSubject("Testing Subject");
		email.setContent("Testing Content");

	}

	@Test
	public void testRecipientEmails() throws InvalidParameterException {

		EmailUser eu1 = new EmailUser();
		/* Invalid Email */
		eu1.setEmail("def@mail@com");
		eu1.setName("def");
		email.getToRecipients().add(eu1);

		thrown.expect(InvalidParameterException.class);
		thrown.expectMessage("invalid email(s) found in the recipients parameter");

		new RecipientsValidationRule().validate(email);

	}
	
	@Test
	public void testRecipientNames() throws InvalidParameterException {

		EmailUser eu1 = new EmailUser();
		/* Invalid Email */
		eu1.setEmail("def@mail.com");
		eu1.setName("def//\\ghi");
		email.getToRecipients().add(eu1);

		thrown.expect(InvalidParameterException.class);
		thrown.expectMessage("invalid user name(s) found in the recipients parameter");

		new RecipientsValidationRule().validate(email);

	}
	

}
