package clonegod.spider.core.page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * 构造每个请求页面的相关数据
 *	
 */
public class PageRequest implements Serializable {
	
	private static final long serialVersionUID = 1906366046763399003L;
	
	private String url;
	private String httpMethod; // get, post
	private String charset;  // gbk, utf-8
	private String contentType; // html, json, text, image, pdf

	private Map<String,String> headers = new HashMap<>();
	
	private boolean rawBody; // 以xmlBody的形式提交post请求
	private String body;
	
	/**
	 * 动态参数：页面请求时需要的参数，使用spel进行变量替换
	 * 如果网站对提交的参数顺序有要求，则使用LinkedHashMap
	 * */
	private Map<String, String> parameters = new HashMap<>();
	
	/**辅助参数：不作为请求参数，计算动态参数时使用*/
	private Map<String, String> cachedArgs = new HashMap<>();
	
	public static class Builder {
		private PageRequest pageModel = new PageRequest();
		
		public Builder url(String url) {
			pageModel.url = url;
			return this;
		}
		
		public Builder httpMethod(String method) {
			pageModel.httpMethod = method;
			return this;
		}
		
		public Builder contentType(String contentType) {
			pageModel.contentType = contentType.toUpperCase();
			return this;
		}
		
		public Builder param(String name, String value) {
			Assert.notNull("parameter name should not null", name);
			pageModel.parameters.put(name, value);
			return this;
		}
		public Builder cachedArg(String name, String value) {
			Assert.notNull("parameter name should not null", name);
			pageModel.cachedArgs.put(name, value);
			return this;
		}
		
		public Builder header(String name, String value) {
			Assert.notNull("header name should not null", name);
			pageModel.headers.put(name, value);
			return this;
		}
		
		public Builder header(String nameValue) {
			Assert.notNull("header nameValue should not null", nameValue);
			int index = nameValue.indexOf(":");
			pageModel.headers.put(nameValue.substring(0, index), nameValue.substring(index+1));
			return this;
		}
		
		public Builder headerAll(Set<String> headers) {
			for(String nameValuePair : headers) {
				if(! nameValuePair.contains(":")) {
					continue;
				}
				header(nameValuePair);
			}
			return this;
		}
		
		public Builder charset(String charset) {
			pageModel.charset = charset;
			return this;
		}
		
		public Builder body(String body) {
			pageModel.body = body;
			return this;
		}
		
		public Builder isRawBody(boolean rawBody) {
			pageModel.rawBody = rawBody;
			return this;
		}
		
		public PageRequest build() {
			return pageModel;
		}
	}

	public String getUrl() {
		return url;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
	
	public Map<String, String> getCachedArgs() {
		return cachedArgs;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public String getPageType() {
		return contentType;
	}

	public String getCharset() {
		return charset;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isRawBody() {
		return rawBody;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
