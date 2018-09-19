package clonegod.jwt;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class JwtApplicationTests {

//	@Value("${local.server.port}")
	@LocalServerPort
	int port;
	
	@Test
	public void contextLoads() {
	}

	RestTemplate restTemplate = new RestTemplate();
	
	@Test
	public void testJWT() throws Exception {
		ResponseEntity<String> loginRes = 
				restTemplate.getForEntity("http://localhost:{port}/login", String.class, port);
		String token = loginRes.getHeaders().getFirst("jwt");
		System.out.println(token);
		
		TimeUnit.SECONDS.sleep(4);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> apiDataRes = 
				restTemplate.exchange("http://localhost:{port}/api/data", HttpMethod.GET, requestEntity, String.class, port);
		String data = apiDataRes.getBody();
		System.out.println(data);
	}
	
}
