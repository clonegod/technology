package clonegod.spider.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexUtil {
	
	public static String match(String text, String regex) {
		String value = null;
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(text);
		if(m.find()) {
			value = m.group(1);
		}
		return value;
	}
	
	public static List<String> matchList(String text, String regex) {
		List<String> value = new ArrayList<String>();
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(text);
		while(m.find()) {
			value.add(m.group(1));
		}
		return value;
	}
	
	public static void main(String[] args) {
		String str = "1-2-c-4";
		System.out.println(match(str, "(\\d)"));
		System.out.println(matchList(str, "(\\d)"));
	}
}
