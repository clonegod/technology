package com.asynclife.wx.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asynclife.wx.config.AppConfig;
import com.asynclife.wx.exception.InvalidSignatureException;
import com.asynclife.wx.hanlder.Dispatcher;
import com.asynclife.wx.service.SignatureCheckService;

@Controller
public class FrontController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	SignatureCheckService scService;
	
	@Autowired
	Dispatcher dispatcher;
	
	/**
	 * 接口配置信息
	 * 请填写接口配置信息，此信息需要你有自己的服务器资源，填写的URL需要正确响应微信发送的Token验证
	 * @return
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	@ResponseBody
	public String checkSign(HttpServletRequest req) {
		
		String result = null;
		
		try {
			result = scService.checkSign((HttpServletRequest)req);
		} catch (InvalidSignatureException e) {
			result = "error";
		}
		
		return result;
	}
	
	/**
	 * 处理微信转发的所有消息: 文本消息，事件消息，状态报告等
	 * 根据消息类型派发到不同的handler进行处理
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/", method=RequestMethod.POST)
	@ResponseBody
	public String handleMessage(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
		
		List<String> lines = IOUtils.readLines(req.getInputStream(), AppConfig.DEFAULT_ENCODING);
		
		StringBuffer buf = new StringBuffer();
		for(String line : lines) {
			buf.append(line).append("\n");
		}
		
		String reqXml = buf.toString();
		logger.debug(reqXml);
		
		String resXml = dispatcher.dispach(reqXml);
		logger.debug(resXml);
		
		return resXml;
	}
	
}
