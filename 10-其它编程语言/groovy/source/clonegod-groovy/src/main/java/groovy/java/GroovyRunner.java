package groovy.java;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import groovy.util.GroovyScriptEngine;

public class GroovyRunner {
	
	static final String DEFAULT_ROOT_PATH = "./scripts";
	
	static ConcurrentHashMap<String, Class<?>> SCRIPT_CLASS_MAP = new ConcurrentHashMap<>();
	
	public static void main(String[] args) {
		GroovyRunner runner = new GroovyRunner();

		String data = "hello world";
		while(true) {
			String scriptName = "DetailFormatter_Baidu.groovy";
			Object result = runner.format(scriptName, data);
			System.out.println(result);
		}
	}

	public Object format(String scriptName, String data) {
		Object result = null;
		try {
			Class<?> scriptClass = new GroovyScriptEngine(DEFAULT_ROOT_PATH).loadScriptByName(scriptName);
			Object scriptInstance = scriptClass.newInstance();
			Method method = scriptClass.getDeclaredMethod("format", new Class[] {String.class});
			result = method.invoke(scriptInstance, new Object[] {data});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
