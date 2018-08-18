package com.asynclife.wx.hanlder;

import com.asynclife.wx.model.MsgReq;

public interface Handler {
	
	public String process(MsgReq msgReq);
	
}
