package json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.alibaba.fastjson.serializer.BeforeFilter;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class TestFastJson {
	/**
		 * Java Bean序列化为JSON字符串、JSON字符串反序列化到JavaBean
		 */
		@Test
		public void testBean2Json() {
			
			Person alice = new Person(1, "alice", 10);
			
			alice.setAddresses(Arrays.asList(new Address("北京","朝阳","大望路"), new Address("四川","洪雅","洪州大道")));
			
			String json = JSON.toJSONString(alice); // 对象转json
			System.out.println(json);
			System.out.println(JSON.toJSONString(alice, true));
			
			Person person = JSON.parseObject(json, Person.class); // json转单个对象
			System.out.println(person);
		}
		
		/**
		 * List 集合转 JSON字符串
		 */
		@Test
		public void testBeanList2Json() {
			
			Person alice = new Person(1, "alice", 10);
			Person bob = new Person(2, "bob", 20);
			alice.setAddresses(Arrays.asList(new Address("北京","朝阳","大望路"), new Address("四川","洪雅","洪州大道")));
			bob.setAddresses(Arrays.asList(new Address("北京1","朝阳","大望路"), new Address("四川1","洪雅","洪州大道")));
			
			List<Person> personList = new ArrayList<Person>();
			personList.add(alice);
			personList.add(bob);
			
			String json = JSON.toJSONString(personList); // list 转 json
			System.out.println(json);
			
			List<Person> persons = JSON.parseArray(json, Person.class); // json 转 list
			persons.forEach(System.out::println);
		}
		
		
		/**
		 * Map 转JSON字符串 - 泛型反序列化 TypeReference
		 */
		@Test
		public void testBeanMap2Json() {
			Person alice = new Person(1, "alice", 10);
			Person bob = new Person(2, "bob", 20);
			alice.setAddresses(Arrays.asList(new Address("北京","朝阳","大望路"), new Address("四川","洪雅","洪州大道")));
			bob.setAddresses(Arrays.asList(new Address("北京1","朝阳","大望路"), new Address("四川1","洪雅","洪州大道")));
			
			Map<String, Person> persons = new HashMap<>();
			persons.put(alice.getName(), alice);
			persons.put(bob.getName(), bob);
			
			String json = JSON.toJSONString(persons, true);
			System.out.println(json);
			
			// 使用TypeReference将返回结果泛型化，避免之后进行强制类型转换
			Map<String, Person> personMap = JSON.parseObject(json, new TypeReference<Map<String, Person>>(){});
			personMap.forEach((k,v) -> {
				System.out.println(k+":"+v);
			});
		}
		
		/**
		 * 可定制序列化的特征行为
		 */
		@Test
		public void testJsonFeature() {
			Person alice = new Person(1, "alice", 10);
			
			String json = JSON.toJSONString(alice, 
								SerializerFeature.QuoteFieldNames, 
								SerializerFeature.UseSingleQuotes, 
								SerializerFeature.PrettyFormat, 
								SerializerFeature.DisableCircularReferenceDetect);
			System.out.println(json);
		}
		
		/**
		 * SerializeConfig 配置Date类型输出格式
		 */
		@Test
		public void testConfigFastJson() {
			SerializeConfig serCfg = new SerializeConfig(); 
			serCfg.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
			
			Person person = new Person(10, "Alice", 20);
			String json = JSON.toJSONString(person, serCfg, SerializerFeature.PrettyFormat);
			System.out.println(json);
			
		}
		
		/**
		 * 序列化时过滤字段，修改字段
		 */
		@Test
		public void testSerFilter() {
			SerializeConfig serCfg = new SerializeConfig(); 
			serCfg.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
			
			Person alice = new Person(1, "alice", 10);
			
			// 过滤key
			PropertyFilter propFilter = new PropertyFilter() {
				@Override
				public boolean apply(Object object, String propertyName, Object propertyValue) {
					if(propertyName.matches("id")) {
						return false; // false to be filtered out 
					}
					return true;  // true if the property will be included
				}
			};
			
			// 重命名key 
			NameFilter nameFilter = new NameFilter() {
				@Override
				public String process(Object object, String propertyName, Object propertyValue) {
					if(propertyName.equals("birth")) {
						return "birth-renamed";
					}
					return propertyName;
				}
			};
			
			// 前置添加内容
			BeforeFilter beforeFilter = new BeforeFilter() {
				@Override
				public void writeBefore(Object bean) {
					super.writeKeyValue("start", new Date());
				}
			}; 
			
			// 后置添加内容
			AfterFilter afterFilter = new AfterFilter() {
				@Override
				public void writeAfter(Object bean) {
					super.writeKeyValue("end", new Date());
				}
			}; 
			
			String json = JSON.toJSONString(alice, serCfg, new SerializeFilter[] {nameFilter, beforeFilter, afterFilter, propFilter}, SerializerFeature.PrettyFormat);
			
			System.out.println(json);
		}
}

