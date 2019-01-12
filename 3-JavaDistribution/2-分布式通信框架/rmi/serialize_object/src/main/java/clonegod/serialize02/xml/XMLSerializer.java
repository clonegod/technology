package clonegod.serialize02.xml;

import com.thoughtworks.xstream.XStream;

import clonegod.serialize.ISerializer;

/**
 * XML序列化
 * 
 */
public class XMLSerializer implements ISerializer {
	
	XStream xStream = new XStream();

	@Override
	public <T> byte[] serialize(T obj) {
		return xStream.toXML(obj).getBytes();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deSerialize(byte[] data, Class<T> clazz) {
		return (T) xStream.fromXML(new String(data));
	}

}
