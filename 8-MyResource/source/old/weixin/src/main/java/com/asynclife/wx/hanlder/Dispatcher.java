package com.asynclife.wx.hanlder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asynclife.wx.enums.MsgType;
import com.asynclife.wx.model.MsgReq;
import com.asynclife.wx.util.xml.JaxbHelper;

@Component
public class Dispatcher {
	
	@Autowired
	MessageHandler messageHandler;
	
	@Autowired
	EventHandler eventHandler;
	
	public String dispach(String reqXml) {
		
		MsgReq msgReq = JaxbHelper.xml2Bean(reqXml, MsgReq.class);
		
		Handler handler = ( MsgType.valueOf(msgReq.getMsgType()) == MsgType.event ) 
				? eventHandler : messageHandler;
		
		return handler.process(msgReq);
		
	}
	
}
