package com.asynclife.wx.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.asynclife.wx.service.AccessTokenService;

@Component
public class HttpClient {

	@Autowired
	AccessTokenService accessTokenService;

	@Autowired
	RestTemplate restTemplate;
	
	
	public String doGet(String url) {
		return doGet(url, null);
	}
	
	public String doGet(String url, Map<String, String> urlVariables) {
		return restTemplate.getForObject(url, String.class, urlVariablesAll(urlVariables));
	}
	
	public String doPost(String url, String body) {
		return doPost(url, body, null);
	}
	
	public String doPost(String url, String body, Map<String, String> urlVariables) {
		return restTemplate.postForObject(url, body, String.class, urlVariablesAll(urlVariables));
	}
	
	public String doPost(String url, MultiValueMap<String, Object> body) {
		return doPost(url, body, null);
	}
	
	public String doPost(String url, MultiValueMap<String, Object> body, Map<String, String> urlVariables) {
		return restTemplate.postForObject(url, body, String.class, urlVariablesAll(urlVariables));
	}
	
	/**
	 * 自动绑定参数值到AccessToken
	 * @param urlVariables
	 * @return
	 */
	public  Map<String, String> urlVariablesAll(Map<String, String> urlVariables) {
		Map<String, String> urlVariableMap = new ConcurrentHashMap<String, String>();
		if(urlVariables != null) {
			urlVariableMap.putAll(urlVariables);
		}
		urlVariableMap.put("ACCESS_TOKEN", accessTokenService.getAccessToken());
		return urlVariableMap;
	}

	
	
	
}
