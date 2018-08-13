package clonegod.dao;

import java.util.UUID;

import clonegod.spring.framework.annotation.Repository;

@Repository
public class SimpleDaoImpl implements SimpleDao {
	
	public String getToken() {
		return UUID.randomUUID().toString();
	}
	
}
