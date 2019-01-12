package clonegod.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import clonegod.dao.SimpleDao;
import clonegod.spring.framework.annotation.Autowired;
import clonegod.spring.framework.annotation.Service;

@Service
public class SimpleServiceImpl implements SimpleService{
	
	@Autowired
	private SimpleDao simpleDao;

	@Override
	public String doService(String name) {
		return name +", 当前时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
				+ ", token=" + simpleDao.getToken();
	}
}
