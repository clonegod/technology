package clonegod.jwt;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtFilter implements Filter {
	
	Logger logger = LoggerFactory.getLogger(this.getClass()); 

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		logger.info("Request uri: {}", req.getRequestURI());

		final String authHeader = req.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			logger.error("Not Authorization !");
			return;
		} else {
			try {
				final String token = authHeader.substring(7); // The part after "Bearer "
				logger.info("客户端jwt: {}",token);
				DecodedJWT jwt = JwtUtils.verifyToken(token);
				logger.info("decode jwt:{}", jwt);
			} catch (Exception e) { // 包含超时，签名错误等异常
				logger.error("jwt非法或已失效", e);
				return;
			}
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
