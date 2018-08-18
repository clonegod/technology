package com.asynclife.wx.service;

import com.asynclife.wx.enums.MsgType;
import com.asynclife.wx.model.MsgReq;
import com.asynclife.wx.model.MsgRsp;
import com.asynclife.wx.util.xml.JaxbHelper;

public class CommonService {
	
	public static String simpleText(MsgReq msgReq, String content) {
		MsgRsp msgRsp = new MsgRsp(msgReq);
		msgRsp.setMsgType(MsgType.text.name());
		msgRsp.setContent(content);
		
		return JaxbHelper.toXml(msgRsp, MsgRsp.class);
	}
	
	public static String simpleArticle(MsgReq msgReq) {
		MsgRsp msgRsp = new MsgRsp(msgReq);
		msgRsp.setMsgType(MsgType.news.name());
		msgRsp.setArticleCount(1);
		msgRsp.addArticle("A4腰PK水桶腰", "闲的慌", 
				"http://pic.yesky.com/uploadImages/2016/076/19/342A5KJ17765.jpg", 
				"http://www.chinapoluo.com");
		
		return JaxbHelper.toXml(msgRsp, MsgRsp.class);
	}
	
	
}
