package com.asynclife.wx.util;

import java.security.MessageDigest;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import com.asynclife.wx.config.AppConfig;

public class Digester {

	static Logger logger = LoggerFactory.getLogger(Digester.class);
	
	public static String md5(String text) {
		return DigestUtils.md5DigestAsHex(text.getBytes());
	}

	public static String sha1(String input) {
		try {
			MessageDigest md = MessageDigest
					.getInstance(AppConfig.DIGEST_ALGORITHM);
			md.update(input.getBytes());
			byte[] output = md.digest();
			String str = bytesToHex(output);
			return str;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String bytesToHex(byte[] b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		return buf.toString();
	}

	
	public static void main(String[] args) {
		//1. 将token、timestamp、nonce三个参数进行字典序排序
		//2. 将三个参数字符串拼接成一个字符串进行sha1加密
		//3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信

		String signature = "c339296de7bf711851d7eef4b3042d463ea33938";
		
		String[] arr = new String[]{"test123", "1458830296", "1320607073"};
		Arrays.sort(arr);
		
		StringBuilder buf = new StringBuilder();
		for(String str : arr) {
			buf.append(str);
		}
		
		String sha1DeigetStr = Digester.sha1(buf.toString());
		if(sha1DeigetStr.equalsIgnoreCase(signature)) {
			logger.info("signature is valid!");
		} else {
			logger.info("signature is invalid!");
		}
		
	}
}
