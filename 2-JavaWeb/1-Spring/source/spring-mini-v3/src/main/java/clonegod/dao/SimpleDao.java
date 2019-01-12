package clonegod.dao;

import java.util.UUID;

import clonegod.spring.framework.annotation.Repository;

@Repository
public class SimpleDao {
	
	public String getToken() {
		return UUID.randomUUID().toString();
	}
	
}
