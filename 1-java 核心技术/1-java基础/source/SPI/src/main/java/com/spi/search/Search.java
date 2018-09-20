package com.spi.search;

public interface Search {
	
	/**
	 * 定制接口，具体实现类通过SPI机制进行加载
	 */
	void search(String key);
	
}
