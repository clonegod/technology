package clonegod.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import clonegod.service.SimpleService;
import clonegod.spring.framework.annotation.Autowired;
import clonegod.spring.framework.annotation.Controller;
import clonegod.spring.framework.annotation.RequestMapping;
import clonegod.spring.framework.annotation.RequestParam;
import clonegod.spring.framework.web.servlet.mvc.ModelAndView;

@Controller
public class SimpleController {
	
	@Autowired
	private SimpleService service;
	
	@RequestMapping("/sayHello")
	public ModelAndView sayHello(HttpServletRequest request, HttpServletResponse response, 
									/*String id, */
									@RequestParam("name") String name) {
		
		Map<String, Object> model = new HashMap<>();
		
		String result = service.doService(name);
		model.put("result", result);
		
		return new ModelAndView("hello", model);
	}
	
}
 