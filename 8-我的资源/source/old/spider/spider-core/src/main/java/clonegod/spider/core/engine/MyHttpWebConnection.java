package clonegod.spider.core.engine;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.HttpWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;

/**
 * 自定义WebConnection
 * 复写HttpWebConnection中的getResponse方法，可以实现在web资源加载成功后第一时间进行处理
 * 
 */
public class MyHttpWebConnection extends HttpWebConnection {
	

	Logger logger = LoggerFactory.getLogger(MyHttpWebConnection.class);

	public MyHttpWebConnection(WebClient webClient) {
		super(webClient);
	}

	/**
	 * 拦截web服务器响应的结果。
	 * 可对结果进行修改后返回新内容。
	 */
	@Override
	public WebResponse getResponse(WebRequest request) throws IOException {
		 WebResponse response = super.getResponse(request);
		 
		 String requestUrl = request.getUrl().toExternalForm();
		 //logger.info("request url={}", requestUrl);
         
		 if (requestUrl.contains("www.xxx.com")) {
             String content = response.getContentAsString();
             logger.info("------>>> original response content:\n{}", content);
             
             //change content
             content = content.replace("var flag=true;", "var flag=false");
             logger.info("------>>> modified response content:\n{}", content);
             
             // reconstruct web response data
             WebResponseData data = new WebResponseData(content.getBytes(),
                     response.getStatusCode(), response.getStatusMessage(), response.getResponseHeaders());
             response = new WebResponse(data, request, response.getLoadTime());
         }
         
         return response;
	}

}
