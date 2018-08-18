package clonegod.spider.core.page;

import java.io.Serializable;

public class PageWebClientOption implements Serializable {

	private static final long serialVersionUID = 8433023441532191343L;
	
	private boolean downloadImages = false;
	private boolean isRedirectEnabled = true;
	private boolean javaScriptEnabled = false;
	private int webConnectionTimeout = 60_000; // 60s
	
	public boolean isDownloadImages() {
		return downloadImages;
	}
	public void setDownloadImages(boolean downloadImages) {
		this.downloadImages = downloadImages;
	}
	public boolean isRedirectEnabled() {
		return isRedirectEnabled;
	}
	public void setRedirectEnabled(boolean isRedirectEnabled) {
		this.isRedirectEnabled = isRedirectEnabled;
	}
	public boolean isJavaScriptEnabled() {
		return javaScriptEnabled;
	}
	public void setJavaScriptEnabled(boolean javaScriptEnabled) {
		this.javaScriptEnabled = javaScriptEnabled;
	}
	public int getWebConnectionTimeout() {
		return webConnectionTimeout;
	}
	public void setWebConnectionTimeout(int webConnectionTimeout) {
		this.webConnectionTimeout = webConnectionTimeout;
	}
	
	
}
