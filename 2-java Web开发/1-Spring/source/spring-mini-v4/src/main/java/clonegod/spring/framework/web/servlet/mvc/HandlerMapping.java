package clonegod.spring.framework.web.servlet.mvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class HandlerMapping {
	
	private Pattern urlPattern;
	
	private Object controllerInstance;
	
	private Method handler;

	public HandlerMapping(Pattern urlPattern, Object controllerInstance, Method handler) {
		super();
		this.urlPattern = urlPattern;
		this.controllerInstance = controllerInstance;
		this.handler = handler;
	}

	public Pattern getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(Pattern urlPattern) {
		this.urlPattern = urlPattern;
	}

	public Object getControllerInstance() {
		return controllerInstance;
	}

	public void setControllerInstance(Object controllerInstance) {
		this.controllerInstance = controllerInstance;
	}

	public Method getHandler() {
		return handler;
	}

	public void setHandler(Method handler) {
		this.handler = handler;
	}
	
}
