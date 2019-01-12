package clonegod.spring.framework.context.support;

/**
 * 提供设置与获取配置文件的接口
 */
public abstract class AbstractRefreshableConfigApplicationContext extends AbstractRefreshableApplicationContext {

	private String[] configLocations;
	
	public void setConfigLocation(String locations) {
		this.configLocations = locations.split(",");
	}
	
	/**
	 * Set the config locations for this application context.
	 * <p>If not set, the implementation may use a default as appropriate.
	 */
	public void setConfigLocations(String... locations) {
		this.configLocations = locations;
	}
	/**
	 * Return an array of resource locations, referring to the XML bean definition
	 * files that this context should be built with. Can also include location
	 * patterns, which will get resolved via a ResourcePatternResolver.
	 * <p>The default implementation returns {@code null}. Subclasses can override
	 * this to provide a set of resource locations to load bean definitions from.
	 * @return an array of resource locations, or {@code null} if none
	 * @see #getResources
	 * @see #getResourcePatternResolver
	 */
	protected String[] getConfigLocations() {
		return (this.configLocations != null ? this.configLocations : getDefaultConfigLocations());
	}

	/**
	 * Return the default config locations to use, for the case where no
	 * explicit config locations have been specified.
	 * <p>The default implementation returns {@code null},
	 * requiring explicit config locations.
	 * @return an array of default config locations, if any
	 * @see #setConfigLocations
	 */
	protected String[] getDefaultConfigLocations() {
		return null;
	}
}
