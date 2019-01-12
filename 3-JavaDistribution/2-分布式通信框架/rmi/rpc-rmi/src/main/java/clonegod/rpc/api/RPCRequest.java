package clonegod.rpc.api;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 封装远程方法调用的相关信息
 * 
 */
public class RPCRequest implements Serializable {
	private static final long serialVersionUID = -3773380997114912318L;
	
	private String className; // 服务接口的名称
	private String method; // 要调用的方法
	private Object[] params; // 方法的参数列表
	private String version;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "RPCRequest [className=" + className + ", method=" + method + ", params=" + Arrays.toString(params)
				+ ", version=" + version + "]";
	}
	
}
