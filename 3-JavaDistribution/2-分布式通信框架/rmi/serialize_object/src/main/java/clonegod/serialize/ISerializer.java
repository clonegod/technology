package clonegod.serialize;

public interface ISerializer {
	
	<T> byte[] serialize(T obj);
	
	<T> T deSerialize(byte[] data, Class<T> clazz);
	
}
