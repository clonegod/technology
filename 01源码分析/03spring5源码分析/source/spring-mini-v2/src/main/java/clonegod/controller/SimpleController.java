package clonegod.controller;

import clonegod.service.SimpleService;
import clonegod.spring.framework.annotation.Autowired;
import clonegod.spring.framework.annotation.Controller;
import clonegod.spring.framework.annotation.RequestMapping;
import clonegod.spring.framework.annotation.RequestParam;

@Controller
public class SimpleController {
	
	@Autowired
	private SimpleService service;
	
	@RequestMapping("/sayHello")
	public String sayHello(@RequestParam String name) {
		return service.doService(name);
	}
	
}
