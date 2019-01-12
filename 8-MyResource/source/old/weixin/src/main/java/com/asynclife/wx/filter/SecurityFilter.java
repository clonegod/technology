package com.asynclife.wx.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.asynclife.wx.service.SignatureCheckService;

public class SecurityFilter implements Filter, ApplicationContextAware{
	
	Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
	
	SignatureCheckService scService;

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rsp,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpReq = (HttpServletRequest)req;
		
		if(httpReq.getParameterMap().isEmpty()) {
			logger.error("No sinature in request, parameter map is empty!");
			
			//TODO 有时会发生request中没有微信传递的签名参数问题
			//return;
		} else {
			printRequestParams(httpReq);
			// scService.checkSign(httpReq);
		}
		
		chain.doFilter(req, rsp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
	public void printRequestParams(HttpServletRequest req) {
		for(String key : req.getParameterMap().keySet()) {
			String[] values = req.getParameterMap().get(key);
			logger.debug("req paramter: key={}, vlaue={}", key, Arrays.toString(values));
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		scService = applicationContext.getBean(SignatureCheckService.class);
	}

}
