/**
 * 
 */
package cn.zxl.deerlet.redis.client.config;

/**
 * @author zuoxiaolong
 *
 */
public abstract class Servers {

	public static Server newServer(String host,int port) {
		return new DefaultServer(host, port);
	}
	
}
