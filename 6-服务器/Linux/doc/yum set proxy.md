# 【yum使用代理的设置】


    如果只是暂时使用代理,在命令行输入下面一条命令:
    export http_proxy="http://210.45.72.XX:808"

    对于长久使用代理的情况:
        yum里面可以单独设置代理
        就是yum源的参数加proxy=“http://ip：PORT”
        即在/etc/yum.conf中加入下面几句.
        proxy=http://210.45.72.XX:808
        proxy_username=username
        proxy_password=password

    另外: 
        /root/.bashrc中加入: 
        export http_proxy="http://username:password@210.45.72.XX:808"
        如果是通过ip或电脑的网卡地址认证,可忽略所有username 和 password .
        注:停止使用代理的时候,要把改过的文件改成原样.尤其是如果使用了export http_proxy="http://210.45.72.XX:808"命令,
        要在命令行输入: unset http_proxy ,去除环境变量.
        因为export http_proxy="http://210.45.72.XX:808"这条命令的实质是把export后面的设置写进环境设置文件中.
        输入env 可以看所有的环境变量设置.
        env >env.txt 可以把环境变量输出到env.txt中,然后打开看也可以.
        unset 变量 可以取消曾经设过的环境变量.