package com.siteminder.tlc.validators;

import com.siteminder.tlc.Email;

/**
 * Validates if at leaset on Primary recipient is available.
 *  
 * @author hmughal
 *
 */
public class PrimaryRecipientsRule implements ValidationRule {

	@Override
	public void validate(Email email) throws InvalidParameterException {

		if (email.getToRecipients() == null) {
			throw new InvalidParameterException("Recipient paramter (to:) is missing. Please provide recipient(s).");
		}

		if (email.getToRecipients().size() < 1) {
			throw new InvalidParameterException(
					"There should be at lease one recipient in the recipients parameter (to:).");
		}

	}

}
