BASE64
	两个主要作用：
	
1. 信息隐藏：对明文进行转换，变为不具可读性的字符串。没有加密效果---隐藏信息，但非要看也无所谓

2. 传输不可打印字符/特殊符号，如传输文件的二进制序列，将其二进制进行base64编码后传送 --- 仅适用于较小文件，转base64字符串，使用http协议进行简单的传输场景。

原理：
1个字节(1byte) = 8个比特位(8bit)

3个字节可以被base64编码后，变为4个字节。理论上，长度增加1/3

3*8=4*6

经base64编码后的每个字节中，前2个bit位都是0，后6位为有效位
