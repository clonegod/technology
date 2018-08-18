package regex.test;

import org.junit.Test;

import clonegod.spider.common.util.RegexUtil;

public class RegexTest {
	
	@Test
	public void test() {
		String text =  "<script>window.location='gjjcx-logineoor.jsp?id=3';</script>";
		String regex = "<script>window.location=(.*?);</script>";
		String result = RegexUtil.match(text, regex);
		System.out.println(result);
	}
	
	@Test
	public void testMultiMatch() {
		String text = "[javascript:window.open('gjj_cx.jsp?nicam=MjIyY3J6enk4NnI1cnlyMzI2cnIyMDcA&hskwe=R0pKd2NhM2F6YTZ3NjI3&vnv=JiMzNzMyOTsmIzI2MTQ5OyYjMzM0NTc7&lx=0','','top=0,left=0,toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=550,height=500');, javascript:window.open('gjj_cx.jsp?nicam=MjIyY3J6enk4NnI1cnlyMzI2cnIyMDcA&hskwe=R0pKd2N6dzN6M3c4NzIy&vnv=JiMzNzMyOTsmIzI2MTQ5OyYjMzM0NTc7&lx=0','','top=0,left=0,toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=550,height=500');]";
		System.out.println(RegexUtil.matchList(text, "(gjj_cx\\.jsp.*?lx=0)"));
	}
}
