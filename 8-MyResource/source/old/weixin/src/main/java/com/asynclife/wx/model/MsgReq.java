package com.asynclife.wx.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class MsgReq extends MsgBase {
	
	private String Content;
	
	private String MsgId;
	private String MsgID;
	
	private String PicUrl;
	
	private String MediaId;

	private String Format;
	
	private String Recognition;
	
	private String ThumbMediaId;
	
	private String Location_X;
	
	private String Location_Y;
	
	private String Scale;
	
	private String Label;
	
	private String Title;
	
	private String Description;
	
	private String Url;
	
	private String Event;
	
	private String EventKey;
	
	private String Ticket;
	
	private String Latitude;
	
	private String Longitude;
	
	private String Precision;
	
	private String Status;

	public String getContent() {
		return Content;
	}

	public String getMsgId() {
		return MsgId;
	}

	public String getMsgID() {
		return MsgID;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public String getMediaId() {
		return MediaId;
	}

	public String getFormat() {
		return Format;
	}

	public String getRecognition() {
		return Recognition;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public String getLocation_X() {
		return Location_X;
	}

	public String getLocation_Y() {
		return Location_Y;
	}

	public String getScale() {
		return Scale;
	}

	public String getLabel() {
		return Label;
	}

	public String getTitle() {
		return Title;
	}

	public String getDescription() {
		return Description;
	}

	public String getUrl() {
		return Url;
	}

	public String getEvent() {
		return Event;
	}

	public String getEventKey() {
		return EventKey;
	}

	public String getTicket() {
		return Ticket;
	}

	public String getLatitude() {
		return Latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public String getPrecision() {
		return Precision;
	}

	public String getStatus() {
		return Status;
	}

}
