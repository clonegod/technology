## 序列化-EOFException

http://www.javamadesoeasy.com/2015/09/avoid-objectinputstreamreadobject-from.html

### Avoid ObjectInputStream.readObject() from throwing EOFException at End Of File in java

》》》 问题：

During deserialization process when file is read till end using readObject() in while loop then EOFException is thrown as we saw in DeSerialization program. Java Api doesn’t provide any elegant solution to signify end the file. 

Generally what we could except at EOF(end of file) is null but that doesn’t happen.


》》》 解决方案：

So, we’ll try to address the problem because catching EOFException and interpreting it as EOF is not the elegant solution because sometimes you may fail to detect a normal EOF of a file that has been truncated.

So, lets discuss best possible solution to address the problem >

 	 ● Solution 1) 
	You may persist some count in file during serialization process to find out exactly how many object were actually serialized and simply use for loop in place of while loop in deserialization process.
Or, 

  	● Solution 2) I’ll recommend you this solution, probably the best solution 
      ○ Create a class EofIndicatorClass which implements Serializable interface.
      ○ During serialization >
          ■ Write instance of EofIndicatorClass at EOF during serialization to indicate EOF during deSerialization process.
      ○ During serialization >
          ■ If oin.readObject() returns instanceof EofIndicatorClass that means it's EOF, exit while loop and EOFException will not be thrown.

#
	>>> 定义一个描述文件结束的标记对象
	/*
	 * Class whose instance will be written at EOF during serialization
	 * to indicate EOF during deSerialization process.
	 */
	public class EofIndicatorClass implements Serializable{
	}
	
#	
	>>> Serialize 序列化写入结束标记
	//write instance of EofIndicatorClass at EOF
	oout.writeObject(new EofIndicatorClass());
	
#	
	>>> DeSerialize 反序列化时读取结束标记，可避免EOFException 
	/*
	 *If oin.readObject() returns instanceof EofIndicatorClass that means
	*it's EOF, exit while loop and EOFException will not be thrown.
	 */
	Object obj;
	while(!((obj =  oin.readObject()) instanceof EofIndicatorClass)){
	     System.out.println(obj);
	}