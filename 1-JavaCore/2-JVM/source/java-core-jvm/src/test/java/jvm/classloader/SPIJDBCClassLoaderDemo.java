package jvm.classloader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SPIJDBCClassLoaderDemo {
	
	public static void main(String[] args) {
		try {
			// 告诉类加载器加载 com.mysql.jdbc.Driver
			Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
			// sun.misc.Launcher$AppClassLoader@2a139a55
			System.out.println(clazz.getClassLoader());
			
			// 从DriverManager获取Connecton
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "alice", "alice123");
			// com.mysql.jdbc.JDBC4Connection@5fa7e7ff
			System.out.println(conn);
			
			// return null if this class was loaded by the bootstrap class loader.
			// null -> 说明 DriverManager是被Bootstrap classLoader 加载的
			System.out.println(DriverManager.class.getClassLoader()); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
