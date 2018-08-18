package demos

// return last line as value to java GroovyScriptEngine

date = new Date() // java.util.Date
println "date="+date

name = 'alice'

def helloStatement = Math.random() > 0.5 ? "Hello: ${name}" :  "Hello: ${name}, it's a Groovy baby, yeah!"

print helloStatement