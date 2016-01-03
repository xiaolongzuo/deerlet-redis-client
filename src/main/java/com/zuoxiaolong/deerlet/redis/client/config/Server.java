/**
 * 
 */
package com.zuoxiaolong.deerlet.redis.client.config;

/**
 *
 * Server接口，定义一个redis服务器需要提供的信息
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 */
public interface Server {

	String getHost();
	
	int getPort();
	
}
