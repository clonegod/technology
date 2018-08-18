package clonegod.spring.framework.web.servlet.mvc;

import java.util.Map;

public class ModelAndView {
	/** View instance or view name String */
	private Object view;

	/** Model Map */
	private Map<String, Object> model;
	
	public ModelAndView() {
		super();
	}

	public ModelAndView(Object view, Map<String, Object> model) {
		super();
		this.view = view;
		this.model = model;
	}

	public Object getView() {
		return view;
	}

	public void setView(Object view) {
		this.view = view;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

}
