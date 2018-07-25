package clonegod.utils.jsonpath;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

@SuppressWarnings("all")
public class TestJsonPath {

	private String json;

	@Before
	public void setUp() {
		try {
			json = IOUtils.toString(TestJsonPath.class.getResource("book.txt"), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 匹配一个单值
	 */
	@Test
	public void testFindOnlyOne() throws IOException {
		Object str = JsonPath.read(json, "$.store.book[0].price");
		System.out.println(str);
	}

	@Test
	public void testJsonContext() throws Exception {
		ReadContext ctx = JsonPath.parse(json);

		// book 包含 isbn
		List<String> authorsOfBooksWithISBN = ctx.read("$.store.book[?(@.isbn)].author");
		authorsOfBooksWithISBN.forEach(System.out::println);

		// book 的 price > 10
		List<Map<String, Object>> expensiveBooks = 
				JsonPath.using(Configuration.defaultConfiguration()).parse(json)
				.read("$.store.book[?(@.price > 10)]", List.class);
		
		expensiveBooks.forEach(m -> {
			System.out.println(m);
		});
	}

	/**
	 * 读取json的一种写法 支持逻辑表达式，&&和||
	 */
	@Test
	public void testAndOr() throws Exception {
		List<Map<String, Object>> books1 = JsonPath.parse(json)
				.read("$.store.book[?(@.price < 10 && @.category == 'fiction')]");
		books1.forEach(m -> {
			System.out.println(m);
		});

		System.out.println("\n\n");

		List<Map<String, Object>> books2 = JsonPath.parse(json)
				.read("$.store.book[?(@.category == 'reference' || @.price > 10)]");

		books2.forEach(m -> {
			System.out.println(m);
		});

		// Like this:
		// JsonPath.read(json, "$..farePrices[?(@.priceType == 'SalePrice' &&
		// @.passengerType == 'ADULT')].amount");
	}

	/**
	 * 匹配多个值，返回List
	 */
	@Test
	public void testExtractList() throws IOException {
		// The authors of all books：获取json中store下book下的所有author值
		printList(json, "$.store.book[*].author");

		// All authors：获取所有json中所有author的值
		printList(json, "$..author");

		// All things, both books and bicycles
		printList(json, "$.store.*");

		// The price of everything：获取json中store下所有price的值
		printList(json, "$.store..price");

		// The third book：获取json中book数组的第3个值
		printList(json, "$..book[2]");

		// The first two books：获取json中book数组的第1和第2两个个值
		printList(json, "$..book[0,1]");

		// All books from index 0 (inclusive) until index 2
		// (exclusive)：获取json中book数组的前两个区间值
		printList(json, "$..book[:2]");

		// All books from index 1 (inclusive) until index 2
		// (exclusive)：获取json中book数组的第2个值
		printList(json, "$..book[1:2]");

		// Last two books：获取json中book数组的最后两个值
		printList(json, "$..book[-2:]");

		// Book number two from tail：获取json中book数组的第3个到最后一个的区间值
		printList(json, "$..book[2:]");

		// All books with an ISBN number：获取json中book数组中包含isbn的所有值
		printList(json, "$..book[?(@.isbn)]");

		// All books in store cheaper than 10：获取json中book数组中price<10的所有值
		printList(json, "$.store.book[?(@.price < 10)]");

		// All books in store that are not
		// "expensive"：获取json中book数组中price<=expensive的所有值
		printList(json, "$..book[?(@.price <= $['expensive'])]");

		// All books matching regex (ignore
		// case)：获取json中book数组中的作者以REES结尾的所有值（REES不区分大小写）
		printList(json, "$..book[?(@.author =~ /.*REES/i)]");

		// Give me every thing：逐层列出json中的所有值，层级由外到内
		printList(json, "$..*");

	}

	private void printList(String json, String jsonpath) {
		List<String> list = JsonPath.read(json, jsonpath);
		System.out.println(jsonpath + " => \t" + Arrays.toString(list.toArray()));
	}
}
