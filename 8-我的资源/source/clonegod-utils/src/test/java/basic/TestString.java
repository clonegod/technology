package basic;

public class TestString {
	public static void main(String[] args) {
		String str1 = "xyz";
		String str2 = "xyz";
		System.out.println(str1 == str2);
		System.out.println(str1.hashCode() == str2.hashCode());
		
		String str3 = new String("xyz");
		String str4 = new String("xyz");
		System.out.println(str3 == str4);
		System.out.println(str3.hashCode() == str4.hashCode()); // hashcode 由字符串的内容(内部char[]数组)决定
	}
}
