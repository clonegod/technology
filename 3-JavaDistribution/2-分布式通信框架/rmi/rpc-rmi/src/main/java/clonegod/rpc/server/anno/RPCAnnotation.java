package clonegod.rpc.server.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RPCAnnotation {
	
	// 对外发布服务的接口
	public Class<?> value();
	
	public String version() default "";
}
