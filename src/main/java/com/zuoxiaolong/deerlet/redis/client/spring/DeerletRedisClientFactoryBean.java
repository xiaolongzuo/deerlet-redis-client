/**
 * 
 */
package com.zuoxiaolong.deerlet.redis.client.spring;

import com.zuoxiaolong.deerlet.redis.client.ClusterDeerletRedisClient;
import com.zuoxiaolong.deerlet.redis.client.DeerletRedisClientFactory;
import org.springframework.beans.factory.FactoryBean;

import com.zuoxiaolong.deerlet.redis.client.DeerletRedisClient;

/**
 * 支持与spring的无缝集成
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:43:51
 *
 * @see DeerletRedisClient
 * @see ClusterDeerletRedisClient
 */
public class DeerletRedisClientFactoryBean implements FactoryBean<DeerletRedisClient> {
	
	private String configFile;
	
	/**
	 * @param configFile the configFile to set
	 */
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public DeerletRedisClient getObject() throws Exception {
		return DeerletRedisClientFactory.INSTANCE.createDeerletRedisClient(configFile);
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

}
