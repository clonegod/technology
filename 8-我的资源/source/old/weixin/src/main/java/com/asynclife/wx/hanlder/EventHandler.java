package com.asynclife.wx.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asynclife.wx.enums.EventType;
import com.asynclife.wx.model.MsgReq;
import com.asynclife.wx.service.EventService;

@Service
public class EventHandler implements Handler {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EventService eventService;
	
	@Override
	public String process(MsgReq msgReq) {
		
		switch(EventType.valueOf(msgReq.getEvent())) {
			case subscribe: 
				return eventService.onSubscribe(msgReq);
			case unsubscribe:
				return eventService.onUnsubscribe(msgReq);
			case SCAN:
				return eventService.onScan(msgReq);
			case LOCATION:
				return eventService.onLocation(msgReq);
			case CLICK:
				return eventService.onClick(msgReq);
			case VIEW:
				return eventService.onView(msgReq);
			case TEMPLATESENDJOBFINISH:
				return eventService.onTemplateSendJobFinish(msgReq);
			default:
				logger.warn("unknown event type:{}", msgReq.getEvent());
		}
		
		return null;
	}


}
