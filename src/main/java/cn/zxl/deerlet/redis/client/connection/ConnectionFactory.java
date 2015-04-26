/**
 * 
 */
package cn.zxl.deerlet.redis.client.connection;

import cn.zxl.deerlet.redis.client.strategy.LoadBalanceStrategy;

/**
 * @author zuoxiaolong
 *
 */
public interface ConnectionFactory {
	
	public Connection createConnection(String key);
	
	public LoadBalanceStrategy<ConnectionPool> getLoadBalanceStrategy();
	
}
