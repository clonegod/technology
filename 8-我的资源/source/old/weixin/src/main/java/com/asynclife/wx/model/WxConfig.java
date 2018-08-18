package com.asynclife.wx.model;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asynclife.wx.util.Digester;

public class WxConfig {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String appId;
	private String noncestr;
	private String jsapi_ticket;
	private String timestamp;
	private String url;
	private String signature;
	
	public WxConfig() {
	}

	public WxConfig(String appId, String jsapi_ticket, String url) {
		this.appId = appId; 
		this.noncestr = UUID.randomUUID().toString().replace("-", "");
		this.jsapi_ticket = jsapi_ticket;
		this.timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		this.url = url;
	}
	
	public void signature() {
		StringBuilder buf = new StringBuilder();
		buf.append("jsapi_ticket=").append(jsapi_ticket).append("&");
		buf.append("noncestr=").append(noncestr).append("&");
		buf.append("timestamp=").append(timestamp).append("&");
		buf.append("url=").append(url);
		
		logger.info("string1={}", buf.toString());
		
		String sha1DeigetStr = Digester.sha1(buf.toString()).toLowerCase();
		
		this.signature = sha1DeigetStr;
		
		logger.info("{}", this);
	}
	
	@Override
	public String toString() {
		return "WxConfig [appId=" + appId + ", noncestr=" + noncestr + ", jsapi_ticket=" + jsapi_ticket + ", timestamp="
				+ timestamp + ", url=" + url + ", signature=" + signature + "]";
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
}
