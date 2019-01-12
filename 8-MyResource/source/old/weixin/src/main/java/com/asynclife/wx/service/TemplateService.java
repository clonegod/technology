package com.asynclife.wx.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.asynclife.wx.model.TemplateMsg;
import com.asynclife.wx.util.HttpClient;
import com.asynclife.wx.util.JsonUtils;

@Service
public class TemplateService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	HttpClient httpClient;
	
	@Value("${POST_SEND_TEMPLATE_MSG}")
	String POST_SEND_TEMPLATE_MSG;
	
	public void sendTemplateMsg(TemplateMsg templateMsg) {
		
		String body = JsonUtils.toJson(templateMsg);
		
		logger.debug("send template msg:{}", body);
		
		String result = httpClient.doPost(POST_SEND_TEMPLATE_MSG, body);
		
		logger.debug("send templaet msg result:{}", result);
		
	}
	
	public static Map<String,String> createMap(String value, String color) {
		Map<String,String> map = new HashMap<String, String>();
		map.put("value", value);
		map.put("color", color);
		return map;
	}
	
}
