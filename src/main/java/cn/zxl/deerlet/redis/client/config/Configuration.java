package cn.zxl.deerlet.redis.client.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * 
 * 该类默认加载classpath下的deerlet.properties文件。可以自己指定文件路径，只能基于classpath。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:42:44
 *
 */
public class Configuration {
	
	private static final String DEFAULT_CONFIG_FILE = "deerlet.properties";

	private String address;
	
	private List<Server> serverList;
	
	private List<Server> unmodifiableServerList;
	
	private String configFile;
	
	private Properties properties;
	
	public Configuration(String configFile) {
		if (configFile == null) {
			configFile = DEFAULT_CONFIG_FILE;
		}
		this.configFile = configFile;
		reload();
	}
	
	public List<Server> getServerList() {
		return unmodifiableServerList;
	}
	
	public void reload() {
		loadProperties();
		loadServers(properties);
	}
	
	public String getString(String key) {
		return properties.getProperty(key);
	}
	
	public String getString(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	public Integer getInteger(String key) {
		return Integer.valueOf(getString(key));
	}
	
	public Integer getInteger(String key, Integer defaultValue) {
		return Integer.valueOf(getString(key, String.valueOf(defaultValue)));
	}
	
	protected void loadProperties() {
		properties = new Properties();
		try {
			properties.load(ClassLoader.getSystemResourceAsStream(configFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void loadServers(Properties properties) {
		address = properties.getProperty("address");
		if (address == null || address.length() ==0) {
			throw new NullPointerException("address is null or empty!");
		}
		serverList = new ArrayList<Server>();
		String[] addresses = address.split(",");
		for (int i = 0; i < addresses.length; i++) {
			serverList.add(Servers.newServer(addresses[i].split(":")[0], Integer.valueOf(addresses[i].split(":")[1])));
		}
		unmodifiableServerList = Collections.unmodifiableList(serverList);
	}
	
	
}
