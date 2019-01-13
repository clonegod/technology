package com.mybatis.sample.blog.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sample2ConnectionFactory {
	
	static Logger logger = LoggerFactory.getLogger(Sample2ConnectionFactory.class);
	
    private static SqlSessionFactory sessionFactory;
    
    static{
    	Reader reader = null;
        try{
        	String resource = "mybatis-config.xml"; 
        	reader = Resources.getResourceAsReader(resource);
        	sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        	logger.info("sessionFactory 创建成功");
        }catch(Exception e){
            e.printStackTrace();
        } finally {
        	try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    public static SqlSessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
