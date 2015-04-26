/**
 * 
 */
package cn.zxl.deerlet.redis.client.connection.impl;

import cn.zxl.deerlet.redis.client.config.Configuration;
import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.connection.ConnectionFactory;
import cn.zxl.deerlet.redis.client.connection.ConnectionPool;
import cn.zxl.deerlet.redis.client.strategy.LoadBalanceStrategies;
import cn.zxl.deerlet.redis.client.strategy.LoadBalanceStrategy;

/**
 * @author zuoxiaolong
 *
 */
public class ConnectionFactoryImpl implements ConnectionFactory {
	
	private LoadBalanceStrategy<ConnectionPool> loadBalanceStrategy;

	public ConnectionFactoryImpl(Configuration configuration) {
		loadBalanceStrategy = LoadBalanceStrategies.createLoadBalanceStrategy(configuration);
	}
	
	public ConnectionFactoryImpl(LoadBalanceStrategy<ConnectionPool> loadBalanceStrategy) {
		this.loadBalanceStrategy = loadBalanceStrategy;
	}
	
	/* (non-Javadoc)
	 * @see cn.zxl.deerlet.redis.client.connection.pool.ConnectionPool#obtainConnection()
	 */
	@Override
	public Connection createConnection(String key) {
		return loadBalanceStrategy.select(key).obtainConnection();
	}
	
	public LoadBalanceStrategy<ConnectionPool> getLoadBalanceStrategy() {
		return loadBalanceStrategy;
	}

}
