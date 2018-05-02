package com.siteminder.tlc.validators;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.siteminder.tlc.Email;
import com.siteminder.tlc.EmailUser;

/**
 * Checks that all recipient user names and email addresses are valid.
 * 
 * @author Hassan Mughal
 *
 */
public class RecipientsValidationRule implements ValidationRule {

	@Override
	public void validate(Email email) throws InvalidParameterException {
		validateRecipients(email.getToRecipients(), "to:");
		validateRecipients(email.getCcRecipients(), "cc:");
		validateRecipients(email.getBccRecipients(), "bcc:");
	}

	public static void validateRecipients(ArrayList<EmailUser> recipientsList, String recipientType)
			throws InvalidParameterException {

		ArrayList<String> invalidEmails = new ArrayList<String>();
		ArrayList<String> invalidNames = new ArrayList<String>();

		/* email regex source: http://emailregex.com */
		String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		Pattern emailPattern = Pattern.compile(emailRegex);

		String nameRegex = "[A-Za-z0-9 _,.'-]+";
		Pattern namePattern = Pattern.compile(nameRegex);

		if (recipientsList != null && recipientsList.size() > 0) {

			for (EmailUser emailUser : recipientsList) {

				if (!emailPattern.matcher(emailUser.getEmail()).matches()) {
					invalidEmails.add(emailUser.getEmail());
				}

				if (!namePattern.matcher(emailUser.getName()).matches()) {
					invalidNames.add(emailUser.getName());
				}
			}
		}

		if (invalidEmails.size() > 0) {
			StringBuilder strInvalidEmails = new StringBuilder();
			for (String s : invalidEmails) {
				strInvalidEmails.append(s);
				strInvalidEmails.append(" - ");
			}
			throw new InvalidParameterException(
					invalidEmails.size() + " invalid email(s) found in the recipients parameter ("
							+ recipientType + ")." + " Please correct these email(s): " + strInvalidEmails);
		}

		if (invalidNames.size() > 0) {
			StringBuilder strInvalidNames = new StringBuilder();
			for (String s : invalidNames) {
				strInvalidNames.append(s);
				strInvalidNames.append(" - ");
			}
			throw new InvalidParameterException(invalidNames.size()
					+ " invalid user name(s) found in the recipients parameter (" + recipientType + ")."
					+ " Valid characters are A-Z a-z 0-9 . _ - '. Please correct these name(s): " + strInvalidNames);
		}

	}
	
}
