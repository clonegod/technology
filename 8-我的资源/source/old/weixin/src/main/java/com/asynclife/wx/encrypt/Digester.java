package com.asynclife.wx.encrypt;

import java.security.MessageDigest;

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
			logger.info("SHA1({})", input);

			MessageDigest md = MessageDigest
					.getInstance(AppConfig.DIGEST_ALGORITHM);
			md.update(input.getBytes());
			byte[] output = md.digest();
			String str = bytesToHex(output);
			logger.info("{}", str);
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

}
