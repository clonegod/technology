package clonegod.network.netty.rpc.api;

import java.io.Serializable;
import java.util.Arrays;

public class RPCMsg implements Serializable {
	
	private static final long serialVersionUID = -1919210001780197270L;
	
	private String className;	// 服务名称
	private String methodName;	// 方法名
	private Class<?>[] parameterTypes; // 参数类型
	private Object[] parameterValues; // 参数值
	
	public RPCMsg() {
		super();
	}

	public RPCMsg(String className, String methodName, Class<?>[] parameterTypes, Object[] parameterValues) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.parameterValues = parameterValues;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public Object[] getParameterValues() {
		return parameterValues;
	}

	@Override
	public String toString() {
		return "RPCMsg [className=" + className + ", methodName=" + methodName + ", parameterTypes="
				+ Arrays.toString(parameterTypes) + ", parameterValues=" + Arrays.toString(parameterValues) + "]";
	}

}
