package part2_oop

class SomeClass {
	/**
	 * 声明属性或变量的规则：
	 * 当变量既没有类型又没有修饰符时，就必须使用def来声明变量。
	 * 	private、protected和public用来修饰可访问范围;
	 * 	static用来标示为类变量;
	 * 	final用来限制对变量再次赋值;
	 * ---------------------------------------------------------------
	 * 
	 * groovy 自动生成getter/setter方法，前提是：
	 * 	声明变量时没有指定访问范围修饰符。
	 * 
	 * ---------------------------------------------------------------
	 * 用def声明的属性或者变量是没有类型的（在内部将被声明为Object类型）。
	 * 
	 * 
	 */
	public fieldWithModifier
	
	String typedField
	
	def untypedField
	
	protected field1, field2, field3
	
	private assignedField = new Date()
	
	static classField 
	
	public static final String CONSTA = 'a', CONSTB = 'b'
	
	def someMethod() {
		def localUntypedMethodVar = 1
		int localTypedMehtodVar = 2
		def localVarWithoutAssignment, andAnotherone
	}
	
	Object get (String name) {
		return "default value"
	}
	
	void set (String name, Object value){
		// do nothing
	}
	
}
