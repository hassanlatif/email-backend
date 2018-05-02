package com.siteminder.tlc.validators;

import java.util.ArrayList;
import java.util.List;
import com.siteminder.tlc.Email;

/**
 * Iterates through all validation rules and checks them
 * against an Email Object
 * 
 * @author hmughal
 *
 */
public class EmailValidator {

	/**
	 * Validates an email against validation rules.
	 * 
	 * @param email - Email to validation
	 * @throws InvalidParameterException
	 */
	public static void validateEmail(Email email) throws InvalidParameterException {

		List<ValidationRule> rules = new ArrayList<>();
		rules.add(new PrimaryRecipientsRule());
		rules.add(new SenderValidationRule());
		rules.add(new RecipientsValidationRule());
		rules.add(new SubjectValidationRule());
		rules.add(new ContentValidationRule());

		for (ValidationRule rule : rules) {
			rule.validate(email);
		}

	}

}
