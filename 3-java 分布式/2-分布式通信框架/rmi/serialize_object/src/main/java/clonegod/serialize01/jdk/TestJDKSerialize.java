package clonegod.serialize01.jdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import clonegod.model.Person;
import clonegod.model.User;
import clonegod.serialize.ISerializer;

public class TestJDKSerialize {
	
	ISerializer serializer = new JDKSerializer();
	
	/**
	 * transient 修饰的字段，不会被序列化
	 * static 静态变量，不会被序列化
	 */
	@Test
	public void testSerialize() throws Exception {
		Person person = new Person();
		person.setName("mic");
		person.setAge(18);
		person.setPassword("mic18");
		
		byte[] data = serializer.serialize(person);
		
		File file = new File("target/person.dat");
		
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		fos.flush();
		fos.close();
		
		
		Person.USER_TYPE = "GUEST"; // 静态变量不会被序列化，所以反序化得到的对象将引用静态变量最新的值
		FileInputStream fis = new FileInputStream(file);
		byte[] bytes = new byte[fis.available()];
		fis.read(bytes);
		fis.close();
		
		Person mic18 = serializer.deSerialize(bytes, Person.class);
		System.out.println(mic18);
		System.out.println(mic18.USER_TYPE);
	}
	
	/**
	 * Serializable 不可继承性！
	 * 
	 * 序列化子类对象时，如果父类没有实现 Serializable接口，则父类的字段都不会被序列化。
	 * 因此，如果序列化时需要将父类中的字段一起序列化，那么父类也必须实现Serializable接口！
	 */
	@Test
	public  void testSerializeSuperFileds() throws Exception {
		User user = new User();
		user.setAge(10);
		user.setName("alice");
		
		// 序列化
		byte[] bytes = serializer.serialize(user);
		
		User user2 = serializer.deSerialize(bytes, User.class);
		System.out.println(user2);
		
	}
	
	
	/**
	 * 对象序列化保存规则：
	 * write多个对象时，序列化文件中的内容并不是简单的成倍数增加，而是按增量方式写入数据。
	 * 这样做的好处是：避免重复数据的写入，减小了序列化文件的大小，更有利于网络传输的效率。
	 * 
	 */
	@Test
	public void testObjectWriteRule() throws Exception {
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("target/person.dat")));
		
		Person person = new Person();
		person.setName("mic");
		person.setAge(18);
		person.setPassword("mic18");
		
		// 第1次写入
		oos.writeObject(person);
		oos.flush();
		System.out.println("第1次序列化之后，文件大小：" + new File("target/person.dat").length());
		
		// 第2次写入
		// 第一次写入后，再修改属性值，在反序列化之后是无法获取到的。为什么呢？
		// 因为同一个对象多次写入，从第二次写入开始，将只通过指针引用第一个对象（占5个字节的大小）--- 提升效率考虑。
		person.setAge(32); 
		oos.writeObject(person);
		oos.flush();
		System.out.println("第2次序列化之后，文件大小：" + new File("target/person.dat").length());
		
		// 第3次写入
		Person p3 = new Person();
		p3.setName("james");
		p3.setPassword("james18");
		oos.writeObject(p3); 
		oos.flush();
		System.out.println("第3次序列化之后，文件大小：" + new File("target/person.dat").length());
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("target/person.dat")));
		Object o1 = ois.readObject();
		Object o2 = ois.readObject();
		Object o3 = ois.readObject();
		System.out.println("o1: " + o1);
		System.out.println("o2: " + o2);
		System.out.println("o3: " + o3);
		
		System.out.println("o1 == o2: " + (o1 == o2)); // true 因为第1次和第2次write的是同一个对象，虽然属性值发生了变化，但最终反序列化回来的对象是相同的
		System.out.println("o1 == o3: " + (o1 == o3)); // false o1和o3是在写入时就是不同的对象
		
		// EOFException
//		System.out.println(ois.readObject());
		
		oos.close();
		ois.close();
	}
	
}
