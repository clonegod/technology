package com.asynclife.wx.service;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.asynclife.wx.exception.InvalidSignatureException;
import com.asynclife.wx.util.Digester;

@Service
@PropertySource("classpath:api.properties")
public class SignatureCheckService {
	
	@Value("${SERVER_TOKEN}")
	String serverToken;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 1. 将token、timestamp、nonce三个参数进行字典序排序
	 * 2. 将三个参数字符串拼接成一个字符串进行sha1加密
	 * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 * 
	 * @return	验证合法，则原样返回echostr
	 */
	public String checkSign(HttpServletRequest req) {
		
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		String[] arr = new String[]{serverToken, timestamp, nonce};
		Arrays.sort(arr);
		
		StringBuilder buf = new StringBuilder();
		for(String str : arr) {
			buf.append(str);
		}
		
		String sha1DeigetStr = Digester.sha1(buf.toString());
		
		if(sha1DeigetStr.equalsIgnoreCase(signature)) {
			logger.debug("signature is valid!");
			return echostr;
		}
		
		throw new InvalidSignatureException("signature is invalid!");
	}
	
}
