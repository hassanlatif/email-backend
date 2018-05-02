package com.siteminder.tlc;

import java.io.IOException;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class parses a JSON Email Object to a Java Email Object
 * 
 * @author Hassan Mughal
 *  
 */
public class EmailParser {

	/**
	 * Parse and maps a JSON Email Object to a Java Email Object
	 * 
	 * @param emailObj - JSON Email Object
	 * @return Email - Java Email Object
	 * @throws JsonMappingException if JSON mapping fails
	 * @throws JsonParseException if JSON parsing fails
	 * @throws IOException if unable to read JSON Object
	 */
	public static Email parseEmail(JSONObject emailObj)
			throws JsonMappingException, JsonParseException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		Email email = mapper.readValue(emailObj.toString(), Email.class);

		return email;		
	}	
}
