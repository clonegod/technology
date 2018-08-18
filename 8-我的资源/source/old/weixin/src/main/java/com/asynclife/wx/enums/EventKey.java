package com.asynclife.wx.enums;

public enum EventKey {
	
	V1001_TODAY_HOUSE("今日房源"),
	V2001_BIZ_INTRO("关于我们/业务介绍"),
	V2001_BIZ_COOP("关于我们/商务合作");
	
	private String keyName;

	private EventKey(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyName() {
		return keyName;
	}
	
}
