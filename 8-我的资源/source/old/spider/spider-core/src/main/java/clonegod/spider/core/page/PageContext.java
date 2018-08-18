package clonegod.spider.core.page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 *	封装所有页面的共享数据
 */
public class PageContext implements Serializable {
	
	private static final long serialVersionUID = 1197017595811667990L;

	// 客户端唯一标识
	private String sessionId;
	
	// 缓存所有与请求相关的参数（图片验证码，短信验证码，静态参数等）
	private Map<String, Object> cachedArgs = new HashMap<>();
	
	private Set<Cookie> webClinetCookies = new HashSet<>();
	
	public PageContext(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Map<String, Object> getCachedArgs() {
		return cachedArgs;
	}

	public void setCachedArgs(Map<String, Object> cachedArgs) {
		this.cachedArgs = cachedArgs;
	}

	public Set<Cookie> getWebClinetCookies() {
		return webClinetCookies;
	}

	public void setWebClinetCookies(Set<Cookie> webClinetCookies) {
		this.webClinetCookies = webClinetCookies;
	}

}
