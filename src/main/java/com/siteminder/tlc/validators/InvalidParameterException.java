package com.siteminder.tlc.validators;

/**
 * Exception class for Invalid Request Parameters to 
 * the service
 * 
 * @author Hassan Mughal
 *
 */
public class InvalidParameterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private int errCode=400; //default 

	public InvalidParameterException(String message, Exception e) {
		super(message, e);
	}		

    public InvalidParameterException(String message) {
        super(message);
    }
	
    public InvalidParameterException(int errCode, String message) {
        super(message);
        this.errCode = errCode;
    }
	
	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	
}
