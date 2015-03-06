package cn.zxl.deerlet.redis.client.factory;

import java.io.IOException;
import java.util.Properties;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.connection.ConnectionImpl;

/**
 * 
 * 连接工厂类。该类读取默认位置的配置文件，并由此产生新的连接。ConnectionPool的连接由该类产生。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:42:44
 *
 */
public class ConnectionFactory {

	private static final String configFile = "deerlet.properties";

	private static final String address;

	private static final int port;

	static {
		Properties properties = new Properties();
		try {
			properties.load(ClassLoader.getSystemResourceAsStream(configFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		address = properties.getProperty("address");
		port = Integer.valueOf(properties.getProperty("port"));
	}

	public static Connection getConnection() {
		try {
			return new ConnectionImpl(address, port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
