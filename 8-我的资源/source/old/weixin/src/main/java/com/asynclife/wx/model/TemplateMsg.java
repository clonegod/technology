package com.asynclife.wx.model;

import java.util.HashMap;
import java.util.Map;

public class TemplateMsg {
	
	public String touser;
	public String template_id;
	public String url;
	public Map<String, Map<String,String>> data = new HashMap<String, Map<String,String>>();
	
	
}