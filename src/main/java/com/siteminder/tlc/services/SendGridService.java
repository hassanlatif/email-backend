package com.siteminder.tlc.services;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.siteminder.tlc.Email;
import com.siteminder.tlc.EmailUser;

/**
 * Implementation of provider for MailGun Email service
 * 
 * @author Hassan Mughal
 *
 */
public class SendGridService extends EmailService {

	/* OK */
	public static final int OK = 200;
	public static final int ACCEPTED = 202;
	
	/* Client Errors */
	public static final int BAD_REQUEST = 400;	
	public static final int UNAUTHORIZED = 401;
	public static final int FORBIDDEN = 403;
	public static final int NOT_FOUND = 404; 
	public static final int METHOD_NOT_ALLOWED = 405; 
	public static final int PAYLOAD_TOO_LARGE = 413;
	public static final int UNSUPPORTED_MEDIA_TYPE = 415;
	public static final int TOO_MANY_REQUESTS = 429;
	
	/* Server Errors */
	public static final int SERVER_UNAVAILABLE = 500;
	public static final int SERVICE_NOT_AVAILABLE = 503;
	
	public SendGridService(String apiURL, String apiKey) {
		super(apiURL, apiKey);
	}

	@Override
	protected String getEmailServiceName() {
		return "SendGrid";
	}

	/**
	 * Attempts to send the email to SendGrid service via HTTP
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
		httpConnection.setRequestProperty("Content-Type", "application/json");
		httpConnection.setRequestProperty("Accept", "application/json");
		httpConnection.setRequestProperty("Authorization", "Bearer " + this.getAPIKey());
		httpConnection.setRequestMethod("POST");

		OutputStreamWriter osWriter = new OutputStreamWriter(httpConnection.getOutputStream());

		osWriter.write(buildEmailJSON(email));
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
		case OK:
		case ACCEPTED:
			System.out.println(serverResponse);
			System.out.println("Email sent to SendGrid Server for delivery.");
			break;
		case BAD_REQUEST:
		case UNAUTHORIZED:
		case FORBIDDEN:
		case NOT_FOUND:
		case METHOD_NOT_ALLOWED:
		case PAYLOAD_TOO_LARGE:
		case UNSUPPORTED_MEDIA_TYPE:
		case TOO_MANY_REQUESTS:
			System.err.println("Error " + serverResponse);
			throw new ServiceProviderException("Error " + serverResponse);
		case SERVER_UNAVAILABLE:
		case SERVICE_NOT_AVAILABLE:
			System.err.println("Server Error" + serverResponse);
			throw new ServiceProviderException("Server Error " + serverResponse);
		default:
			System.err.println("Unknown Error" + serverResponse);
			throw new ServiceProviderException("Unknown Error " + serverResponse);
		}
	}

	/**
	 * Builds the JSON request string as per the SendGrid specs
	 * 
	 * @param email - Email Object
	 * @return JSON request string
	 */	
	public String buildEmailJSON(Email email) {

		JSONObject allRecipients = new JSONObject();
		
		JSONArray toRecipients = bulidRecipientsJSON(email.getToRecipients());
		allRecipients.put("to", toRecipients);

		if (email.getCcRecipients().size() > 0) {
			JSONArray ccRecipients = bulidRecipientsJSON(email.getCcRecipients());
			allRecipients.put("cc", ccRecipients);
		}

		if (email.getBccRecipients().size() > 0) {						
			JSONArray bccRecipients = bulidRecipientsJSON(email.getBccRecipients());
			allRecipients.put("bcc", bccRecipients);
		}
		
		JSONArray personalizations = new JSONArray();
		personalizations.put(allRecipients);

		JSONObject sender = new JSONObject();
		sender.put("email", email.getSender().getEmail());
		sender.put("name", email.getSender().getName());

		JSONArray content = new JSONArray();
		JSONObject text = new JSONObject();
		text.put("type", "text/plain");
		text.put("value", email.getContent());
		content.put(text);
		
		JSONObject emailJSONObj = new JSONObject();
		emailJSONObj.put("personalizations", personalizations);
		emailJSONObj.put("from", sender);
		emailJSONObj.put("subject", email.getSubject());
		emailJSONObj.put("content", content);
		
		System.out.println("SendGrid JSON Request: " + emailJSONObj.toString());
		
		return emailJSONObj.toString();
		
	}

	/**
	 * Utility function for buildEmailJSON to add recipients.
	 * 
	 * @param recipients - list of recipients from Email Object
	 * @return JSONArray of recipients
	 */	
	private JSONArray bulidRecipientsJSON(ArrayList<EmailUser> recipients) {

		JSONArray recipientJSONArray = new JSONArray();

		Iterator<EmailUser> recipientsIterator = recipients.iterator();

		while (recipientsIterator.hasNext()) {
			EmailUser recipient = recipientsIterator.next();
			JSONObject recipientJSON = new JSONObject();
			recipientJSON.put("email", recipient.getEmail());
			recipientJSON.put("name", recipient.getName());
			recipientJSONArray.put(recipientJSON);
		}

		return recipientJSONArray;

	}
}
