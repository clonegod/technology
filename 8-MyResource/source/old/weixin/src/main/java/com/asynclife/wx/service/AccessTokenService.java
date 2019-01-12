package com.asynclife.wx.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.asynclife.wx.config.AppConfig;
import com.asynclife.wx.model.AccessToken;
import com.google.gson.Gson;

@Service
@PropertySource("classpath:api.properties")
public class AccessTokenService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	RedisTemplate<String,String> redisTemplate;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${GET_FETCH_ACCESS_TOKEN}")
	String accessTokenURL;
	
	@Value("${appID}")
	String APPID;
	
	@Value("${appSecret}")
	String APPSECRET;
	
	Gson gson = new Gson();
	
	/**
	 * 获取access_token
	 * 每日调用上限/次: 	2000
	 * @return
	 */
	public String getAccessToken() {
		String token = redisTemplate.opsForValue().get(AppConfig.KEY_WX_ACCESS_TOKEN);
		if(token == null || true) {
			AccessToken newToken = refreshAccessTokenFromWXServer();
			token = newToken.getAccess_token();
			// expires_in 	凭证有效时间，单位：秒
			redisTemplate.opsForValue().set(AppConfig.KEY_WX_ACCESS_TOKEN, token, 
					newToken.getExpires_in() - 20, TimeUnit.SECONDS);
			logger.info("refresh token success");
		}
		return token;
	}
	
	private AccessToken refreshAccessTokenFromWXServer() {
		String retStr = restTemplate.getForObject(accessTokenURL, String.class, APPID, APPSECRET);
		logger.info("wx server return:{}", retStr);
		AccessToken wx = gson.fromJson(retStr, AccessToken.class);
		if(wx.getErrcode() != null) {
			logger.error(retStr);
			throw new RuntimeException(retStr);
		}
		logger.info("access token={}", wx.getAccess_token());
		return wx;
	}
	
	
}
