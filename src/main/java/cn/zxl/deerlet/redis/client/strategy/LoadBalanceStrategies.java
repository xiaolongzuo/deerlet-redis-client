/**
 * 
 */
package cn.zxl.deerlet.redis.client.strategy;

import cn.zxl.deerlet.redis.client.config.Configuration;
import cn.zxl.deerlet.redis.client.config.Server;
import cn.zxl.deerlet.redis.client.connection.ConnectionPool;
import cn.zxl.deerlet.redis.client.connection.impl.ConnectionPoolImpl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 策略的静态工厂类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:43:51
 *
 */
public abstract class LoadBalanceStrategies {
	
	private static final int INIT_SIZE = 10;

	private static final int MAX_SIZE = 100;

	private static final int MIN_IDLE_SIZE = 10;

	private static final int MAX_IDLE_SIZE = 20;
	
	private static final String INIT_SIZE_PROPERTY = "initSize";

	private static final String MAX_SIZE_PROPERTY = "maxSize";

	private static final String MIN_IDLE_SIZE_PROPERTY = "minIdleSize";

	private static final String MAX_IDLE_SIZE_PROPERTY = "maxIdleSize";

	public static LoadBalanceStrategy<ConnectionPool> createLoadBalanceStrategy(Configuration configuration) {
		return chooseLoadBalanceStrategy(createConnectionPools(configuration));
	}
	
	private static List<ConnectionPool> createConnectionPools(Configuration configuration) {
		List<ConnectionPool> connectionPools = new ArrayList<ConnectionPool>();
		
		List<Server> servers = configuration.getServerList();
		Integer initSize = configuration.getInteger(INIT_SIZE_PROPERTY, INIT_SIZE);
		Integer maxSize = configuration.getInteger(MAX_SIZE_PROPERTY, MAX_SIZE);
		Integer minIdleSize = configuration.getInteger(MIN_IDLE_SIZE_PROPERTY, MIN_IDLE_SIZE);
		Integer maxIdleSize = configuration.getInteger(MAX_IDLE_SIZE_PROPERTY, MAX_IDLE_SIZE);
		for (int i = 0; i < servers.size(); i++) {
			connectionPools.add(new ConnectionPoolImpl(servers.get(i), initSize, maxSize, minIdleSize, maxIdleSize));
		}
		return connectionPools;
	}
	
	private static LoadBalanceStrategy<ConnectionPool> chooseLoadBalanceStrategy(List<ConnectionPool> connectionPools) {
		if (connectionPools != null && connectionPools.size() == 1) {
			return new SimpleNodeStrategy<ConnectionPool>(connectionPools);
		} else {
			return new ConsistencyHashStrategy<ConnectionPool>(connectionPools);
		}
	}
	
}
