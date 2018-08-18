package com.asynclife.wx.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.asynclife.wx.config.AppConfig;
import com.asynclife.wx.model.JsApiTicket;
import com.asynclife.wx.util.HttpClient;
import com.google.gson.Gson;

@Service
public class JSAPITicketService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	RedisTemplate<String,String> redisTemplate;
	
	@Autowired
	HttpClient httpClient;
	
	@Value("${GET_FETCH_JSAPI_TICKET}")
	String jsApiTicketURL;
	
	Gson gson = new Gson();
	
	public String getJsApiTicket() {
		String ticket = redisTemplate.opsForValue().get(AppConfig.KEY_WX_JSAPI_TICKET);
		if(ticket == null) {
			JsApiTicket jsApiticket = refreshJsApiTicketFromWXServer();
			ticket = jsApiticket.getTicket();
			// expires_in 	凭证有效时间，单位：秒
			redisTemplate.opsForValue().set(AppConfig.KEY_WX_JSAPI_TICKET, ticket, 
					jsApiticket.getExpires_in() - 20, TimeUnit.SECONDS);
			logger.info("refresh js api ticket success");
		} else {
			logger.info("use cached js api ticket");
		}
		return ticket;
	}
	
	private JsApiTicket refreshJsApiTicketFromWXServer() {
		String retStr = httpClient.doGet(jsApiTicketURL);
		logger.info("wx server return:{}", retStr);
		JsApiTicket ticket = gson.fromJson(retStr, JsApiTicket.class);
		if(! "ok".equalsIgnoreCase(ticket.getErrmsg())) {
			logger.error(retStr);
			throw new RuntimeException(retStr);
		}
		logger.info("js api ticket={}", ticket.getTicket());
		return ticket;
	}
	
}
