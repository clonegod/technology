模块与包
    当一个模块编写完毕，就可以被其他地方引用。比如引用Python内置的模块和来自第三方的模块。
    
    模块 
        - 指的是python脚本文件,一个.py文件就称之为一个模块（Module）。比如app/abc.py模块，它的模块名就是app.abc
        使用模块可以避免函数名和变量名冲突。
        模块可以被其它模块调用。
        代码按功能分离到不同的模块，有利于维护和管理。

    包 
        - 指的是文件目录，要求该目录下必须要有一个__init__.py 
        __init__.py本身就是一个模块，而它的模块名就是它所在的目录名。

        
使用模块
    第1行注释可以让这个hello.py文件直接在Unix/Linux/Mac上运行，
    第2行注释表示.py文件本身使用标准UTF-8编码；
    第4行是一个字符串，表示模块的文档注释，任何模块代码的第一个字符串都被视为模块的文档注释；
    第6行使用__author__变量把作者写进去
    

    #!/usr/bin/env python
    # -*- coding: utf-8 -*-

    ' a test module '

    __author__ = 'Michael Liao'

    import sys  # 导入sys模块

    def test():
        args = sys.argv
        if len(args)==1:
            print 'Hello, world!'
        elif len(args)==2:
            print 'Hello, %s!' % args[1]
        else:
            print 'Too many arguments!'

    # 当在命令行运行该模块文件时，Python解释器把一个特殊变量__name__置为__main__，而如果在其他地方导入该模块，这个if判断将失败。
    if __name__=='__main__':
        test()
        
        
        
别名
   导入模块时，还可以使用别名，这样，可以在运行时根据当前环境选择最合适的模块
    try:
        import cStringIO as StringIO
    except ImportError: # 导入失败会捕获到ImportError
        import StringIO
        
    try:
        import json # python >= 2.6
    except ImportError:
        import simplejson as json # python <= 2.5
    

函数、变量的可访问性问题

    正常的函数或变量名（public），比如：abc，x123，PI 
    特殊用途的变量，比如：__author__，__name__, __doc__
    私有的函数或变量（private），比如：_abc，__abc等；

    # 外部不需要引用的函数全部定义成private，只有外部需要引用的函数才定义为public。
    def _private_1(name):
        return 'Hello, %s' % name

    def _private_2(name):
        return 'Hi, %s' % name

    def greeting(name):
        if len(name) > 3:
            return _private_1(name)
        else:
            return _private_2(name)    

----------------------------------------------------------------------

模块搜索路径
    默认情况下，Python解释器会搜索
        当前目录
        已安装的内置模块
        已安装的第三方模块
        
    搜索路径存放在sys模块的path变量中：
    >>> import sys
    >>> print sys.path

    加载一个模块时，Python会在指定的路径下搜索对应的.py文件，如果找不到，就会报错：        
    ImportError: No module named mymodule

    > 手动在sys.path中追加要搜索的目录：
        import sys
        sys.path.append('C:/Users/Administrator/Desktop/test/scripts')
        import MyUtil
        
    > 通过环境变量PYTHONPATH来设置模块搜索目录，只需要添加你自己的搜索路径。


    导入其它包下的类
        # test/sub1/bootstrap.py
            #!/usr/bin/env python
            # -*- coding: utf-8 -*-

            import sys
            sys.path.append('C:/Users/Administrator/Desktop/test/sub2')
            #from Student import Student
            import Student as StuModule

            def invoke():
                for p in sys.path:
                    print p
                #Student('alice', 100).print_score()
                StuModule.Student('alice', 100).print_score()

                
            if __name__=='__main__':
                invoke()
            
        # test/sub2/Student.py
            #!/usr/bin/env python
            # -*- coding: utf-8 -*-

            class Student(object):

                def __init__(self, name, score):
                    self.name = name
                    self.score = score

                def print_score(self):
                    print '%s: %s' % (self.name, self.score)