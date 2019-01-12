
package clonegod.spider.core.exception;

public class SpiderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1440498036817429769L;

	public SpiderException(String message, Throwable cause) {
		super(message, cause);
	}

	public SpiderException(String message) {
		super(message);
	}

	public SpiderException(Throwable cause) {
		super(cause);
	}
	
}
