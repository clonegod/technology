package clonegod.spider.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.text.StringEscapeUtils;


public class CommonUtil {
	
	public static void threadSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static Object clone(Object src) {
		Object newObject = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(src);
			oos.close();
			
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			newObject = ois.readObject();
			ois.close();
		} catch (Exception e) {
			throw new RuntimeException("clone object failed", e);
		}
		return newObject;
	}
	
	public static String unescapeHtml(String input) {
        String value = StringEscapeUtils.unescapeHtml4(input);
        return value;
	}
	
	public static void main(String[] args) {
		System.out.println(unescapeHtml("&#37329;&#26149;&#33457;"));
		
	}
}
