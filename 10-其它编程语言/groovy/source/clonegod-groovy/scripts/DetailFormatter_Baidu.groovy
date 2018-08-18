import groovy.json.JsonOutput
import groovy.json.StringEscapeUtils

class DetailFormatter_Baidu {
	def data = """
Recognized: 序号 1 记账日期归集和提取业务类型发生领发生利息额 2017-06-20汇缴4170.000.00 2017-06-30年终结息288.940.00 2017-09-25汇缴4170.000.00 2017-11-06部分提取12800.000.00 2017-12-11汇缴1390.000.00 2017-12-11汇缴1390.000.00 2017-12-11汇缴1390.000.00 2018-01-23汇缴1800.000.00 2018-02-12汇缴1800.000.00 2018-03-09汇缴1800.000.00 共10条记录第111页 提取原因 提取方式 偿还购房贷款本息现金提取 234567co910
"""
	def format(String text) {
		Thread.sleep(2000);
		
		def REGEX_ROW = /(?ms)(\d{4}-\d{2}-\d{2})([\u4E00-\u9FA5]+)(.*?)0\.00\s+/
		
		def recordDateMatcher = data =~ REGEX_ROW
		
		def rowList = []
		
		recordDateMatcher.each {
			def record = [:]
			def exec = it.toString() =~ REGEX_ROW
			record.recordDate 	= exec[0][1]
			record.opType 		= exec[0][2]/* + "========================="*/
			record.amount 		= exec[0][3]
			rowList.push(record)
		}
		
		def rawJson = JsonOutput.toJson(rowList)
		
		StringEscapeUtils.unescapeJava(JsonOutput.prettyPrint(rawJson))
		
	}
	
}
