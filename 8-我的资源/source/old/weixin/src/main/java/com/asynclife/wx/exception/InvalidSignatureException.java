package com.asynclife.wx.exception;

public class InvalidSignatureException extends RuntimeException {

	private static final long serialVersionUID = 8250837078633860500L;

	public InvalidSignatureException() {
		super();
	}

	public InvalidSignatureException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidSignatureException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSignatureException(String message) {
		super(message);
	}

	public InvalidSignatureException(Throwable cause) {
		super(cause);
	}

	
	
}
