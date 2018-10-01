package rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import rmi.api.ISayHello;

/**
 * 实现类
 * 	1、必须继承 extends UnicastRemoteObject
 * 	2、实现服务接口
 *
 */
public class SyaHelloImpl extends UnicastRemoteObject implements ISayHello {

	private static final long serialVersionUID = -7328697563827984436L;

	
	/**
	 * 必须  throws RemoteException 
	 */
	protected SyaHelloImpl() throws RemoteException {
		super();
	}

	/**
	 * 必须  throws RemoteException 
	 */
	@Override
	public String sayHello(String name) throws RemoteException {
		System.out.println("收到客户端请求，name=" + name);
		return "你好：" + name;
	}

}
