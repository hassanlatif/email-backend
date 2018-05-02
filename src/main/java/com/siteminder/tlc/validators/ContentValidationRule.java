package com.siteminder.tlc.validators;

import com.siteminder.tlc.Email;

/**
 * Validates Email Content field
 * 
 * @author Hassan Mughal
 *
 */
public class ContentValidationRule implements ValidationRule {

	@Override
	public void validate(Email email) throws InvalidParameterException {
		if (email.getContent() == null) {
			throw new InvalidParameterException("Content parameter is missing. Please provide Content.");
		}

		if (email.getContent().trim().equals("")) {
			throw new InvalidParameterException("Content parameter should not be empty.");
		}
	}

}
