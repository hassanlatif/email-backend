package com.siteminder.tlc.validators;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.siteminder.tlc.Email;
import com.siteminder.tlc.EmailUser;

public class SubjectValidationRuleTest {

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
	public void tesSubjectMissing() throws InvalidParameterException {

		/* Subject Missing */
		email.setContent("Testing Subject");

		thrown.expect(InvalidParameterException.class);
		thrown.expectMessage("Subject parameter is missing. Please provide Subject.");

		new SubjectValidationRule().validate(email);
	}

	@Test
	public void tesSubjectEmpty() throws InvalidParameterException {

		email.setSubject("Testing Subject");
		email.setSubject(""); 		/* Subject Empty */

		thrown.expect(InvalidParameterException.class);
		thrown.expectMessage("Subject parameter should not be empty.");

		new SubjectValidationRule().validate(email);
	}

	@Test
	public void tesSubject() throws InvalidParameterException {
		Email email = new Email();

		email.setSubject("Testing Subject");
		email.setSubject("Testing Subject");

		new SubjectValidationRule().validate(email);
	}

}
