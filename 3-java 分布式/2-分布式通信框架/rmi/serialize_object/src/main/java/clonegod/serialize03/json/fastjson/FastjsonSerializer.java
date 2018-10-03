package clonegod.serialize03.json.fastjson;

import com.alibaba.fastjson.JSON;

import clonegod.serialize.ISerializer;

public class FastjsonSerializer implements ISerializer {

	@Override
	public <T> byte[] serialize(T obj) {
		return JSON.toJSONString(obj).getBytes();
	}

	@Override
	public <T> T deSerialize(byte[] data, Class<T> clazz) {
		return JSON.parseObject(new String(data), clazz);
	}

}
