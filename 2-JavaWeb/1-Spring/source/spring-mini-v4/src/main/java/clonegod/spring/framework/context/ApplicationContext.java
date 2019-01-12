package clonegod.spring.framework.context;

import clonegod.spring.framework.beans.factory.ListableBeanFactory;

public interface ApplicationContext extends ListableBeanFactory {
	/**
	 * Return a friendly name for this context.
	 * @return a display name for this context (never {@code null})
	 */
	String getDisplayName();
	
	
	/**
	 * Return the parent context, or {@code null} if there is no parent
	 * and this is the root of the context hierarchy.
	 * @return the parent context, or {@code null} if there is no parent
	 */
	ApplicationContext getParent();
	
}
