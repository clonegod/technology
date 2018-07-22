package regexp;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * 正则->匹配
 * 正则->切割
 * 正则->替换
 * 
 * 字符串String类中有关正则的方法
 * 	matches	返回true/false
 *  split	返回数组
 *  replaceFirst 返回替换后的新字符串
 *  replaceAll 返回替换后的新字符串
 * 
 */
public class Test02StringRegex {
	
	/**
	 * String --- matches()
	 * qq校验规则：5-12位，第一位不能为0，全为数字
	 */
	@Test
	public void testQQ() {
		String regex = "[1-9][0-9]{4,11}";
		
		String qqNo = "1234a5";
		
		boolean isValid = qqNo.matches(regex);
		
		System.out.println(qqNo+" 通过验证:" + isValid);
		
	}
	
	/**
	 * String --- matches()
	 * 手机号码匹配
	 */
	@Test
	public void testTel() {
		String phoneNo = "15011586295";
		String regex = "1[3458]\\d{9}";
		
		boolean isValid = phoneNo.matches(regex);
		
		System.out.println(phoneNo+" 通过验证:" + isValid);
	}
	
	/**
	 * String--split()
	 * 	按指定规则切割字符串，对于特殊字符的切割，需要进行转义
	 */
	@Test
	public void testSplit() {
		String text = "abc.123.go";
		String regex = "\\.";
		String arr[] = text.split(regex);
		System.out.println(Arrays.toString(arr));
		
		text = "abc|123|go";
		regex = "\\|";
		arr = text.split(regex);
		System.out.println(Arrays.toString(arr));
		
		text = "abc  123 go";
		regex = "\\s+";
		arr = text.split(regex);
		System.out.println(Arrays.toString(arr));
		
		text = "c:\\demo\\1.txt";
		regex = "\\\\";
		arr = text.split(regex);
		System.out.println(Arrays.toString(arr));
	}
	
	/**
	 * String---split()
	 * 按叠词进行切割:为了让规则的结果可以被重用，需要将规则封装到1个组中，用()来实现组的封装。
	 * 组都是有编号的，从1开始。
	 * 要使用已有的组，通过\n（n为组的编号）的方式进行引用即可。
	 */
	@Test
	public void testSplitByDuplicate() {
		String text = "fkllsaanrlgglkdfkkkjfsd";
		String regex = "(.)\\1+";
		String arr[] = text.split(regex);
		System.out.println(Arrays.toString(arr));
	}
	
	/**
	 * String---replaceAll
	 * 替换
	 */
	@Test
	public void testReplace() {
		final String text = "abcdeefghijjklmnoppqrstuuvwxyz";
		
		String regex = "(.)\\1+"; //匹配叠词
		String replacement = "#"; //替换规则：将叠词替换为#
		String newText = text.replaceFirst(regex, replacement); //只替换字符串中第一个匹配的字串
		System.out.println(newText);
		
		regex = "(.)\\1+"; // 匹配叠词
		replacement = "$1"; //替换规则： 动态引用正则表达式中的第一个组中的结果作为替换词
		newText = text.replaceAll(regex, replacement);// 对整个字符串中符合规则的字串进行替换
		System.out.println(newText);
	}
	
	
	
	@Test
	public void testEmail() {
		this.isVaildEmail("fkdsa@lj.co]");
	}
	
	public static final String VALID_EMAIL_PATTERN =
			"([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]"
					+ "{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))"
					+ "([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)";

	public void isVaildEmail(String emailAddress) {
		if (emailAddress.matches(VALID_EMAIL_PATTERN)) {
			addEmailAddress(emailAddress);
		} else {
			throw new IllegalArgumentException(emailAddress);
		}
	}

	private void addEmailAddress(String emailAddress) {
		System.out.println("valid email address");
	}
	
	
	//===========================匹配字符串=============================
	
	@Test
	public void testYes() {
		boolean yes = goodAnswer("y");
		System.out.println(yes);
	}
	
	public boolean goodAnswer(String answer) {
		return (Pattern.matches("[Yy]es|[Yy]|[Tt]rue", answer));
	}
	
	
	//===========================匹配字符串=============================
	
	@Test
	public void testDot() {
		// 字符串中的.
		String str = "abc.def.hij";
		String arr[] = str.split("\\.");
		System.out.println(Arrays.toString(arr));
		
		// 正则中的.
		String reg = ".+"; // 正则表达式中的.表示任意字符
		System.out.println("abc".matches(reg));
	}
	
	//=========================切割字符串===============================
	
	@Test
	public void testSplitViaDuplicateChars() {
		String str = "fslkadj####fldsaajlf^^^^kdsja";
		
		// 使用"括号"对子表达式进行封装，作为1个组进行解析，使其具有复用性 
		// \\1 表示引用第1组的结果
		// + 表示至少出现1次
		String arr[] = str.split("(.)\\1+"); 
		System.out.println(Arrays.toString(arr));
	}
	
	//============================替换字符串============================
	
	@Test
	public void testReplace2() {
		String str = "fslkadj####fldsaajlf^^^^kdsja";
		
		str = str.replaceAll("(.)\\1+", "$1"); // 对正则表达式中某个组所匹配到的结果进行引用
		System.err.println(str);
	}
	
	@Test
	public void testReplace3() {
		String telno = "13772390801";
		
		telno = telno.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
		System.err.println(telno);
	}
	
	
	//============================获取字符串============================
	
	@Test
	public void testSearch() {
		String input = "abc";
		
		String reg = ".";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(input);
		
		while(m.find()) {
			System.out.println(m.group()+", start="+m.start()+", end="+m.end());
		}
		
		System.err.println(input);
	}
	
	@Test
	public void testSearch2() {
		String input = "hello world, hi java, ok";
		
		String regex = "\\b[a-z]{2}\\b";
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		
		while(m.find()) {
			System.err.println(m.group());
		}
	}
	
	
	@Test
	public void test() {
		// 待排序的IP地址
		String[] ips = {"192.168.3.10", "127.0.0.1", "10.0.60.94", "215.2.3.123"};
		
		String ipsStr = Arrays.toString(ips);
		ipsStr = ipsStr.replaceAll("[\\[|\\]]", "");
		System.err.println(ipsStr);
		
		// 补0，让所有位都至少3个数字
		ipsStr = ipsStr.replaceAll("(\\d+)", "00$1");
		System.err.println(ipsStr);
		
		// 截取，让所有位都为3位数字
		ipsStr = ipsStr.replaceAll("0*(\\d{3})", "$1");
		System.err.println(ipsStr);
		
		// 排序
		TreeSet<String> set = new TreeSet<String>();
		set.addAll(Arrays.asList(ipsStr.split(",\\s*")));
		System.err.println(set);
		
		// 删除前缀0
		for(String ip : set) {
			String ipstr = ip.replaceAll("0*(\\d+)", "$1");
			System.err.println(ipstr);
		}
		
	}
}
