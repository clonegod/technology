package com.asynclife.wx.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
	
	// Thread Safe
	public static final Gson GSON = 
			new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	
	
	public static <T> T fromJson(String jsonStr, Class<T> cls) {
		return GSON.fromJson(jsonStr, cls);
	}
	
	public static String toJson(Object src) {
		return GSON.toJson(src);
	}
	
	
}
