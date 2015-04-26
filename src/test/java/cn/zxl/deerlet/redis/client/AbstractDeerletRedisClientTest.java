/**
 * 
 */
package cn.zxl.deerlet.redis.client;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;

import cn.zxl.deerlet.redis.client.config.ConfigurationFactory;
import cn.zxl.deerlet.redis.client.connection.impl.ConnectionFactoryImpl;

/**
 * @author zuoxiaolong
 *
 */
public abstract class AbstractDeerletRedisClientTest {

	protected DeerletRedisClient deerletRedisClient = new DeerletRedisClientImpl(new ConnectionFactoryImpl(ConfigurationFactory.create().loadConfiguration()));

	protected DeerletRedisClient otherDeerletRedisClient = new DeerletRedisClientImpl(new ConnectionFactoryImpl(ConfigurationFactory.create().loadConfiguration("deerlet-other.properties")));

	@Before
	public void before() {
		deerletRedisClient.flushall();
		otherDeerletRedisClient.flushall();
		Assert.assertEquals(0, deerletRedisClient.dbsize());
		Assert.assertEquals(0, otherDeerletRedisClient.dbsize());
	}

	@After
	public void after() {
		deerletRedisClient.flushall();
		otherDeerletRedisClient.flushall();
		Assert.assertEquals(0, deerletRedisClient.dbsize());
		Assert.assertEquals(0, otherDeerletRedisClient.dbsize());
	}
	
}
