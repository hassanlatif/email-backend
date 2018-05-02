package com.siteminder.tlc.services;

import java.io.IOException;

import com.siteminder.tlc.Email;

/**
 * Parent Abstract class for all email Service Provider. Implements the 
 * chain of responsiblity design pattern
 * 
 * @author Hassan Mughal
 *
 */
public abstract class EmailService {
	
	private String APIURL;
	private String APIKey;
	
	EmailService(String apiURL, String apiKey) {
		this.APIKey = apiKey;
		this.APIURL = apiURL;
	}
	
	protected String getAPIURL() {
		return APIURL;
	}

	protected String getAPIKey() {
		return APIKey;
	}
	
	protected EmailService nextEmailService;

	/**
	 * This function chains the email service providers. 
	 * 
	 * @param email
	 * @throws ServiceProviderException
	 */
	public void setNextEmailService(EmailService nextEmailService) {
		this.nextEmailService = nextEmailService;
	}

	abstract protected String getEmailServiceName();


	/**
	 * Attempts to send email to the service provider.
	 * @param email
	 * @throws IOException if there is an internal error
	 * @throws ServiceProviderException if there is a service provider error
	 */
	abstract protected void send(Email email) throws IOException, ServiceProviderException;

	/**
	 * Tries to send a email via a service provider. If one service provider 
	 * fails it switches to the next available service provider.
	 * 
	 * @param email - the email to send
	 * @throws ServiceProviderException if there is a service provider error
	 */
	public void sendEmail(Email email) throws ServiceProviderException {

		try {

			send(email);

		} catch (Exception e) {

			System.err.println("Error - Cannot send email using " + getEmailServiceName());
			
			if (nextEmailService != null) {
				
				System.out.println("Trying to send email using " + nextEmailService.getEmailServiceName());
				nextEmailService.sendEmail(email);
				
			} else {
				String serverError = "Server Error - Unable to send email.";
				System.err.println(serverError);
				throw new ServiceProviderException(serverError);
			}

		}
	}

}
