package clonegod.spider.core.filter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UrlFilter implements Serializable {
	
	private static final long serialVersionUID = -8546550283150549311L;
	
	Set<String> igonreUrls = new HashSet<>();
	
	public Set<String> add(String url) {
		igonreUrls.add(url);
		return igonreUrls;
	}
	
	/**
	 * 加载一个页面可能会发起多个资源请求，比如css，js，image，html等静态资源。
	 * 可以定义一组需要被过滤掉的url，对这些url返回"空字符串"。
	 */
	public boolean filter(String url) {
		for(String igonreUrl : igonreUrls) {
			if(url.contains(igonreUrl) || url.matches(igonreUrl)) {
				return true;
			}
		}
		return false;
	}
	
}
