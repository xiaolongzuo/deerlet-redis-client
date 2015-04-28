/**
 * 
 */
package cn.zxl.deerlet.redis.client.config;

/**
 *
 * Server工厂类，用于创建相应的server
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 * @see cn.zxl.deerlet.redis.client.config.Server
 */
public abstract class Servers {

	public static Server newServer(String host,int port) {
		return new DefaultServer(host, port);
	}
	
}
