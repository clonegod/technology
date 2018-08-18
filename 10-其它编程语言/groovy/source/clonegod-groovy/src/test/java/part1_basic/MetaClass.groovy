package part1_basic

/** 通过MetaClass，groovy能修改groovy调用者调用的任何方法。
 *  groovy使用这种机制增加新的能力给现有的JDK类库，这些就是GDK。
 *  */

// groovy动态性的第一个方面：
//方法的调用在底层被重定向为通过对象的MetaClass进行处理，这个MetaClass能在运行时处理如拦截、重定向、增加/删除方法等等之类的方法调用，从而实现动态化的方法执行
// getMetaClass().invokeMethod(this, "foo", EMPTY_PARAMS_ARRAY)


// groovy动态性的第二个方面：
// 动态代码的第二个选项是把代码放在一个字符串中并且通过groovy来运行它
def code = '1 + ' 
code += System.getProperty('os.version')  // 通过字符串构建动态代码

//prints “1 + 5.1” 
println code 

//prints “6.1” 
println evaluate(code) // 执行动态代码

