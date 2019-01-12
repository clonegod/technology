package clonegod.spring.framework.beans.factory.support;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import clonegod.spring.framework.beans.factory.ObjectFactory;

public class DefaultSingletonBeanRegistry {
	/**
	 * Internal marker for a null singleton object:
	 * used as marker value for concurrent Maps (which don't support null values).
	 */
	protected static final Object NULL_OBJECT = new Object();
	
	/** Cache of singleton objects: bean name --> bean instance */
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);
	
	
	/** Set of registered singletons, containing the bean names in registration order */
	private final Set<String> registeredSingletons = new LinkedHashSet<String>(256);
	
	
	public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException {
		synchronized (this.singletonObjects) {
			Object oldObject = this.singletonObjects.get(beanName);
			if (oldObject != null) {
				throw new IllegalStateException("Could not register object [" + singletonObject +
						"] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
			}
			addSingleton(beanName, singletonObject);
		}
	}

	/**
	 * Add the given singleton object to the singleton cache of this factory.
	 * <p>To be called for eager registration of singletons.
	 * @param beanName the name of the bean
	 * @param singletonObject the singleton object
	 */
	protected void addSingleton(String beanName, Object singletonObject) {
		synchronized (this.singletonObjects) {
			this.singletonObjects.put(beanName, (singletonObject != null ? singletonObject : NULL_OBJECT));
			this.registeredSingletons.add(beanName);
			// 兼容接口注入的情况，否则按接口类型注入将获取不到对于的实例
			for(Class<?> clazz : singletonObject.getClass().getInterfaces()) {
				this.singletonObjects.put(clazz.getSimpleName(), singletonObject);
			}
		}
	}

	/**
	 * Return the (raw) singleton object registered under the given name,
	 * creating and registering a new one if none registered yet.
	 * @param beanName the name of the bean
	 */
	public Object getSingleton(String beanName) {
		synchronized (this.singletonObjects) {
			Object singletonObject = this.singletonObjects.get(beanName);
			return (singletonObject != NULL_OBJECT ? singletonObject : null);
		}
	}
	
	public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
		synchronized (this.singletonObjects) {
			Object singletonObject = this.singletonObjects.get(beanName);
			if (singletonObject == null) {
				boolean newSingleton = false;
				try {
					singletonObject = singletonFactory.getObject();
					newSingleton = true;
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				if (newSingleton) {
					addSingleton(beanName, singletonObject);
				}
			}
			return (singletonObject != NULL_OBJECT ? singletonObject : null);
		}
	}

	/**
	 * Remove the bean with the given name from the singleton cache of this factory,
	 * to be able to clean up eager registration of a singleton if creation failed.
	 * @param beanName the name of the bean
	 * @see #getSingletonMutex()
	 */
	protected void removeSingleton(String beanName) {
		synchronized (this.singletonObjects) {
			this.singletonObjects.remove(beanName);
			this.registeredSingletons.remove(beanName);
		}
	}

	/**
	 * Destroy the given bean. Delegates to {@code destroyBean}
	 * if a corresponding disposable bean instance is found.
	 * @param beanName the name of the bean
	 * @see #destroyBean
	 */
	public void destroySingleton(String beanName) {
		// Remove a registered singleton of the given name, if any.
		removeSingleton(beanName);
		
		// Destroy the corresponding DisposableBean instance.
	}
	
	public void destroySingletons() {
		synchronized (this.singletonObjects) {
			this.singletonObjects.clear();
			this.registeredSingletons.clear();
		}
	}
}
