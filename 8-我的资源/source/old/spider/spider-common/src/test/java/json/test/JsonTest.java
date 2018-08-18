package json.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.reflect.TypeToken;

import clonegod.spider.common.util.JsonUtil;

public class JsonTest {
	
	@Test
	public void testCreateAccountJsonFile() {
		
		Map<String, Map<String,Map<String,String>>> accountMap = new HashMap<>();
		
		Map<String,String> loginType1 = new HashMap<>();
		loginType1.put("loginId", "3912784832748932");
		loginType1.put("password", "888668");
		loginType1.put("loginType", "5");
		
		Map<String,Map<String,String>> beijing = new HashMap<>();
		beijing.put("联名卡号", loginType1);
		
		accountMap.put("beijing", beijing);
	
		String jsonStr = JsonUtil.toJson(accountMap);
		System.out.println(jsonStr);
		
		@SuppressWarnings("serial")
		Object object = JsonUtil.fromJson(jsonStr, new TypeToken<Map<String, Map<String,Map<String,String>>>>() {});
		System.out.println(object);
	}
	
}
