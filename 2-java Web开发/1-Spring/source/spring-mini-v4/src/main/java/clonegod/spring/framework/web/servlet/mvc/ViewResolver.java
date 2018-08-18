package clonegod.spring.framework.web.servlet.mvc;

import java.nio.file.Path;

public class ViewResolver {
	
	private String viewName;
	
	private Path viewPath;
	
	private String suffix;

	public ViewResolver(String viewName, Path viewPath, String suffix) {
		super();
		this.viewName = viewName;
		this.viewPath = viewPath;
		this.suffix = suffix;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Path getViewPath() {
		return viewPath;
	}

	public void setViewPath(Path viewPath) {
		this.viewPath = viewPath;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
}
