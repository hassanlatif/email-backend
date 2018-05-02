package com.siteminder.tlc.resource;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.siteminder.tlc.Email;
import com.siteminder.tlc.EmailGateway;
import com.siteminder.tlc.EmailParser;
import com.siteminder.tlc.services.ServiceProviderException;
import com.siteminder.tlc.validators.EmailValidator;
import com.siteminder.tlc.validators.InvalidParameterException;

/**
 * The entry end-point to the email backend service
 * 
 * @author Hassan Mughal
 *
 */
@Path("/email")
public class EndPointResource {

	/**
	 * RESTful method to send email - this maps to the AWS Lambda function 
	 * @param request
	 * @return
	 */
	@Path("/send")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendEmail(String request) {

		JSONObject jsonObjectRQ = new JSONObject(request);
		JSONObject jsonObjectRS = new JSONObject();

		try {

			Email email = EmailParser.parseEmail(jsonObjectRQ);
			EmailValidator.validateEmail(email);
			EmailGateway.routeEmail(email);

			jsonObjectRS.put("code", "200");
			jsonObjectRS.put("message", "OK");
			jsonObjectRS.put("details", "Email send request was successful.");

		} catch (InvalidParameterException e) {
			jsonObjectRS.put("code", "400");
			jsonObjectRS.put("message", "Bad Request");
			jsonObjectRS.put("details", e.getMessage());
		} catch (JsonParseException | JsonMappingException e) {
			jsonObjectRS.put("code", "400");
			jsonObjectRS.put("message", "Bad Request");
			jsonObjectRS.put("details",
					"Invalid Request format, please check that required fields are provided and the JSON format is valid.");
		} catch (ServiceProviderException | IOException e) {
			jsonObjectRS.put("code", "500");
			jsonObjectRS.put("message", "Internal Server Error");
			jsonObjectRS.put("details", "Something went wrong on our side, please try again or contact support.");
		}
		String result = jsonObjectRS.toString();
		
		return Response.status(jsonObjectRS.getInt("code")).entity(result).build();
	}

}