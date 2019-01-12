package com.asynclife.wx.dispatcher;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.asynclife.wx.hanlder.Handler;

//@Component
public class Dispatcher {
	
	@Autowired
	ApplicationContext applicationContext;
	
	public String dispach(String reqXml) {
		
		Map<String, Handler> handlers = applicationContext.getBeansOfType(Handler.class);
		
		for(String key : handlers.keySet()) {
			System.out.println(key+"="+handlers.get(key).getClass().getName());
		}
		
		return null;
	}
	
}
