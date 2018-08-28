package xpath;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XPATH3.1  规范： https://www.w3.org/TR/2017/REC-xpath-31-20170321/
 * 
 * xpath示例： http://www.w3school.com.cn/xpath/index.asp
 * 
 * xpath 常用函数：
 * 		last()			返回当前上下文中的最后一个节点的位置
 * 		position() 		返回当前节点的位置
 * 		count(node-set) 返回节点集node-set中的节点数 
 * 
 * 		starts-with(.,'xxx')	判断节点内容是否以指定字符串开头
 * 		contains(.,'xxx')		判断节点内容是否包含指定字符串
 * 		text()					获取节点的文本内存
 * 		string-length() 		返回当前节点内容的字符串长度
 * 		
 * 		and , or, not 多个表达式结合的逻辑与或非
 * 		
 * 		parent::*	选择直接父节点
 * 		following-sibling::*  选择节点后面的兄弟节点
 * 		following-sibling::*[0]  选择节点后面的第一个兄弟节点
 */

//*[@id="logout"]//p[contains(.,'确定退出')] | //*[@id="qzdl_div"]/div/div/div/div[2]/p/span[string-length()>1]/parent::*
//*[@id="list1"]/tbody/tr/td[6][contains(.,'正常') or contains(.,'0')]/parent::*/td[2] | //*[@id="center"]/script[contains(.,'loancontrnum')]
//*[@id="mmdl"]/form//div[@class='tooltip-inner'][string-length()>1]
//*[@id="center"]//div[1]/span/a[contains(.,'信息查询')]


public class TestXpath {
	
//	<?xml version="1.0"?>
//	<Tutorials>
//		<Tutorial tutId="01" type="java">
//			<title>Guava</title>
//			<description>Introduction to Guava</description>
//			<date>04/04/2016</date>
//			<author>GuavaAuthor</author>
//		</Tutorial>
//		<Tutorial tutId="02" type="java">
//			<title>XML</title>
//			<description>Introduction to XPath</description>
//			<date>04/05/2016</date>
//			<author>XMLAuthor</author>
//		</Tutorial>
//	</Tutorials>
	
	public static void main(String[] args) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		
		Document doc = docBuilder.parse(TestXpath.class.getResourceAsStream("test.xml"));
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		String title = (String)xpath.compile("/Tutorials/Tutorial[@tutId=1]/title").evaluate(doc, XPathConstants.STRING);
		System.out.println(title);
		
		NodeList nodelist = (NodeList) xpath.compile("/Tutorials/Tutorial/@tutId").evaluate(doc, XPathConstants.NODESET);
		for(int i = 0 ; i < nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			System.out.println(node.getNodeName() + ": " + node.getTextContent());
		}
		
		
		String author = (String)xpath.compile("/Tutorials/Tutorial[1]/author").evaluate(doc, XPathConstants.STRING);
		System.out.println(author);
		
		author = (String)xpath.compile("/Tutorials/Tutorial[last()]/author").evaluate(doc, XPathConstants.STRING);
		System.out.println(author);
		
		author = (String)xpath.compile("/Tutorials/Tutorial[position() > 0 and position() < 2]/author[string-length() = 11]").evaluate(doc, XPathConstants.STRING);
		System.out.println(author);
		
		// axis
		String desc = (String)xpath.compile("/Tutorials/Tutorial[descendant::description[contains(text(), 'XPath')]]/description").evaluate(doc, XPathConstants.STRING);
		System.out.println(desc);
		
		desc = (String)xpath.compile("/Tutorials//title[contains(., 'XML')]/ancestor::*//title[1]").evaluate(doc, XPathConstants.STRING);
		System.out.println(desc);
		
	}
	
}
