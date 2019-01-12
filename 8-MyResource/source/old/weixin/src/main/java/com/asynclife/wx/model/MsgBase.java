package com.asynclife.wx.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.asynclife.wx.util.xml.AdapterXmlCDATA;

@XmlAccessorType(XmlAccessType.FIELD)
public class MsgBase {
	
	@XmlJavaTypeAdapter(AdapterXmlCDATA.class)
	protected String ToUserName;
	
	@XmlJavaTypeAdapter(AdapterXmlCDATA.class)
	protected String FromUserName;
	
	protected String CreateTime;
	
	@XmlJavaTypeAdapter(AdapterXmlCDATA.class)
	protected String MsgType;
	
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	
	
	
}
