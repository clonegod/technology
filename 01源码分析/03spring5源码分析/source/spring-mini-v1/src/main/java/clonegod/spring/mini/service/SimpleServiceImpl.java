package clonegod.spring.mini.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import clonegod.spring.mini.annotation.Service;

@Service
public class SimpleServiceImpl implements SimpleService{

	@Override
	public String doService(String name) {
		return name +", 当前时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}
