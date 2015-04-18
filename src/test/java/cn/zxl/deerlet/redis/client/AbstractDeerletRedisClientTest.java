/**
 * 
 */
package cn.zxl.deerlet.redis.client;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;

import cn.zxl.deerlet.redis.client.connection.pool.ConnectionPoolImpl;

/**
 * @author zuoxiaolong
 *
 */
public abstract class AbstractDeerletRedisClientTest {

	protected DeerletRedisClient deerletRedisClient = new DeerletRedisClientImpl(new ConnectionPoolImpl());

	@Before
	public void before() {
		deerletRedisClient.flushall();
		Assert.assertEquals(0, deerletRedisClient.dbsize());
	}

	@After
	public void after() {
		deerletRedisClient.flushall();
		Assert.assertEquals(0, deerletRedisClient.dbsize());
	}
	
}
