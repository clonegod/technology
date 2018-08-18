package spidermain.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.google.common.reflect.TypeToken;

import clonegod.spider.common.constant.SpiderConst;
import clonegod.spider.common.util.JsonUtil;
import clonegod.spider.core.filter.UrlFilter;
import clonegod.spider.core.page.Field;
import clonegod.spider.core.page.PageExtract;
import clonegod.spider.core.page.PageRequest;
import clonegod.spider.core.page.PageTask;
import clonegod.spider.core.page.PageWebClientOption;
import clonegod.spider.core.parser.PageParserType;

/**
 * 循环请求的设计：
 * 	设计一颗二叉树，当遇到循环时，动态添加叶子节点，若是嵌套循环，则在当前叶子节点下继续添加叶子节点.
 * 	动态生成二叉树的叶子节点，遍历二叉树---即可实现分页功能.
 * 	login.html
 * 		-> mainRecord1.html
 * 			--> detailRecord11.html
 * 			--> detailRecord12.html	
 * 		-> mainRecord2.html
 * 			--> detailRecord21.html
 * 			--> detailRecord22.html
 * 				---> subDetailRecord221.html
 * 				---> subDetailRecord222.html
 *  logout.html
 */
public class PageTaskFactory {
	
	static String loginId = "1111111";
	static String password = "222222";
	static String loginType = "x";
	
	/**
	 * 从本地文件读取测试账号
	 */
	static {
		try {
			InputStream in = new FileInputStream("/var/web-secret/accounts.json");
			String data = IOUtils.toString(in, SpiderConst.CHARSET_UTF8);
			@SuppressWarnings("serial")
			Map<String, Map<String,Map<String,String>>> datamap = 
				JsonUtil.fromJson(data, new TypeToken<Map<String, Map<String,Map<String,String>>>>() {});
			System.out.println(datamap);
			Map<String,String> loginTypeMap = datamap.get("beijing").get("联名卡号");
			loginId = loginTypeMap.get("loginId");
			password = loginTypeMap.get("password");
			loginType = loginTypeMap.get("loginType");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static PageTask createIndexPage() {
		PageTask task = new PageTask();
		
		PageRequest request = new PageRequest.Builder()
				.url("http://www.bjgjj.gov.cn/wsyw/wscx/gjjcx-login.jsp")
				.httpMethod(HttpMethod.GET.name())
				.charset(SpiderConst.CHARSET_GBK)
				.contentType(PageParserType.HTML.name())
				.header("Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
				.header("Accept-Encoding:gzip, deflate")
				.header("Accept-Language:zh-CN,zh;q=0.8")
				.header("Cache-Control:no-cache")
				.header("Host:www.bjgjj.gov.cn")
				.header("Referer:http://www.bjgjj.gov.cn/wsyw/wscx/gjjcx-choice.jsp")
				.header("User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
				// 辅助参数，使用spel对动态参数求值时使用
				.cachedArg("loginId", loginId)
				.cachedArg("password", password)
				.cachedArg("fMcM3", "pdcss123")
				.cachedArg("TjXjbs4", "css11q1a")
				.cachedArg("fEAI5", "co1qacq11")
				.param("t", System.currentTimeMillis()+"")
				.build();
		
		UrlFilter urlFilter = new UrlFilter();
		urlFilter.add("asdwqnasmdnams.jsp").add("xxx.js");
		
		PageWebClientOption webclientOption = new PageWebClientOption();
		webclientOption.setJavaScriptEnabled(true);
		
		List<Field> extFields = new ArrayList<>();
		extFields.add(new Field(SpiderConst.IMAGE_CODE, PageParserType.HTML, "//*[@id='sds']/img"));
		extFields.add(new Field("lk", PageParserType.TEXT, "<input .* value=\"(.*?)\" id=\"lk\">")); // 浏览器定时从服务器获取的动态字符串
		extFields.add(new Field("lb", PageParserType.HTML, "//input[@id='lb']")); // 登陆类型编号-默认联名卡
		// 动态参数，以spel:为前缀，将通过spel进行变量替换
		extFields.add(new Field("bh", PageParserType.HTML, "spel:'javascript:strEncode('''+['loginId']+''','''+['fMcM3']+''','''+['TjXjbs4']+''','''+['fEAI5']+''');'"));
		extFields.add(new Field("mm", PageParserType.HTML, "spel:'javascript:strEncode('''+['password']+''','''+['fMcM3']+''','''+['TjXjbs4']+''','''+['fEAI5']+''');'"));
		
		
		List<Field> successFields = new ArrayList<>();
		successFields.add(new Field("lb", PageParserType.HTML, "//input[@id='lb']"));

		PageExtract extracter = new PageExtract();
		extracter.setExtFields(extFields);
		extracter.setSuccessFields(successFields);

		task.setTaskOrder(101);
		task.setTaskName("index");
		task.setPauseNext(true);
		task.setPageRequest(request);
		task.setUrlFilter(urlFilter);
		task.setWebClientOption(webclientOption);
		task.setPageExtract(extracter);
		return task;
	}
	
	public static PageTask createLkPage() {
		PageTask task = new PageTask();
		
		PageRequest request = new PageRequest.Builder()
				.url("http://www.bjgjj.gov.cn/wsyw/wscx/asdwqnasmdnams.jsp?t=1")
				.httpMethod(HttpMethod.POST.name())
				.charset(SpiderConst.CHARSET_GBK)
				.contentType(PageParserType.TEXT.name())
				.header("Accept: */*")
				.header("Accept-Encoding: gzip, deflate")
				.header("Accept-Language: zh-CN,zh;q=0.8")
				.header("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
				.header("Content-Type: text/html;")
				.header("Connection: keep-alive")
				.header("Referer: http://www.bjgjj.gov.cn/wsyw/wscx/gjjcx-login.jsp")
				.header("Origin: http://www.bjgjj.gov.cn")
				.header("Host: www.bjgjj.gov.cn")
				.isRawBody(true)
				.body("")
				.build();
		
		PageWebClientOption webclientOption = new PageWebClientOption();
		webclientOption.setRedirectEnabled(true);
		
		List<Field> extFields = new ArrayList<>();
		extFields.add(new Field("lk", PageParserType.TEXT, "\\w{4}(\\d+)"));
		
		List<Field> successFields = new ArrayList<>();
		successFields.add(new Field("lk", PageParserType.TEXT, "\\w{4}(\\d+)"));
		
		PageExtract extracter = new PageExtract();
		extracter.setExtFields(extFields);
		extracter.setSuccessFields(successFields);
		
		task.setTaskOrder(201);
		task.setTaskName("lk");
		task.setPauseNext(false);
		task.setPageRequest(request);
		task.setWebClientOption(webclientOption);
		task.setPageExtract(extracter);
		return task;
	}
	
	public static PageTask createLoginPage() {
		PageTask task = new PageTask();
		
		PageRequest request = new PageRequest.Builder()
				.url("http://www.bjgjj.gov.cn/wsyw/wscx/gjjcx-choice.jsp")
				.httpMethod(HttpMethod.POST.name())
				.charset(SpiderConst.CHARSET_GBK)
				.contentType(PageParserType.HTML.name())
				.header("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
				.header("Accept-Encoding: gzip, deflate")
				.header("Accept-Language: zh-CN,zh;q=0.8")
				.header("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
				.header("Content-Type: application/x-www-form-urlencoded")
				.header("Cache-Control: max-age=0")
				.header("Referer: http://www.bjgjj.gov.cn/wsyw/wscx/gjjcx-login.jsp")
				.header("Origin: http://www.bjgjj.gov.cn")
				.header("Host: www.bjgjj.gov.cn")
				.param("lk", "spel:['lk']")
				.param("lb", "spel:['lb']")
				.param("bh", "spel:['bh']") // 账号
				.param("mm", "spel:['mm']") // 密码
				.param("gjjcxjjmyhpppp",  "spel:['imageCode']")
				.build();
		
		PageWebClientOption webclientOption = new PageWebClientOption();
		webclientOption.setJavaScriptEnabled(false);
		
		List<Field> successFields = new ArrayList<>();
		successFields.add(new Field("success.msg", PageParserType.HTML, "//*[@id='new-mytable']/tbody/tr[position()>1]")); // 成功页面包含的特征
		
		List<Field> failedFields = new ArrayList<>();
		failedFields.add(new Field("error.msg1", PageParserType.TEXT, "<script>alert(.*?);")); // 失败页面包含的特征1
		failedFields.add(new Field("error.msg2", PageParserType.TEXT, "<script>window.location=(.*?);</script>",
				"spel:#this.contains('gjjcx-logineoor.jsp?id=3')?'没有此条记录':#this"));  // 失败页面包含的特征2
		
		List<Field> extFields = new ArrayList<>();
		extFields.add(new Field("loop:login:mainRecord", PageParserType.HTML, 
				"//table[@id='new-mytable']//td//a[contains(@onclick,'gjj_cx.jsp')]/@onclick", 
				"regex:(gjj_cx\\.jsp.*?lx=0)")); // 从登陆成功页面获取账号列表
		
		PageExtract extracter = new PageExtract();
		extracter.setSuccessFields(successFields);
		extracter.setFailedFields(failedFields);
		extracter.setExtFields(extFields);
		
		task.setTaskOrder(301);
		task.setTaskName("login");
		task.setPauseNext(false);
		task.setPageRequest(request);
		task.setWebClientOption(webclientOption);
		task.setPageExtract(extracter);
		task.setSubPageTaskName("mainRecord");
		return task;
	}
	
	public static PageTask createMainRecordPage() {
		PageTask task = new PageTask();
		
		PageRequest request = new PageRequest.Builder()
				.url("spel:'http://www.bjgjj.gov.cn/wsyw/wscx/'+['loop:login:mainRecord']['%s']")
				.httpMethod(HttpMethod.GET.name())
				.charset(SpiderConst.CHARSET_GBK)
				.contentType(PageParserType.HTML.name())
				.header("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
				.header("Accept-Encoding: gzip, deflate")
				.header("Accept-Language: zh-CN,zh;q=0.8")
				.header("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
				.header("Content-Type: application/x-www-form-urlencoded")
				.header("Cache-Control: max-age=0")
				.header("Referer: http://www.bjgjj.gov.cn/wsyw/wscx/gjjcx-choice.jsp")
				.header("Host: www.bjgjj.gov.cn")
				.build();
		
		PageWebClientOption webclientOption = new PageWebClientOption();
		webclientOption.setJavaScriptEnabled(false);
		
		List<Field> successFields = new ArrayList<>();
		successFields.add(new Field("success.msg", PageParserType.TEXT, "(住房公积金个人总账信息)")); // 成功页面包含的特征
		
		List<Field> failedFields = new ArrayList<>();
		failedFields.add(new Field("error.msg1", PageParserType.TEXT, "(.*?)")); // 失败页面包含的特征1
		
		List<Field> extFields = new ArrayList<>();
		extFields.add(new Field("detailRecordURI", PageParserType.HTML, 
				"//div/span//a[contains(.,'历史明细')]/@onclick", 
				"regex:window.open\\('(gjj_cxls\\.jsp.*?)'"));
		
		List<Field> dataFields = new ArrayList<>();
		dataFields.add(new Field("mainRecord.realName", PageParserType.TEXT, 
				"姓名.*?class=\"style2\">(.*?)</td>", "spel:T(clonegod.spider.util.CommonUtil).unescapeHtml(#this)")); // 姓名
		dataFields.add(new Field("mainRecord.govtId", PageParserType.TEXT, 
				"证件号.*?class=\"style2\">(.*?)</td>")); // 证件号码
		
		PageExtract extracter = new PageExtract();
		extracter.setSuccessFields(successFields);
		extracter.setFailedFields(failedFields);
		extracter.setExtFields(extFields);
		extracter.setBusinessDataFields(dataFields);
		
		task.setTaskOrder(401);
		task.setTaskName("mainRecord");
		task.setPauseNext(false);
		task.setPageRequest(request);
		task.setWebClientOption(webclientOption);
		task.setPageExtract(extracter);
		task.setSubPageTaskName("detailRecord");
		return task;
	}
	
	public static PageTask createMainDetailPage() {
		PageTask task = new PageTask();
		
		PageRequest request = new PageRequest.Builder()
				.url("spel:'http://www.bjgjj.gov.cn//wsyw/wscx/'+['detailRecordURI']")
				.httpMethod(HttpMethod.GET.name())
				.charset(SpiderConst.CHARSET_GBK)
				.contentType(PageParserType.HTML.name())
				.header("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
				.header("Accept-Encoding: gzip, deflate")
				.header("Accept-Language: zh-CN,zh;q=0.8")
				.header("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
				.header("Content-Type: application/x-www-form-urlencoded")
				.header("Cache-Control: max-age=0")
				.header("Host: www.bjgjj.gov.cn")
				.build();
		
		PageWebClientOption webclientOption = new PageWebClientOption();
		webclientOption.setJavaScriptEnabled(false);
		
		List<Field> successFields = new ArrayList<>();
		successFields.add(new Field("success.msg", PageParserType.TEXT, "(.*)")); // 成功页面包含的特征
		
		List<Field> failedFields = new ArrayList<>();
		
		List<Field> extFields = new ArrayList<>();
		
		List<Field> dataFields = new ArrayList<>();
		
		PageExtract extracter = new PageExtract();
		extracter.setSuccessFields(successFields);
		extracter.setFailedFields(failedFields);
		extracter.setExtFields(extFields);
		extracter.setBusinessDataFields(dataFields);
		
		task.setTaskOrder(501);
		task.setTaskName("detailRecord");
		task.setPauseNext(false);
		task.setPageRequest(request);
		task.setWebClientOption(webclientOption);
		task.setPageExtract(extracter);
		return task;
	}
}
