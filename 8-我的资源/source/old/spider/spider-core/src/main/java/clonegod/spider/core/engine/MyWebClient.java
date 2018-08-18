package clonegod.spider.core.engine;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;

import clonegod.spider.core.filter.UrlFilter;
import clonegod.spider.core.page.PageWebClientOption;

/**
 * 扩展的WebClient
 * 
 */
public class MyWebClient extends WebClient {

	private static final long serialVersionUID = -1490633011981606621L;
	
	private static final Logger logger = LoggerFactory.getLogger(MyWebClient.class);
	
//	private static final int WEB_CONNECTION_TIMEOUT = 60 * 1000;
	
	private static final int JAVASCRIPT_EXECUTE_TIMEOUT = 60 * 1000;
	
	private UrlFilter urlFilter = new UrlFilter();
	
	private static final WebResponse WEBRESPONSE_EMPTY_CONTENT = new StringWebResponse("", URL_ABOUT_BLANK);
	
	public MyWebClient() {
		this(BrowserVersion.BEST_SUPPORTED);
	}
	
	public MyWebClient(BrowserVersion browserVersion) {
		super(browserVersion);
	}
	
	/**
	 * 默认配置---对某些属性需要根据不同页面的具体需求来设置。
	 * 比如，登陆页面：需要下载图片验证码，其它页面不需要下载图片；
	 * 
	 * @param myOptions 每个页面可以有不同的option
	 */
	public void init(PageWebClientOption pageOption) {
		this.setAjaxController(new NicelyResynchronizingAjaxController()); // re-synchronize asynchronous XHR.
		this.setAlertHandler(new CollectingAlertHandler(new AlertCollectList())); // 收集页面上的alert信息
		this.getOptions().setCssEnabled(false); // 不下载解析css样式
		this.setCssErrorHandler(new SilentCssErrorHandler());
		
		this.getOptions().setProxyConfig(new ProxyConfig("127.0.0.1", 8888, false));  // 可配置代理
		
		this.getOptions().setUseInsecureSSL(true);
		this.getCookieManager().setCookiesEnabled(true); // 启用cookie
		this.setWebConnection(new MyHttpWebConnection(this)); // 设置使用的http连接器
		
		this.setJavaScriptTimeout(JAVASCRIPT_EXECUTE_TIMEOUT);
		this.getOptions().setThrowExceptionOnScriptError(false);
		this.getOptions().setThrowExceptionOnFailingStatusCode(true);
		
		this.getOptions().setDownloadImages(pageOption.isDownloadImages());
		this.getOptions().setTimeout(pageOption.getWebConnectionTimeout());
		this.getOptions().setRedirectEnabled(pageOption.isRedirectEnabled());
		this.getOptions().setJavaScriptEnabled(pageOption.isJavaScriptEnabled());
	}
	
	/**
	 * 自定义List的目的：
	 * 当页面中alert被执行并添加到AlertHandler时，可以验证程序是否正确捕获到页面上的alert内容。
	 *
	 * @param <E>
	 */
	private class AlertCollectList extends ArrayList<String> {
		private static final long serialVersionUID = 7758886956612255449L;

		@Override
		public boolean add(String e) {
			logger.info("add content to MyList:{}", e);
			return super.add(e);
		}
	}
	

	/**
	 * 1. 这里可以在发起web请求前，对webRequest对象中封装的各种请求信息进行修改
	 * 2. 这里也可以对web response的内容进行修改---比如修正页面的错误标签
	 */
	@Override
	public WebResponse loadWebResponse(WebRequest webRequest) throws IOException {
		String requestUrl = webRequest.getUrl().toExternalForm();
		logger.info("------>>> webclient request: {}", requestUrl);
		
		// 清除header，相当于重置当前请求头信息。比如，清空cookie，则可以使得本次请求为一个全新的请求。
		if(requestUrl.contains("NCCB_Encoder/Encoder")) {
			webRequest.getAdditionalHeaders().clear();
		}
		
		// 对匹配的url返回假的内容：一个空字符串。
		if(urlFilter.filter(requestUrl)) {
			logger.warn("------>>> webclient igonre request: {}", requestUrl);
			return WEBRESPONSE_EMPTY_CONTENT; 
		}
		
		WebResponse response = super.loadWebResponse(webRequest);
		
		String content = response.getContentAsString(webRequest.getCharset());
		
		// you can modify content here
		logger.debug("request url={}, response content={}", requestUrl, content);
		
		// construct new response instance use modified content
		WebResponseData data = new WebResponseData(content.getBytes(webRequest.getCharset()),
				response.getStatusCode(), response.getStatusMessage(), response.getResponseHeaders());
		response = new WebResponse(data, webRequest, response.getLoadTime());
		
		getCache().cacheIfPossible(webRequest, response, null);
		
		return response;
		
	}

	public void setUrlFilter(UrlFilter urlFilter) {
		this.urlFilter = urlFilter;
	}
	
	/**
	 * Shutdown the JavaScriptEngine.
	 * 关闭javascript引擎，释放内存资源
	 */
	public void shutdownJavascriptEngine() {
		this.getJavaScriptEngine().shutdown();
	}

}
