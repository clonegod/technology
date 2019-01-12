package part1_basic

// 属性访问
def groovyBook = new Book()
//通过显示的方法调用来使用属性 
groovyBook.setTitle('Groovy conquers the world') 
assert groovyBook.getTitle() == 'Groovy conquers the world'

//通过groovy的快捷方式来使用属性 
//注意groovyBook.title不是类属性的直接访问，它是访问该属性访问者方法的快捷方式。
groovyBook.title = 'Groovy in Action' 
assert groovyBook.title == 'Groovy in Action'

