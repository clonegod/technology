package regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test06ZeroWidthAssertionMatch {
	@org.junit.Test
    public void testtt() {
        String str = "regex represents regular expression";
        Pattern p = null;
        Matcher m = null;

        /**
         * 用正则表达式来检测一个字符串中包含某个子串，要表示一个字符串中不包含某个字符或某些字符也很容易，用[^...]形式就可以了。
         * 
         * 要表示一个字符串中不包含某个子串（由字符序列构成）呢？ 
         *     用[^...]这种形式就不行了，这时就要用到（负向）先行断言或后行断言、或同时使用。 
         *  --- 限定 指定字符串的左边或右边 必须 出现或不能出现 某个规则的字符串
         */

        // 零宽：匹配时只进行条件检查，不会占用字符位置，因此是零宽度。
        // 正向：断言能够匹配成功；负向：断言不能匹配成功
        // 先行：紧接位置之后（从左向右）；后行：紧接位置之前（从右向左）

        // 零宽正向先行断言 ------ re的右边，必须出现指定的内容，即re的后面要出现字符g才能被匹配
        p = Pattern.compile("(re(?=g))");
        m = p.matcher(str);
        while(m.find()) {
            System.out.println(m.group(1) + ": " + m.start() + "," + m.end());
        }

        System.out.println("====================================");
        // 零宽负向先行断言 ------ re的右边，不能出现指定内容，即re的后面不能出现g字符串
        p = Pattern.compile("(re(?!g))");
        m = p.matcher(str);
        while(m.find()) {
            System.out.println(m.group(1) + ": " + m.start() + "," + m.end());
        }

        System.out.println("====================================");
        // 零宽正向后行断言: re的左边，必须出现一个字符，即排除掉了以re开头的情况（第一个单词‘regex’将不被匹配）
        p = Pattern.compile("((?<=\\w)re)");
        m = p.matcher(str);
        while(m.find()) {
            System.out.println(m.group(1) + ": " + m.start() + "," + m.end());
        }

        System.out.println("====================================");
        // 零宽负向后行断言: re的左边，不能出现指定内容，即re的前面不能有任何字符（第一个单词‘regex’成功匹配）
        p = Pattern.compile("((?<!.)re)");
        m = p.matcher(str);
        while(m.find()) {
            System.out.println(m.group(1) + ": " + m.start() + "," + m.end());
        }

        // 判断一句话中包含this，但不包含that。
        String string = "note this is the case";
        System.out.println(string.matches("(.(?<!that))*this(.(?!that))*"));
        System.out.println(string.matches("((?<!that).)*this(.(?!that))*"));
    }
}
