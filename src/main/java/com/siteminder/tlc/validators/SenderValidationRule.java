package com.siteminder.tlc.validators;

import com.siteminder.tlc.Email;

/**
 * Checks that sender user name and email address are valid.
 * 
 * @author Hassan Mughal
 *
 */
public class SenderValidationRule implements ValidationRule {

	@Override
	public void validate(Email email) throws InvalidParameterException {

		if (email.getSender() == null) {
			throw new IllegalArgumentException("Sender parameter is missing. Please provide Sender name and email.");
		}

		/* email regex source: http://emailregex.com */
		String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		String nameRegex = "[A-Za-z0-9 _,.'-]+";

		if (!email.getSender().getEmail().matches(emailRegex)) {
			throw new InvalidParameterException("The sender email address is invalid.");
		}

		if (!email.getSender().getName().matches(nameRegex)) {
			throw new InvalidParameterException("The sender name is invalid. "
					+ "Please use only letters (a-z), numbers (0-9), dashes (-), underscores (_), apostrophes ('), and periods (.).\"");
		}
	}

}
