package clonegod.webservice;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * 最简单的方式-直接发布，没有接口
 *
 */
public class TestPublishWebService {
	
	public static void main(String[] args) {
		Endpoint.publish("http://localhost:8080/myWS/sayHello", new MyService());
	}
	
	@WebService
	private static class MyService {
		@SuppressWarnings("unused")
		public String sayHello(String name) {
			return "Hello "+name;
		}
	}
}
