package htmlunit.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlPageXpathTest {
	public static void main(String[] args) throws Exception {
		String url = "http://www.bjgjj.gov.cn/wsyw/wscx/gjj_cx.jsp?nicam=MjIyY3J6enk4NnI1cnlyMzI2cnI5NjcA&hskwe=R0pKd2NhM2F6YTZ3Mzk1&vnv=JiMzNzMyOTsmIzI2MTQ5OyYjMzM0NTc7&lx=0";
		
		WebClient webClient = new WebClient();
		webClient.getOptions().setProxyConfig(new ProxyConfig("127.0.0.1", 8888));
		webClient.getOptions().setJavaScriptEnabled(false);
		
		HtmlPage page = webClient.getPage(url);
		
		String xpath = "//div/span//a[contains(.,'历史明细')]/@onclick";
		
		System.out.println(page.getByXPath(xpath));
		
		List<DomNode> domNodes = ((SgmlPage)page).getByXPath(xpath);
		if(! CollectionUtils.isEmpty(domNodes)) {
			List<String> dataList = new ArrayList<String>(domNodes.size());
			for (DomNode domNode : domNodes) {
				if(domNode instanceof HtmlInput) {
					dataList.add(((HtmlInput) domNode).getValueAttribute());
				} else {
					dataList.add(domNode.getTextContent()); // ok
					dataList.add(domNode.getNodeValue());
				}
			}
			dataList.stream().forEach(x-> System.out.println(x));
		}
		
		
		webClient.close();
	}
}
