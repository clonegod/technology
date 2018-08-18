package com.asynclife.wx.hanlder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asynclife.wx.enums.MsgType;
import com.asynclife.wx.model.MsgReq;
import com.asynclife.wx.service.MessageService;

@Service
public class MessageHandler implements Handler {

	@Autowired
	MessageService messageService;
	
	@Override
	public String process(MsgReq msgReq) {
		
		switch(MsgType.valueOf(msgReq.getMsgType())) {
		case text:
			return messageService.onReceiveText(msgReq);
		case image:
			return messageService.onReceiveImage(msgReq);
		case voice:
			return messageService.onReceiveVoice(msgReq);
		case video:
			return messageService.onReceiveVideo(msgReq);
		case shortvideo:
			return messageService.onReceiveShortVideo(msgReq);
		case location:
			return messageService.onReceiveLocation(msgReq);
		case link:
			return messageService.onReceiveLink(msgReq);
		default:
			return null;
		}
	}

}
