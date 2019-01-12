package com.asynclife.wx.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.asynclife.wx.config.AppConfig;
import com.asynclife.wx.util.HttpClient;

@Service
public class MenuService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	HttpClient httpClient;
	
	@Value("${POST_CREATE_MENU}")
	String POST_CREATE_MENU;
	
	public void createMenu() {
		
		String menuJsonStr = null;
		
		InputStream ins = null;
		try {
			ins = MenuService.class.getClassLoader().getResourceAsStream("menu.dataout.config");
			
			List<String> lines = IOUtils.readLines(ins, AppConfig.DEFAULT_ENCODING);
			
			StringBuilder buf = new StringBuilder();
			for(String line : lines) {
				buf.append(line).append("\n");
			}
			
			menuJsonStr = buf.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(ins);
		}
		logger.debug("create menu: json str={}", menuJsonStr);
		
		String ret = httpClient.doPost(POST_CREATE_MENU, menuJsonStr);
		
		logger.debug("create menu: return={}", ret);
		
	}
	
}
