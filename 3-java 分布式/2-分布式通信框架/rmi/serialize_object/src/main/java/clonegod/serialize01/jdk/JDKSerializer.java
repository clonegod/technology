package clonegod.serialize01.jdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import clonegod.serialize.ISerializer;

public class JDKSerializer implements ISerializer {
	
	@Override
	public <T> byte[] serialize(T obj) {
		ObjectOutputStream oos = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(out);
			oos.writeObject(obj);
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("序列化失败", e);
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deSerialize(byte[] data, Class<T> clazz) {
		ObjectInputStream ois = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(in);
			return (T) ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException("反序列化失败", e);
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T deepClone(T object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 8);
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.close();
			
			byte[] data = baos.toByteArray();
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(in);
			
			Object obj = ois.readObject();
			ois.close();
			
			return (T)obj;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
