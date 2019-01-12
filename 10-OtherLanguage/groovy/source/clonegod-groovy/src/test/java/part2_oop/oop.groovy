package part2_oop

/**
 * 类、对象、构造方法、引用
 * 运行时改变对象的能力、拦截方法调用
 * 
 * 变量的定义：
 * 1、属性或本地变量：
 * 		属性和本地变量在使用之前必须进行声明。声明总是包含指定一个名称，也许还包含一个可选的类型，修饰符和分配初始化值，一旦声明之后，变量就可以通过相应的名称进行引用。
 * 2、脚本中的变量：
 * 		脚本允许使用没有声明的变量 - binding提供这类变量的全局绑定
 * 
 */

x = new SomeClass()

def localvar = 1  // 脚本中使用def声明变量
boundvar = 2 // 没有声明的变量，设置到binding

def someMethod() {
	def localMethodVar = 3 // 方法中的变量，使用def声明后，外界无法访问
	boundVar2 = 4 // 没有声明的变量，设置到binding，方法调用后，该变量可以被外界访问到
}

println localvar
println boundvar

someMethod()

//println localMethodVar
println boundVar2

