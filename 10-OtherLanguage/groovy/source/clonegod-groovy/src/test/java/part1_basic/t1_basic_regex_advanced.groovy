package part1_basic

// ===================================================================
def text = """
日期 	类型 	金额  其它
2018-03-28 汇缴  800 xxxxxxxx
2018-02-28 汇缴  700.0
2018-01-28 汇缴  600.00;
"""

// (?m) ------ 多行模式，Use multiline mode. ^ and $ match the beginning and end of a line, instead of the beginning and end of a string.
println "------提取每行的子串"
def m = text =~ /(?m)(^\d{4}-\d{2}-\d{2}\s+汇缴\s+\d+(?:\.\d{1,2})?)/
while(m.find()) {
	println m.group(1)
}

// (?s) ------ 单行模式。makes the dot match all characters, including line breaks
println "------提取跨行子串"
def m2 = text =~ /(?s)(\d{4}-\d{2}-\d{2}.*?;)/
if(m2.find()) {
	println m2.group(1)
}


// ===================================================================
def sometext = """
StringStart 1111111111111111111
0123465798 BlockStart 666666666 
77777777 BlockEnd 88888888
00000000000000000000000000000000000000
BlockStart 2222222222222 
3333333333 444444 BlockEnd 55555555
99999999999999

"""

matcher = sometext =~ /(?ms)(^BlockStart.*?BlockEnd)/
while(matcher.find()) {
	println matcher.group(1)
}


// ================================================================ //
println "-----------------------"
def s = """
END IF;

EXECUTE IMMEDIATE ' CREATE INDEX #idx1
      ON somename ( row_id,
                           something)';

   IF v_sys_error <> 0 THEN
      GOTO SQL_ERROR;

   END IF;
"""

def expect = """
EXECUTE IMMEDIATE ' CREATE INDEX #idx1
      ON somename ( row_id,
                           something)';
""".trim()

def exe = s =~ /(?ms)(EXECUTE.*?;)/

assert expect == exe[0][1]

println exe[0][1]

