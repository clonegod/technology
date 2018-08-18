package clonegod.spring.framework.beans;

public class BeanWrapperImpl implements BeanWrapper {
	
	Object wrappedObject;
	
	public BeanWrapperImpl(Object wrappedObject) {
		super();
		this.wrappedObject = wrappedObject;
	}

	/**
	 * Set a bean instance to hold, without any unwrapping of {@link java.util.Optional}.
	 * @param object the actual target object
	 * @since 4.3
	 * @see #setWrappedInstance(Object)
	 */
	public void setBeanInstance(Object object) {
		this.wrappedObject = object;
	}
	
	public final Object getWrappedInstance() {
		return this.wrappedObject;
	}

	public final Class<?> getWrappedClass() {
		return (this.wrappedObject != null ? this.wrappedObject.getClass() : null);
	}
}
