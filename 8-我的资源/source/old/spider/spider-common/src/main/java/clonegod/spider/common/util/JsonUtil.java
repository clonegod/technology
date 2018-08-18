package clonegod.spider.common.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	
	private static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

	/**
	 * Gson instances are Thread-safe so you can reuse them freely across multiple threads. 
	 * 
	 * @param src
	 * @return
	 */
	public static String toJson(Object src) {
		return gson.toJson(src);
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}

	public static <T> T fromJson(String json, TypeToken<T> typeToken) {
		return gson.fromJson(json, typeToken.getType());
	}
}
