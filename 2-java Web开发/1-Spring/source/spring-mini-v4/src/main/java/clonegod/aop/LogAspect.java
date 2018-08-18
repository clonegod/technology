package clonegod.aop;

public class LogAspect {
	
	public void before() {
		System.out.println(this.getClass().getName() + " ... before" );
	}
	
	public void after() {
		System.out.println(this.getClass().getName() + " ... after" );
		
	}
	
}
