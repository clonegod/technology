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

public class MyFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rsp,
			FilterChain chain) throws IOException, ServletException {
		
		printRequestParams((HttpServletRequest)req);
		
		chain.doFilter(req, rsp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
	static Logger logger = LoggerFactory.getLogger(MyFilter.class);
	
	public static void printRequestParams(HttpServletRequest req) {
		logger.info("==============start=============");
		logger.info("remoteUser={}, remoteAddr={}, remoteHost={}, remotePort={}",
				req.getRemoteUser(), req.getRemoteAddr(), req.getRemoteHost(), req.getRemotePort());
		for(String key : req.getParameterMap().keySet()) {
			String[] values = req.getParameterMap().get(key);
			logger.info(key+"="+Arrays.toString(values));
		}
		logger.info("==============end=============");
	}

}
