package com.siteminder.tlc.services;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

import com.siteminder.tlc.Email;
import com.siteminder.tlc.EmailUser;

/**
 * Implementation of provider for MailGun Email service
 * 
 * @author Hassan Mughal
 *
 */
public class MailGunService extends EmailService {

	/* OK */
	public static final int ACCEPTED = 200;
	
	/* Client Errors */
	public static final int BAD_REQUEST = 400;
	public static final int UNAUTHORIZED = 401;
	public static final int REQUEST_FAILED = 402;
	public static final int NOT_FOUND = 404;
	
	/* Server Errors */
	public static final int SERVER_ERROR1 = 500;
	public static final int SERVER_ERROR2 = 502;
	public static final int SERVER_ERROR3 = 503;
	public static final int SERVER_ERROR4 = 504;

	public MailGunService(String apiURL, String apiKey) {
		super(apiURL, apiKey);
	}

	@Override
	protected String getEmailServiceName() {
		return "MailGun";
	}

	/**
	 * Attempts to send the email to MailGun service via HTTP
	 * @param email - Email Object
	 * @throws IOException if there is an internal error
	 * @throws ServiceProviderException if there is a service provider error
	 */
	@Override
	protected void send(Email email) throws IOException, ServiceProviderException {

		URL url = new URL(this.getAPIURL());
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setConnectTimeout(5000);
		httpConnection.setRequestProperty("Authorization",
				"Basic " + Base64.getEncoder().encodeToString(this.getAPIKey().getBytes()));
		httpConnection.setRequestMethod("POST");

		OutputStreamWriter osWriter = new OutputStreamWriter(httpConnection.getOutputStream());
		osWriter.write(buildEmailStr(email));
		osWriter.flush();
		osWriter.close();

		int responseCode = httpConnection.getResponseCode();
		String responseMessage = httpConnection.getResponseMessage();
		handleServerResponse(responseCode, responseMessage);
	}

	/**
	 * Handles server response based on the status and error codes.
	 * 
	 * @param responseCode - status/error code
	 * @param responseMessage - status message
	 * @throws ServiceProviderException if service provider fails
	 */
	public void handleServerResponse(int responseCode, String responseMessage)
			throws ServiceProviderException {
		String serverResponse = responseCode + " - " + responseMessage;

		switch (responseCode) {
		case ACCEPTED:
			System.out.println(serverResponse);
			System.out.println("Email sent to MailGun Server for delivery.");
			break;
		case BAD_REQUEST:
		case UNAUTHORIZED:
		case REQUEST_FAILED:
		case NOT_FOUND:
			System.err.println("Error " + serverResponse);
			throw new ServiceProviderException("Error " + serverResponse);
		case SERVER_ERROR1:
		case SERVER_ERROR2:
		case SERVER_ERROR3:
		case SERVER_ERROR4:
			System.err.println("Server Error" + serverResponse);
			throw new ServiceProviderException("Server Error " + serverResponse);
		default:
			System.err.println("Unknown Error" + serverResponse);
			throw new ServiceProviderException("Unknown Error " + serverResponse);
		}

	}
	
	/**
	 * Builds the HTTP POST query string as per the MailGun specs
	 * 
	 * @param email Email Object
	 * @return HTTP POST query string
	 */
	public String buildEmailStr(Email email) {

		StringBuilder emailParts = new StringBuilder();
		emailParts.append("from=");
		emailParts.append(email.getSender().getName());
		emailParts.append(" <" + email.getSender().getEmail()+ ">");
		
		emailParts.append("&");
		buildRecipientsStr(emailParts, "to=", email.getToRecipients());

		if (email.getCcRecipients().size() > 0) {
			emailParts.append("&");
			buildRecipientsStr(emailParts, "cc=", email.getCcRecipients());
		}

		if (email.getBccRecipients().size() > 0) {
			emailParts.append("&");
			buildRecipientsStr(emailParts, "bcc=", email.getBccRecipients());
		}

		emailParts.append("&");
		emailParts.append("subject=" + encodeParam(email.getSubject()));
		emailParts.append("&");
		emailParts.append("text=" + encodeParam(email.getContent()));

		System.out.println("Mailgun URL Request String: " +emailParts.toString());

		return emailParts.toString();
	}

	/**
	 * Utility function for buildEmailStr to add recipients to the query string.
	 * 
	 * @param emailParts - Main Email Object
	 * @param recipientType - To, CC or BCC
	 * @param recipients - list of recipients from Email Object
	 */
	public void buildRecipientsStr(StringBuilder emailParts, String recipientType, ArrayList<EmailUser> recipients) {

		Iterator<EmailUser> recipientsIterator = recipients.iterator();
		emailParts.append(recipientType);
		while (recipientsIterator.hasNext()) {
			EmailUser recipient = recipientsIterator.next();
			emailParts.append(recipient.getName());
			emailParts.append(" <" + recipient.getEmail() + ">");

			if (recipientsIterator.hasNext()) {
				emailParts.append(",");
			}
		}
	}

	/**
	 * Utility function to convert a String to the 
	 * application/x-www-form-urlencoded MIME format
	 * @param str - input string
	 * @return encoded string
	 */
	public String encodeParam(String str) {

		String strEncoded;

		try {
			strEncoded = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 not supported");
		}

		return strEncoded;
	}

}
