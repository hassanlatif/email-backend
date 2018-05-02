package com.siteminder.tlc.services;

/**
 * Exception class for exception from service providers.
 * 
 * @author Hassan Mughal
 *
 */
public class ServiceProviderException extends Exception {

	private static final long serialVersionUID = 1L;
	private int errCode=500; //default

	public ServiceProviderException(String message, Exception e) {
		super(message, e);
	}		

    public ServiceProviderException(String message) {
        super(message);
    }
	
    public ServiceProviderException(int errCode, String message) {
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
