package clonegod.spider.core.engine;

import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import clonegod.spider.core.exception.SpiderException;
import clonegod.spider.core.page.PageContext;
import clonegod.spider.core.page.PageRequest;
import clonegod.spider.core.page.PageTask;


public class ExecutionEngine implements Serializable {
	
	private static final long serialVersionUID = -2646796419474710454L;

	private static final Logger logger = LoggerFactory.getLogger(ExecutionEngine.class); 
	
	private PageContext pageContext;
	
	private transient MyWebClient webClient; // 反序列化之后该字段为空，使用时需要重新实例化并设置相关属性。
	
	public ExecutionEngine(PageContext pageContext) {
		this.pageContext = pageContext;
		initWebClient(pageContext);
	}


	private void initWebClient(PageContext pageContext) {
		this.webClient =  new MyWebClient();
		for(Cookie cookie : pageContext.getWebClinetCookies()) {
			webClient.getCookieManager().addCookie(cookie);
		}
	}


	public Page execute(PageTask pageTask) {
		Page page = null;
		int count = 0;
		int limit = pageTask.getReTryCount();
		do {
			try {
				WebRequest request = createWebRequest(pageTask.getPageRequest());
				if(webClient == null) {
					// 反序列化后webclient为空，所以创建一个新的实例
					initWebClient(pageContext);
				}
				// 与PageTask相关的个性化配置
				webClient.init(pageTask.getWebClientOption());
				webClient.setUrlFilter(pageTask.getUrlFilter());
				// download page
				page = webClient.getPage(request);
			} catch (Exception e) {
				logger.error("download page error", e);
				throw new SpiderException(e);
			} finally {
				
			}
		} while(page==null && ++count <= limit);
		
		return page;
	}
	
	private WebRequest createWebRequest(PageRequest pageRequest) throws Exception {
		WebRequest request = new WebRequest(new URL(pageRequest.getUrl()));

		request.setCharset(Charset.forName(pageRequest.getCharset()));
		
		request.setHttpMethod(HttpMethod.valueOf(pageRequest.getHttpMethod().toUpperCase()));
		
		Map<String,String> headers = pageRequest.getHeaders();
		for(Map.Entry<String, String> entry : headers.entrySet()) {
			headers.put(entry.getKey().trim(), entry.getValue().trim());
		}
		request.getAdditionalHeaders().putAll(headers);
		
		if(pageRequest.isRawBody()) {
			request.setRequestBody(pageRequest.getBody());
		} else {
			List<NameValuePair> requestParameters = new ArrayList<>();
			for(Map.Entry<String, String> entry : pageRequest.getParameters().entrySet()) {
				requestParameters.add(new NameValuePair(entry.getKey(), entry.getValue()));
			}
			request.setRequestParameters(requestParameters);
		}
		
		return request;
	}


	public MyWebClient getWebClient() {
		return webClient;
	}


	public void setWebClient(MyWebClient webClient) {
		this.webClient = webClient;
	}
	
}
