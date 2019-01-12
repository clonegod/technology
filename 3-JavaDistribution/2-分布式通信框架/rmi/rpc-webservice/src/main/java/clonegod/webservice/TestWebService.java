package clonegod.webservice;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

public class TestWebService {
	static String addr = "http://localhost:8083/myWS/test?wsdl";

	static final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public static void main(String[] args) throws Exception {
		// 发布服务
		Endpoint.publish(addr, new MyServiceImpl());
		
		// 调用服务
		Client client = new Client(100, 8);
		Future<Long> future = executor.submit(client);
		System.out.println(future.get());
		
		// 关闭资源
		executor.awaitTermination(3, TimeUnit.SECONDS);
		executor.shutdownNow();
		System.exit(0);
	}
	
	private static class Client implements Callable<Long> {
		
		private int num1;
		private int num2;
		
		public Client(int num1, int num2) {
			this.num1 = num1;
			this.num2 = num2;
		}


		public Long call() {
			Long sum = null;
			
			try {
				URL wsdlLocation = new URL(addr);
				
				String namespaceURI = "http://webservice.clonegod/"; //WSDL的targetNamespace
				String localPort = "MyServiceImplService"; //WSDL的name
				QName serviceName = new QName(namespaceURI, localPort);
				
				Service service = Service.create(wsdlLocation, serviceName);
				IMyService imyService = service.getPort(IMyService.class);
				
				sum = imyService.add(num1, num2);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return sum;
			
		}
	}
}
