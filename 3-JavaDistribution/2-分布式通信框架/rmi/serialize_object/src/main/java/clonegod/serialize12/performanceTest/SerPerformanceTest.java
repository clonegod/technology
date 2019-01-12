package clonegod.serialize12.performanceTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSON;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import clonegod.model.Person;

/**
 * 测试各种序列化框架的性能
 * 
 */
public class SerPerformanceTest {

	private static final int COUNT = 10000;

	// 初始化
	private static Person init() {
		Person person = new Person();
		person.setName("mic");
		person.setAge(18);
		return person;
	}

	public static void main(String[] args) throws IOException {
		excuteWithJackson();

		excuteWithFastJson();

		excuteWithProtoBuf();

		excuteWithHessian();
	}

	/**
	 * jackson 速度慢，序列化生成的字节数较少
	 */
	private static void excuteWithJackson() throws IOException {
		Person person = init();

		ObjectMapper mapper = new ObjectMapper();
		byte[] writeBytes = null;
		Long start = System.currentTimeMillis();
		for (int i = 0; i < COUNT; i++) {
			writeBytes = mapper.writeValueAsBytes(person);
		}
		System.out
				.println("Jackson序列化：" + (System.currentTimeMillis() - start) + "ms : " + "总大小->" + writeBytes.length);

		Person person1 = mapper.readValue(writeBytes, Person.class);
		System.out.println(person1);
	}

	/**
	 * fastjson 速度很慢，序列化生成的字节数比较少
	 */
	private static void excuteWithFastJson() throws IOException {
		Person person = init();
		String text = null;
		Long start = System.currentTimeMillis();
		for (int i = 0; i < COUNT; i++) {
			text = JSON.toJSONString(person);
		}
		System.out.println(
				"fastjson序列化：" + (System.currentTimeMillis() - start) + "ms : " + "总大小->" + text.getBytes().length);

		Person person1 = JSON.parseObject(text, Person.class);
		System.out.println(person1);
	}

	/**
	 * protobuf 序列化速度很快，而且序列化生成的字节数很少，对于网络传输而言有很大优势 --- 优秀！
	 */
	private static void excuteWithProtoBuf() throws IOException {
		Person person = init();
		Codec<Person> personCodec = ProtobufProxy.create(Person.class, false);

		Long start = System.currentTimeMillis();
		byte[] bytes = null;
		for (int i = 0; i < COUNT; i++) {
			bytes = personCodec.encode(person);
		}
		System.out.println("protobuf序列化：" + (System.currentTimeMillis() - start) + "ms : " + "总大小->" + bytes.length);

		Person person1 = personCodec.decode(bytes);
		System.out.println(person1);
	}

	/**
	 * hession 序列化速度最快，序列化生成的字节数很多
	 */
	private static void excuteWithHessian() throws IOException {
		Person person = init();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HessianOutput ho = new HessianOutput(os);
		Long start = System.currentTimeMillis();
		for (int i = 0; i < COUNT; i++) {
			ho.writeObject(person);
			if (i == 0) {
				System.out.println("Hessian序列化：first object size=" + os.toByteArray().length);
			}
		}
		System.out.println(
				"Hessian序列化：" + (System.currentTimeMillis() - start) + "ms : " + "总大小->" + os.toByteArray().length);

		HessianInput hi = new HessianInput(new ByteArrayInputStream(os.toByteArray()));
		Person person1 = (Person) hi.readObject();
		System.out.println(person1);
	}
}
