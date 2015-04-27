/**
 * 
 */
package cn.zxl.deerlet.redis.client;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;

/**
 * @author zuoxiaolong
 *
 */
public abstract class AbstractDeerletRedisClientTest {

	protected DeerletRedisClient deerletRedisClient = DeerletRedisClientFactory.INSTANCE.createDeerletRedisClient();

	protected DeerletRedisClient otherDeerletRedisClient = DeerletRedisClientFactory.INSTANCE.createDeerletRedisClient("deerlet-other.properties");

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
