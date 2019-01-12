package com.asynclife.wx.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.asynclife.wx.model.WxConfig;
import com.asynclife.wx.service.JSAPITicketService;

@Controller
@RequestMapping("/wx")
public class WxJsController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	JSAPITicketService jsApiTicketService;

	@Value("${appID}")
	String appId;
	
	
	@RequestMapping("/index")
	public String index(Model model) {
		
		String ticket = jsApiTicketService.getJsApiTicket();
		
		model.addAttribute("jsTicket", ticket);
		
		return "index";
	}
	
	@RequestMapping(value="/sign", produces="application/json")
	@ResponseBody
	public Object createSign(String url) {
		
		String ticket = jsApiTicketService.getJsApiTicket();
		
		WxConfig wxConfig = new WxConfig(appId, ticket, url);
		
		wxConfig.signature();
		
		return wxConfig;
	}
	
	/**
	 * 根据参数控制是否显示页面中某部分
	 * @param shared
	 * @param mav
	 * @return
	 */
	@RequestMapping("/share")
	public ModelAndView share(@RequestParam(name="shared", required=false) String shared, ModelAndView mav) {
		
		logger.info("shared={}", shared);
		
		mav.setViewName("share");
		mav.addObject("isShared", !StringUtils.isEmpty(shared));
		
		return mav;
	}
	
}
