/**
 * 
 */
package cn.zxl.deerlet.redis.client.config;

/**
 * @author zuoxiaolong
 *
 */
public class ConfigurationFactory {
	
	private static final String DEFAULT_CONFIG_FILE = "deerlet.properties";

	private ConfigurationFactory (){}
	
	public static ConfigurationFactory create() {
		return new ConfigurationFactory();
	}
	
	public Configuration loadConfiguration() {
		return new Configuration(DEFAULT_CONFIG_FILE);
	}
	
	public Configuration loadConfiguration(String configFile) {
		return new Configuration(configFile);
	}
	
}
