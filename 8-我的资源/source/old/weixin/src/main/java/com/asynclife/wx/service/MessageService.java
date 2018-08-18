package com.asynclife.wx.service;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asynclife.wx.enums.MsgType;
import com.asynclife.wx.model.MsgReq;
import com.asynclife.wx.model.MsgRsp;
import com.asynclife.wx.util.HttpClient;
import com.asynclife.wx.util.xml.JaxbHelper;

/**
 * 接收普通消息
 *
 */
@Service
public class MessageService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	HttpClient httpClient;
	
	public String onReceiveText(MsgReq msgReq) {
		String str = MessageFormat.format("MESSAGE-text, user={0}, Content={1}",
				msgReq.getFromUserName(), msgReq.getContent());
		
		logger.info(str);
		
		if(str.contains("A4")) {
			return CommonService.simpleArticle(msgReq);
		}
		
		return CommonService.simpleText(msgReq, str);
	}

	public String onReceiveImage(MsgReq msgReq) {
		String str = MessageFormat.format("MESSAGE-image, user={0}, MediaId={1}, PicUrl={2}",
				msgReq.getFromUserName(), msgReq.getMediaId(), msgReq.getPicUrl());
		
		logger.info(str);
		
		return CommonService.simpleText(msgReq, str);
	}

	public String onReceiveVoice(MsgReq msgReq) {
		String str = MessageFormat.format("MESSAGE-voice, user={0}, MediaId={1}, Format={2}",
				msgReq.getFromUserName(), msgReq.getMediaId(), msgReq.getFormat());
		
		logger.info(str);
		
		return CommonService.simpleText(msgReq, str);
	}

	public String onReceiveVideo(MsgReq msgReq) {
		String str = MessageFormat.format("MESSAGE-video, user={0}, MediaId={1}, ThumbMediaId={2}",
				msgReq.getFromUserName(), msgReq.getMediaId(), msgReq.getThumbMediaId());
		
		logger.info(str);
		
		return CommonService.simpleText(msgReq, str);
	}

	public String onReceiveShortVideo(MsgReq msgReq) {
		String str = MessageFormat.format("MESSAGE-shortvideo, user={0}, MediaId={1}, ThumbMediaId={2}",
				msgReq.getFromUserName(), msgReq.getMediaId(), msgReq.getThumbMediaId());
		
		logger.info(str);
		
		return CommonService.simpleText(msgReq, str);
	}

	public String onReceiveLocation(MsgReq msgReq) {
		
		String str = MessageFormat.format("MESSAGE-location, user={0}, Location_X={1}, Location_Y={2}, Scale={3}, Label={4}",
				msgReq.getFromUserName(), msgReq.getLocation_X(), msgReq.getLocation_Y(), 
				msgReq.getScale(), msgReq.getLabel());
		
		logger.info(str);
		
		return CommonService.simpleText(msgReq, str);
	}

	public String onReceiveLink(MsgReq msgReq) {
		
		String str = MessageFormat.format("MESSAGE-link, user={0}, title={1}, desc={2}, url={3}",
				msgReq.getFromUserName(), msgReq.getTitle(), msgReq.getDescription(), msgReq.getUrl());
		
		logger.info(str);
		
		return CommonService.simpleText(msgReq, str);
	}
	
	
	public String returnPic(MsgReq msgReq) {
		
		MsgRsp msgRsp = new MsgRsp(msgReq);
		msgRsp.setMsgType(MsgType.image.name());
		msgRsp.setImage("WxQXlLknD-eg6oYhCRJAm0LZ-LTxYtIU-BLQQSY3LLDABeoFAMXU6TU42tKn7UNy");
		
		return JaxbHelper.toXml(msgRsp, MsgRsp.class);
	}
}
