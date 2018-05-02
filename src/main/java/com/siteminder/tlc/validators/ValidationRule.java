package com.siteminder.tlc.validators;

import com.siteminder.tlc.Email;

/**
 * Interface to implement a validation rule for email
 * 
 * @author Hassan Mughal
 *
 */
public interface ValidationRule {

	void validate(Email email) throws InvalidParameterException;
}
