package com.asynclife.batch.domain;

/**
 * @author Tobias Flohre
 */
public class IlleageAccountrException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IlleageAccountrException() {
		super();
	}

	public IlleageAccountrException(String message, Throwable cause) {
		super(message, cause);
	}

	public IlleageAccountrException(String message) {
		super(message);
	}

	public IlleageAccountrException(Throwable cause) {
		super(cause);
	}

	
}
