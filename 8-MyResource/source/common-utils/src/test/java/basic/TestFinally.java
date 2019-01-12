package basic;

public class TestFinally {
	
	public static void main(String[] args) {
		
		System.out.println(finallyTester(true));
	    System.out.println(finallyTester(false));
		
		try {
			System.out.println("application start");
			System.exit(1);
		} finally {
			System.out.println("finally 被执行");
		}
	}
	
	/**
	 * It will return "c" both times.
	 * 
	 * @param succeed
	 * @return
	 */
	public static String finallyTester(boolean succeed) {
	    try {
	        if(succeed) {
	            return "a";
	        } else {
	            throw new Exception("b");
	        }
	    } catch(Exception e) {
	        return "b";
	    } finally {
	    	//  putting a return statement in the finally block is a very bad idea.
	    	if(true)
	    		return "c";
	    }
	  }

}
