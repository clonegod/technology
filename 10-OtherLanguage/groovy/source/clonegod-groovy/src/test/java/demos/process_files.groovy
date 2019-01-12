package demos

// 遍历目录下的文件
new File('.').eachFileRecurse { println it }

// 读取文件内容
def number=0
file = new File ('src/test/java/demos', 'Hello.groovy')
println file.absolutePath

file.eachLine { line ->
	number++
	println "$number: $line"
}