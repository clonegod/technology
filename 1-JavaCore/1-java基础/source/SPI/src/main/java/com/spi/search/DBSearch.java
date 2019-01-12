package com.spi.search;
public class DBSearch implements Search {
    /**
     * 基于数据库的查找
     *
     */
    @Override
    public void search(String key) {
        // 业务逻辑
        System.out.println("基于数据库的查找操作！");
    }
}