/**
 * 
 */
package cn.zxl.deerlet.redis.client.connection;

import cn.zxl.deerlet.redis.client.strategy.LoadBalanceStrategy;

/**
 *
 * 该接口定义了连接工厂需要提供的功能
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 * @see cn.zxl.deerlet.redis.client.connection.Connection
 */
public interface ConnectionFactory {
	
	public Connection createConnection(String key);
	
	public LoadBalanceStrategy<ConnectionPool> getLoadBalanceStrategy();
	
}
