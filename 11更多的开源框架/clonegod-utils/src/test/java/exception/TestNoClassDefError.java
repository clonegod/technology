package exception;

public class TestNoClassDefError {
	
	public static void main(String[] args) {
		try {
			new BadClass();
		} catch (Throwable e) {
			System.err.println("\n>>> NoClassDefError 需用Throwable异常进行捕获！");
			e.printStackTrace();
		}
		
		System.out.println("\n======================================");
		System.out.println("\n>>> BadClass在类加载的link阶段(静态初始化)时发生异常，因此该类的加载是不成功的，若之后再创建该类的对象就会发生NoClassDefError");
		
		new BadClass();
	}
	
	private static class BadClass {
		static {
			int n = 0;
			int j = 1 / n;
			System.out.println(j);
		}
		
		public BadClass() {
			System.out.println("Construct BadClass");
		}
	}
}
