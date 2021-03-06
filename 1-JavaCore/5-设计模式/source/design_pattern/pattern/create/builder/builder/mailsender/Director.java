package builder.mailsender;

import builder.mail.Builder;
import builder.mail.Mail;

public class Director {
	
	private Builder builder;
	
	public Director(Builder builder) {
		this.builder = builder;
	}
	
	public Mail construct(String fromAddress, String toAddress) {
		builder.buildeFrom(fromAddress);
		builder.buildeTo(toAddress);
		builder.buildeSubject();
		builder.buildeBody();
		builder.buildeSendDate();
		return builder.retrieveMail();
	}
}
