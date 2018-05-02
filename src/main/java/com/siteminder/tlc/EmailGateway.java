package com.siteminder.tlc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.siteminder.tlc.services.EmailService;
import com.siteminder.tlc.services.MailGunService;
import com.siteminder.tlc.services.SendGridService;
import com.siteminder.tlc.services.ServiceProviderException;

/**
* This class acts as the gateway for sending email via third-party
* email service provider. It first creates a chain of responsiblity between
* the service providers (currently two) and then tries to send the email. If
* the one service provider goes down it failovers to the other service provider.
*
* @author  Hassan Mughal 
*/
public class EmailGateway {

	/**
	 * Routes the email to the available service provider.
	 * 
	 * @param email - the email to be sent.
	 * @throws IOException on failure to retireve a service providers credentials
	 * @throws ServiceProviderException - on service failure by all service providers
	 */
	public static void routeEmail(Email email) throws IOException, ServiceProviderException {
			
			EmailService emailService = getChainOfEmailServices();
			emailService.sendEmail(email);
	}

	/**
	 * Gets service provider credentials from config file and construcs them. It then builds 
	 * a chain of all service providers 
	 * @return the first service provider in the chain
	 * @throws IOException on failure to retireve a service providers credentials
	 */
	private static EmailService getChainOfEmailServices() throws IOException {

		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String servicesConfig = rootPath + "emailservices.xml";
		Properties serviceProps = new Properties();
		serviceProps.loadFromXML(new FileInputStream(servicesConfig));

		EmailService sendGridService = new SendGridService(serviceProps.getProperty("sendGridAPIURL"),
				serviceProps.getProperty("sendGridAPIKey"));

		EmailService mailGunService = new MailGunService(serviceProps.getProperty("MailGunAPIURL"),
				serviceProps.getProperty("MailGunAPIKey"));
		// More EmailServices can be added here

		sendGridService.setNextEmailService(mailGunService);

		return sendGridService;
	}

}
