package part2_oop.grovvybean

// 在底层， groovy 提供了与 java 代码等价的属性访问方法，但是你不用输入这些代码
class MyBean implements Serializable {
	
	// 编译为字节码的时候，会对这些属性自动添加getter/setter
	def untyped
	String typed
	def item1, item2
	def assigned = 'default value'
	
}


