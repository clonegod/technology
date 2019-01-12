package builder.mail;

import java.util.Date;

import util.SimpleUtil;

public class Mail {
	
	public String subject;
	public String body;
	public String from;
	public String to;
	public Date sendDate;
	
	@Override
	public String toString() {
		return "Mail [subject=" + subject + ", body=" + body + ", from=" + from
				+ ", to=" + to + ", sendDate=" + SimpleUtil.parseCurrentDateTime(sendDate) + "]";
	}
	
	
	
}
