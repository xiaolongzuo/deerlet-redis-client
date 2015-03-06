/**
 * 
 */
package cn.zxl.deerlet.redis.client.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import cn.zxl.deerlet.redis.client.DeerletRedisClient;
import cn.zxl.deerlet.redis.client.DeerletRedisClientImpl;
import cn.zxl.deerlet.redis.client.connection.pool.ConnectionPool;

/**
 * 支持与spring的无缝集成
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:43:51
 *
 */
public class DeerletRedisClientFactoryBean implements FactoryBean<DeerletRedisClient> , InitializingBean {
	
	private ConnectionPool connectionPool;
	
	/**
	 * @param connectionPool the connectionPool to set
	 */
	public void setConnectionPool(ConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public DeerletRedisClient getObject() throws Exception {
		return new DeerletRedisClientImpl(connectionPool);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<? extends DeerletRedisClient> getObjectType() {
		return DeerletRedisClient.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (connectionPool == null) {
			throw new IllegalArgumentException("connectionPool can't be null!");
		}
	}

	
}
