package clonegod.spring.mini.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClonegodDispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = -5172564106420370290L;
	
	/**
	 * Servlet 容器启动时，回调的方法，用来执行用户之定义逻辑：模拟spring容器的初始化。
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		String contextConfigLocation = config.getInitParameter("servlet-mvc");
		System.out.println("加载配置成功，contextConfigLocation: " + contextConfigLocation);
		new IOCContainer().init(contextConfigLocation);
	}
	
	// ---> http://localhost:8080/clonegod-spring-mini
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("请求URI: "+req.getRequestURI());
		resp.setContentType("text/html; charset=utf-8");
		resp.setCharacterEncoding("UTF-8");
		
		PrintWriter pw = resp.getWriter();
		pw.write("当前时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		pw.flush();
	}

	
}
