package com.hotchpotch.dao.config;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.github.pagehelper.PageInterceptor;

import java.util.Properties;

import javax.sql.DataSource;

@Configuration
public class MybatisConfig {
	
    @Bean(name="sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperXmlResource = resolver.getResources("classpath*:mapper/*Mapper.xml");
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(mapperXmlResource);
        
        // 设置MyBatis分页插件
        PageInterceptor pageInterceptor = this.initPageInterceptor();
        sqlSessionFactory.setPlugins(new Interceptor[]{pageInterceptor});
        
        return sqlSessionFactory;
    }

    private PageInterceptor initPageInterceptor() {
    	 PageInterceptor pageInterceptor = new PageInterceptor();
         Properties properties = new Properties();
         properties.setProperty("helperDialect", "mysql");
         properties.setProperty("offsetAsPageNum", "true");
         properties.setProperty("rowBoundsWithCount", "true");
         pageInterceptor.setProperties(properties);
         return pageInterceptor;
	}

	@Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setBasePackage("com.hotchpotch.dao");
        msc.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return msc;
    }
    
    @Bean
    public SqlSessionTemplate sqlSeesionTemplate(SqlSessionFactoryBean sqlSessionFactory) {
    	try {
			SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory.getObject());
			return sqlSessionTemplate;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	
    }
}
