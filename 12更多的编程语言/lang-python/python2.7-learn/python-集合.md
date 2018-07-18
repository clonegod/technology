list和tuple是Python内置的有序集合，一个可变，一个不可变。根据需要来选择使用它们。	
list是一种有序的集合，可以随时添加和删除其中的元素。
	classmates = []	
	增
		classmates.append('Adam')
		classmates.insert(1, 'Jack')
	删
		classmates.pop()
		classmates.pop(1)
	改 
		classmates[1] = 'Sarah'
	查
		classmates[0]
		classmates[-1]
		len(classmates)
	
	
tuple一旦初始化就不能修改
	>>> t = (1,)
	>>> t = (1, 2)

-----------------------------------------------------------------------		
使用dict和set --- 牢记的第一条就是dict的key必须是不可变对象（可变对象的hashcode值不是固定的）


dict全称dictionary，在其他语言中也称为map，使用键-值（key-value）存储，具有极快的查找速度。
	>>> d = {'Michael': 95, 'Bob': 75, 'Tracy': 85}
	>>> d['Michael']
	增
		 d['Adam'] = 67
	删
		 d.pop('Bob')
	改
		 d['Adam'] = 68
	查
		'Thomas' in d		# 判断key是否在dict中存在
		d['Thomas']			
		d.get('Thomas')		# 不存在，返回None
		d.get('Thomas', -1)	# 不存在，返回预设的默认值
		

set和dict类似，也是一组key的集合，但不存储value。由于key不能重复，所以，在set中，没有重复的key。
	要创建一个set，需要提供一个list作为输入集合：
	>>> s = set()
	
	增
		s.add(4)
	删
		s.remove(4)
		s.pop()
	查
		(4 in s) #访问set值就是判断set中是否存在该值

	交集，并集运算
	>>> s1 = set([1, 2, 3])
	>>> s2 = set([2, 3, 4])
	>>> s1 & s2
	set([2, 3])
	>>> s1 | s2
	set([1, 2, 3, 4])