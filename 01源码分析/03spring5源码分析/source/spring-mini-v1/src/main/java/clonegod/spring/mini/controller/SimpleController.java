package clonegod.spring.mini.controller;

import clonegod.spring.mini.annotation.Autowired;
import clonegod.spring.mini.annotation.Controller;
import clonegod.spring.mini.annotation.RequestMapping;
import clonegod.spring.mini.annotation.RequestParam;
import clonegod.spring.mini.service.SimpleService;

@Controller
public class SimpleController {
	
	@Autowired
	private SimpleService service;
	
	@RequestMapping("/sayHello")
	public String sayHello(@RequestParam String name) {
		return service.doService(name);
	}
	
}
