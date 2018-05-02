package com.siteminder.tlc.validators;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.siteminder.tlc.Email;
import com.siteminder.tlc.EmailUser;

public class PrimaryRecipientsRuleTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
		
	@Test
	public void testValidate() throws InvalidParameterException  {

		Email email = new Email();
		
		EmailUser eu0 = new EmailUser();
		eu0.setName("abc");
		eu0.setEmail("abc@mail.com");
		
		email.setSender(eu0);

		/* No Primary Recipients */

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
		
		thrown.expect(InvalidParameterException.class);
		thrown.expectMessage("There should be at lease one recipient in the recipients parameter (to:).");

		new PrimaryRecipientsRule().validate(email);
		
		
	}


	

}
