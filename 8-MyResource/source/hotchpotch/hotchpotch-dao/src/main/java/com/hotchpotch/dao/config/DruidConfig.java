package com.hotchpotch.dao.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DruidConfig {
    private JdbcProperties jdbcConfig;
	
    @Autowired
	public DruidConfig(JdbcProperties jdbcConfig) {
		this.jdbcConfig = jdbcConfig;
	}

    @Bean(name="dataSource")
    @Primary
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(jdbcConfig.getDriverClass());
        dataSource.setUrl(jdbcConfig.getUrl());
        dataSource.setUsername(jdbcConfig.getUsername());
        dataSource.setPassword(jdbcConfig.getPassword());
        dataSource.setInitialSize(jdbcConfig.getInitialSize());
        dataSource.setMaxActive(jdbcConfig.getMaxActive());
        dataSource.setMinIdle(jdbcConfig.getMinIdle());
        dataSource.setMaxWait(jdbcConfig.getMaxWait());
        dataSource.setTimeBetweenEvictionRunsMillis(jdbcConfig.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(jdbcConfig.getMinEvictableIdleTimeMillis());
        return dataSource;
    }
    
}
