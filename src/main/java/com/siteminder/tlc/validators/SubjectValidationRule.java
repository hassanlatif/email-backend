package com.siteminder.tlc.validators;

import com.siteminder.tlc.Email;

/**
 * Validates Email Subject field
 * 
 * @author Hassan Mughal
 *
 */
public class SubjectValidationRule implements ValidationRule {

	@Override
	public void validate(Email email) throws InvalidParameterException {
		if (email.getSubject() == null) {
			throw new InvalidParameterException("Subject parameter is missing. Please provide Subject.");
		}

		if (email.getSubject().trim().equals("")) {
			throw new InvalidParameterException("Subject parameter should not be empty.");
		}

	}

}
