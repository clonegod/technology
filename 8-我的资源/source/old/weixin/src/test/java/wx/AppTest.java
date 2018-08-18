package wx;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.asynclife.wx.App;
import com.asynclife.wx.util.HttpClient;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest
public class AppTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	HttpClient httpClient;
	
	/**
	 * 微信服务器主动发起，对开发者账号进行验证
	 */
	@Test
	public void testGet() {
		String url = "http://localhost:80/?timestamp={timestamp}&nonce={nonce}";
		
		Map<String,String> urlVariables = new HashMap<String,String>();
		urlVariables.put("timestamp", ""+ (System.currentTimeMillis() / 1000));
		urlVariables.put("nonce", UUID.randomUUID().toString());
		
		String ret = httpClient.doGet(url, urlVariables);
		logger.info(ret);
	}
	
	
	@Test
	public void testPost() {
		String url = "http://localhost:80/";
		
		Map<String,String> urlVariables = new HashMap<String,String>();
		urlVariables.put("timestamp", ""+ (System.currentTimeMillis() / 1000));
		urlVariables.put("nonce", UUID.randomUUID().toString());
		
		String body = "<xml></xml>";
		
		String ret = httpClient.doPost(url, body, urlVariables);
		logger.info(ret);
		
	}
	
	
}
