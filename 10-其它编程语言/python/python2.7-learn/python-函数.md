定义函数
    在Python中，定义一个函数要使用def语句，依次写出函数名、括号、括号中的参数和冒号:，然后，在缩进块中编写函数体，函数的返回值用return语句返回。
    如果没有return语句，函数执行完毕后也会返回结果，只是结果为None。
    return None可以简写为return。
    
空函数
    pass可以用来作为占位符，比如现在还没想好怎么写函数的代码，就可以先放一个pass，让代码能运行起来
    def nop():
        pass
        
参数检查
    def my_abs(x):
        if not isinstance(x, (int, float)):
            raise TypeError('bad operand type')
        if x >= 0:
            return x
        else:
            return -x   
    
返回多个值
    import math

    def move(x, y, step, angle=0):
        nx = x + step * math.cos(angle)
        ny = y - step * math.sin(angle)
        return nx, ny        
    
    # 返回值是一个tuple
    >>> x, y = move(100, 100, 60, math.pi / 6)
    >>> print x, y 


函数的参数
    对于函数的调用者来说，只需要知道如何传递正确的参数，以及函数将返回什么样的值就够了
    Python的函数具有非常灵活的参数形态，既可以实现简单的调用，又可以传入非常复杂的参数。

必选参数 - 函数运行所必须的参数
    def run(task):
        process(task)
        return '100'
    
默认参数 - 降低了函数调用的难度。无论是简单调用还是复杂调用，函数只需要定义一个。
    def power(x, n=2):
        s = 1
        while n > 0:
            n = n - 1
            s = s * x
        return s

    def enroll(name, gender, age=6, city='Beijing'):
        print 'name:', name
        print 'gender:', gender
        print 'age:', age
        print 'city:', city
        
    按顺序提供默认参数
        enroll('Bob', 'M', 7)
    
    当不按顺序提供部分默认参数时，需要把参数名写上。
        enroll('Adam', 'M', city='Tianjin')
        

    Python函数在定义的时候，默认参数L的值就被计算出来了，默认参数必须指向不变对象！
    def add_end(L=None):
        if L is None:
            L = []
        L.append('END')
        return L
    

        

可变参数 - 传入的参数个数是可变的，可以是1个、2个到任意个，还可以是0个。
    def calc(*numbers):
        sum = 0
        for n in numbers:
            sum = sum + n * n
        return sum    

    Python允许你在list或tuple前面加一个*号，把list或tuple的元素变成可变参数传进去：
    可变参数在函数调用时自动组装为一个tuple
    >>> nums = [1, 2, 3]
    >>> calc(*nums)
    14

    
关键字参数
    关键字参数允许你传入0个或任意个含参数名的参数，这些关键字参数在函数内部自动组装为一个dict。

    def person(name, age, **kw):
        print 'name:', name, 'age:', age, 'other:', kw
    
    可以只传入必选参数：
        >>> person('Michael', 30)
        name: Michael age: 30 other: {}
    
    可以传入任意个数的关键字参数：
        >>> person('Bob', 35, city='Beijing')
        name: Bob age: 35 other: {'city': 'Beijing'}
        
        >>> person('Adam', 45, gender='M', job='Engineer')
        name: Adam age: 45 other: {'gender': 'M', 'job': 'Engineer'}
        
        >>> kw = {'city': 'Beijing', 'job': 'Engineer'}
        >>> person('Jack', 24, **kw)
        name: Jack age: 24 other: {'city': 'Beijing', 'job': 'Engineer'}

    
参数组合
    对于任意函数，都可以通过类似func(*args, **kw)的形式调用它，无论它的参数是如何定义的。
    
    参数定义的顺序必须是：必选参数、默认参数、可变参数和关键字参数。

    def func(a, b, c=0, *args, **kw):
        print 'a =', a, 'b =', b, 'c =', c, 'args =', args, 'kw =', kw

    >>> func(1, 2, 3, 'a', 'b', x=99)
    a = 1 b = 2 c = 3 args = ('a', 'b') kw = {'x': 99}

    最神奇的是通过一个tuple和dict，你也可以调用该函数：
    >>> args = (1, 2, 3, 4)
    >>> kw = {'x': 99}
    >>> func(*args, **kw)
    a = 1 b = 2 c = 3 args = (4,) kw = {'x': 99}


接收命令行参数
    from sys import argv
    script,arglist = argv
    firstArg = arglist[0]
