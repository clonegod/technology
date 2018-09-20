package com.spi.search;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SpiTest {
 
    public static void main(String[] args) {
    	// 从依赖的jar包的 META-INF/services 目录下，查询Search接口的实现类。
    	// 因此，基于SPI的机制，可以实现依赖不同的jar包，提供不能的实现。
    	// 比如，JDBC 的Driver驱动，mysql与oracle的驱动实现就是不同的，但是它们都是Driver的实现类。
    	
        ServiceLoader<Search> searches = ServiceLoader.load(Search.class);
        Iterator<Search> iterator = searches.iterator();
        while (iterator.hasNext()) {
            Search search1 = iterator.next();
            search1.search("abc");
        }
 
    }
}