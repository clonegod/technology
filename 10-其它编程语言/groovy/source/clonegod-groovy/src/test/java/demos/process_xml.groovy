package demos
// Groovy的xml处理方式
def customers = new XmlSlurper().parse(new File('src/test/resources','data/customers.xml')) 

// 使用GString对String进行功能增强
for (customer in customers.corporate.customer) {
	println "${customer.@name} works for ${customer.@company}"
}

