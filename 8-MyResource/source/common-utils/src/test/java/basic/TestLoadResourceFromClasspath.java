package basic;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.google.common.base.Charsets;

public class TestLoadResourceFromClasspath {

	/**
	 * Class.getResource(String path)
	 * 	相对路径：
	 * 		path  不以’/'开头时，是相对于该类所在package获取资源文件。
	 * 	
	 * 绝对路径：
	 * 		path  以’/'开头时，则是以classpath路径为根路径，在此下获取资源文件。（如果资源在子目录下，则路径中要包含子目录，因为不是递归搜索的）
	 */
	@Test
	public void testClassLoad() throws Exception {
		System.out.println(IOUtils.toString(TestLoadResourceFromClasspath.class.getResourceAsStream("1.txt"), StandardCharsets.UTF_8));
		System.out.println(IOUtils.toString(TestLoadResourceFromClasspath.class.getResourceAsStream("/2.txt"), StandardCharsets.UTF_8));
		System.out.println(IOUtils.toString(TestLoadResourceFromClasspath.class.getResourceAsStream("/config/3.txt"), StandardCharsets.UTF_8));
		
	}
	
	/**
	 * Class.getClassLoader().getResource(String path)
	 * 		相对路径：
	 * 			path 相对于classpath的路径
	 * 		绝对路径：
	 * 			不支持，path 不能以’/'开头时；
	 */
	@Test
	public void testClassLoaderLoad() throws Exception {
		System.out.println(IOUtils.toString(TestLoadResourceFromClasspath.class.getClassLoader().getResourceAsStream("basic/1.txt"), StandardCharsets.UTF_8));
		System.out.println(IOUtils.toString(TestLoadResourceFromClasspath.class.getClassLoader().getResourceAsStream("2.txt"), StandardCharsets.UTF_8));
		System.out.println(IOUtils.toString(TestLoadResourceFromClasspath.class.getClassLoader().getResourceAsStream("config/3.txt"), StandardCharsets.UTF_8));
		
	}
	
}
