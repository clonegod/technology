package groovy.java;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Test;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

public class TestJavaInvokeGroovy {

	/**
	 * java 调用 Groovy - GroovyShell
	 */
	@Test
	public void testGroovyShell() {
		Binding binding = new Binding();
		binding.setVariable("x", new BigDecimal("1.2"));
		binding.setVariable("y", 2);
		
		GroovyShell shell = new GroovyShell(binding);
		Object value = shell.evaluate("x + y");
		assert value.equals(new BigDecimal("3.2"));
	}
	
	/**
	 * java 调用 Groovy - GroovyClassLoader
	 * 仅适用于调用少量的Groovy工具类，如果要用大量的Groovy代码，推荐使用完整的GroovyScriptEngine
	 */
	@Test
	public void testGroovyClassLoader() {
		try (GroovyClassLoader loader = new GroovyClassLoader();) {
			Class<?> groovyClass = loader.parseClass(
					Paths.get("src/test/java/demos/CalculateMax.groovy").toFile());
			GroovyObject groovyObj = 
					(GroovyObject) groovyClass.newInstance();
			
			List<Integer> numbers = Arrays.asList(1,2,3,9,7,1);
			Object value = groovyObj.invokeMethod("getMax", numbers);
			
			assert value.equals(new Integer(9));
		}catch (CompilationFailedException | IOException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * java 调用 Groovy - GroovyScriptEngine
	 * 
	 * 指定Groovy脚本的路径：URL或所在目录。
	 * Groovy脚本引擎会加载那些脚本，并在必要时进行编译。
	 */
	@Test
	public void testGroovyScriptEngine() {
		String[] roots = new String[]{"src/test/java/demos"};
		try {
			GroovyScriptEngine gse = 
						new GroovyScriptEngine(roots);
			Binding binding = new Binding();
			binding.setVariable("name", "Alice");
			
			String gscriptName = "Hello.groovy";
			while(true) {
				Object output = gse.run(gscriptName, binding);
				System.out.println("output="+output);
				Thread.sleep(1000);
				if(Math.random() > 0.8) {
					break;
				}
			}
		} catch (IOException | ResourceException | ScriptException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
