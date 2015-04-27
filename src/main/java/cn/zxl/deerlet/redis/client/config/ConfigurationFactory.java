/**
 * 
 */
package cn.zxl.deerlet.redis.client.config;

/**
 *
 * 配置工厂，用于创建配置。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 * @see cn.zxl.deerlet.redis.client.config.Configuration
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
