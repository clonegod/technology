package system;
public class TestSystemEnv {
	/**
	 * 操作系统的环境变量，比如定义的JAVA_HOME, Path路径（可执行程序的搜索路径）等。
	 */
	public static void main(String[] args) {
		System.getenv().forEach((key, value) -> {
			System.out.println(String.join("=", key, value));
		});
	}
}
