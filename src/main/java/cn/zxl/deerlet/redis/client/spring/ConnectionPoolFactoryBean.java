/**
 * 
 */
package cn.zxl.deerlet.redis.client.spring;

import org.springframework.beans.factory.FactoryBean;

import cn.zxl.deerlet.redis.client.connection.pool.ConnectionPool;
import cn.zxl.deerlet.redis.client.connection.pool.ConnectionPoolImpl;

/**
 * @author zuoxiaolong
 *
 */
public class ConnectionPoolFactoryBean implements FactoryBean<ConnectionPool> {
	
	private Integer initSize = 10;
	
	private Integer minIdleSize = 10;
	
	private Integer maxIdleSize = 20;
	
	private Integer maxSize = 100;

	/**
	 * @param initSize the initSize to set
	 */
	public void setInitSize(Integer initSize) {
		this.initSize = initSize;
	}

	/**
	 * @param minIdleSize the minIdleSize to set
	 */
	public void setMinIdleSize(Integer minIdleSize) {
		this.minIdleSize = minIdleSize;
	}

	/**
	 * @param maxIdleSize the maxIdleSize to set
	 */
	public void setMaxIdleSize(Integer maxIdleSize) {
		this.maxIdleSize = maxIdleSize;
	}

	/**
	 * @param maxSize the maxSize to set
	 */
	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public ConnectionPool getObject() throws Exception {
		return new ConnectionPoolImpl(initSize, maxSize, minIdleSize, maxIdleSize);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<? extends ConnectionPool> getObjectType() {
		return ConnectionPool.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

}
