package com.siteminder.tlc;

import java.util.ArrayList;

/**
* This class encapsulates a basic Email Object. 
*
* @author  Hassan Mughal 
*/
public class Email {

	private EmailUser sender;
	private ArrayList<EmailUser> toRecipients;
	private ArrayList<EmailUser> ccRecipients;
	private ArrayList<EmailUser> bccRecipients;
	private String subject;
	private String content;
	
	public Email() {

		toRecipients = new ArrayList<EmailUser>();
		ccRecipients = new ArrayList<EmailUser>();		
		bccRecipients = new ArrayList<EmailUser>();
	}

	public EmailUser getSender() {
		return sender;
	}

	public void setSender(EmailUser sender) {
		this.sender = sender;
	}

	public ArrayList<EmailUser> getToRecipients() {
		return toRecipients;
	}

	public void setToRecipients(ArrayList<EmailUser> toRecipients) {
		this.toRecipients = toRecipients;
	}

	public ArrayList<EmailUser> getCcRecipients() {
		return ccRecipients;
	}

	public void setCcRecipients(ArrayList<EmailUser> ccRecipients) {
		this.ccRecipients = ccRecipients;
	}

	public ArrayList<EmailUser> getBccRecipients() {
		return bccRecipients;
	}

	public void setBccRecipients(ArrayList<EmailUser> bccRecipients) {
		this.bccRecipients = bccRecipients;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
		
	
}
