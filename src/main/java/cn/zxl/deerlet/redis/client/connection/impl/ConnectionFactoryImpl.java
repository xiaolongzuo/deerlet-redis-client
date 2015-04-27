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
import org.apache.log4j.Logger;

/**
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 */
public class ConnectionFactoryImpl implements ConnectionFactory {

	private static final Logger LOGGER = Logger.getLogger(ConnectionFactoryImpl.class);
	
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
		ConnectionPool connectionPool = loadBalanceStrategy.select(key);
		Connection connection = connectionPool.obtainConnection();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("choose server for key[" + key + "] ,connectionPool is [" + connectionPool + "] ,connection is [" + connection + "]");
		}
		return connection;
	}
	
	public LoadBalanceStrategy<ConnectionPool> getLoadBalanceStrategy() {
		return loadBalanceStrategy;
	}

}
