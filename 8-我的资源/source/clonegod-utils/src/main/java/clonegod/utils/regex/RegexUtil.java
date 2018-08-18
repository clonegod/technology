package clonegod.utils.regex;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

public class RegexUtil {
	
	/**
	 * 字符串的完整匹配
	 */
	public static boolean isMatch(String source, String pattern) {
		Preconditions.checkNotNull(source, "source cann't be null!");
		return source.matches(pattern);
	}
	
	/**
	 * 在整个字符串中进行匹配，返回与给定模式相匹配的子串（第一个分组中的第一个匹配的子字符串）
	 */
	public static String matchFirst(String source, String pattern) {
		Preconditions.checkNotNull(source, "source cann't be null!");
		List<String> data = matchAll(source, pattern, 1);
		if(data.isEmpty()) {
			return null;
		}
		return data.get(0);
	}
	
	/**
	 * 在整个字符串中进行匹配，返回与给定模式相匹配的子串（指定分组中的所有匹配的子字符串）
	 */
	public static List<String> matchAll(String source, String pattern, int group) {
		Preconditions.checkNotNull(source, "source cann't be null!");
		List<String> data = new LinkedList<>();
		Pattern p = Pattern.compile(pattern, Pattern.DOTALL); // 默认情况下，"."不匹配换行符！ 如果需要匹配换行符，则设置DOTALL属性。
		Matcher m = p.matcher(source);
		while(m.find()) {
			if(m.groupCount() >= group)
				data.add(m.group(group));
		}
		return data;
	}
	
	public static void main(String[] args) {
		String source = "a1b2c3e";
		System.out.println(isMatch(source, "^\\w+$"));
		System.out.println(matchFirst(source, "(\\d)"));
		System.out.println(matchAll(source, "(\\d)([a-z])", 1));
		System.out.println(matchAll(source, "(\\d)([a-z])", 2));
	}
	
}
