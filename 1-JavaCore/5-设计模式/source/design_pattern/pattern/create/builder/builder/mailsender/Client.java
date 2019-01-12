package builder.mailsender;

import builder.mail.Builder;
import builder.mail.GoodbyeBuilder;
import builder.mail.Mail;
import builder.mail.WelcomeBuilder;
import util.SimpleUtil;

public class Client {
	
	public static void main(String[] args) throws Exception {
		Builder builder = new WelcomeBuilder();
		Director director = new Director(builder);
		Mail welcome = director.construct("aaa@123.com", "bbb@123.com");

		Builder builder2 = new GoodbyeBuilder();
		Director director2 = new Director(builder2);
		Mail goodbye = director2.construct("aaa@123.com", "ccc@123.com");
		
		MailSender.getInstance().send(welcome, goodbye);
		
		System.out.println(SimpleUtil.parseCurrentDateTime());
		
	}

}
