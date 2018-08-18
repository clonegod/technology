package com.asynclife.wx.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asynclife.wx.enums.EventKey;
import com.asynclife.wx.model.MsgReq;

/**
 * 接收事件推送
 *
 */
@Service
public class EventService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 订阅后自动回复
	 * @param msgReq
	 * @return
	 */
	public String onSubscribe(MsgReq msgReq) {
		logger.debug("Event-SUBSCRIBE, user={}", msgReq.getFromUserName());
		int count = new Random().nextInt(1000) + 1;
		return CommonService.simpleText(msgReq, "欢迎关注叵罗品质，您是第"+count+"位客户！我们将为您提供最优质的商品与服务！");
	}

	public String onUnsubscribe(MsgReq msgReq) {
		logger.debug("Event-UNSUBSCRIBE, user={}", msgReq.getFromUserName());
		return null;
	}

	public String onScan(MsgReq msgReq) {
		if(msgReq.getEventKey().startsWith("qrscene_")) {
			logger.debug("Event-SCAN, 未关注扫码, user={}, eventKey={}, Ticket={}", 
					msgReq.getFromUserName(), msgReq.getEventKey(), msgReq.getTicket());
		} else {
			logger.debug("Event-SCAN, 已关注扫码, user={}, eventKey={}, Ticket={}", 
					msgReq.getFromUserName(), msgReq.getEventKey(), msgReq.getTicket());
		}
		
		return null;
	}

	public String onLocation(MsgReq msgReq) {
		logger.debug("Event-LOCATION, user={}, Latitude={}, Longitude={}, Precision={}", 
				msgReq.getFromUserName(), msgReq.getLatitude(), msgReq.getLongitude(), msgReq.getPrecision());
		
		return CommonService.simpleText(msgReq, "Latitude="+msgReq.getLatitude()+"Longitude="+msgReq.getLongitude());
	}

	public String onClick(MsgReq msgReq) {
		EventKey evtKey = EventKey.valueOf(msgReq.getEventKey());
		String keyName = evtKey.getKeyName();
		
		logger.debug("Event-CLICK! user={}, keyName={}", msgReq.getFromUserName(), keyName);
		
		return CommonService.simpleText(msgReq, "你点击了"+keyName);
	}

	public String onView(MsgReq msgReq) {
		String url = msgReq.getEventKey();
		logger.debug("Event-VIEW! user={}, url={}", msgReq.getFromUserName(), url);
		
		return null;
	}

	public String onTemplateSendJobFinish(MsgReq msgReq) {
		
		logger.debug("Event-TEMPLATESENDJOBFINISH! user={}, msgID={}, status={}",
				msgReq.getFromUserName(), msgReq.getMsgID(), msgReq.getStatus());
		
		return null;
	}
	
	
}
