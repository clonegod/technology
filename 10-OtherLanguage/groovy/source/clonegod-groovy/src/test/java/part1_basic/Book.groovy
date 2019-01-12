package part1_basic

/***
 * Book类的文件Book.groovy必须在类路径中，groovy运行时系统将自动找到这个文件，显式的将它编译成类，并且产生一个新的Book对象实例。
 */
// 声明类 - 类用来定义了一个对象的结构。
class Book {
	private String title;

	Book(String title) {
		super();
		this.title = title;
	}

	// groovy中缺省的方法访问修饰符是public
	String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}

