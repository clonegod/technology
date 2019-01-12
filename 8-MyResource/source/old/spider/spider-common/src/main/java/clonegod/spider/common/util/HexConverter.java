package clonegod.spider.common.util;

import org.apache.commons.lang3.StringUtils;

public class HexConverter {
	/**
	 * 对普通字符串进行加密（不算加密，只是对每个ascii字符用16进制来表示罢了）
	 * 
	 * "pdcss123" => "\\x70\\x64\\x63\\x73\\x73\\x31\\x32\\x33"
	 * 
	 * @return 
	 * 
	 */
	public static String asciiToHexString(String plainText) {
		StringBuilder buf = new StringBuilder();

		for(char c : plainText.toCharArray()) {
			short charShort = (short)c;
			String charHex = Integer.toHexString(charShort);
			// System.out.println(String.format("Ascii %s: 10进制=%s, 16进制=%s", c, charShort, charHex));
			buf.append("\\x"+charHex);
		}
		
		return buf.toString();
	}
	
	/**
	 * 将字符从16进制转换为10进制，然后转换为Ascii字符，即可还原字符串
	 * 
	 * "\\x70\\x64\\x63\\x73\\x73\\x31\\x32\\x33"  =>  "pdcss123"
	 * @return 
	 */
	public static String hexStringToAscii(String hexStr) {
		StringBuilder buf = new StringBuilder();
		String[] hexArray = hexStr.replace("\\", "").split("x");
		
		int radix = 16;
		for(String number : hexArray) {
			if(StringUtils.isEmpty(number)) {
				continue;
			}
			int integer = Integer.parseInt(number, radix); // 将number从16进制转换为10进制
			char c = (char)integer;
			buf.append(c);
		}
		
		return buf.toString();
	}
	
	public static void main(String[] args) {
		final String text = "pdcss123";
		final String hexString = "\\x70\\x64\\x63\\x73\\x73\\x31\\x32\\x33"; 
				
		String result = asciiToHexString(text);
		System.out.println(hexString.equals(result));
		
		result = hexStringToAscii(result);
		System.out.println(text.equals(result));
		
		System.out.println(hexStringToAscii("\\x70\\x64\\x63\\x73\\x73\\x31\\x32\\x33")); //pdcss123
		System.out.println(hexStringToAscii("\\x63\\x73\\x73\\x31\\x31\\x71\\x31\\x61")); //css11q1a
		System.out.println(hexStringToAscii("\\x63\\x6f\\x31\\x71\\x61\\x63\\x71\\x31\\x31")); //co1qacq11
		
		
		System.out.println(hexStringToAscii("\\x23\\x6c\\x6b"));
		
		//http://www.bjgjj.gov.cn/wsyw/wscx/asdwqnasmdnams.jsp
		System.out.println(asciiToHexString("lk"));
	}
}
