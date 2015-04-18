package cn.zxl.deerlet.redis.client;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

/**
 * 
 * 测试客户端是否可以正常工作
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:45:23
 *
 */
public class DeerletRedisClientServerTest extends AbstractDeerletRedisClientTest {

	@Test
	public void testDbSize() throws IOException {
		int size = 1000;
		for (int i = 0; i < size; i++) {
			deerletRedisClient.set("testKey" + i, "testValue");
		}
		Assert.assertEquals(size, deerletRedisClient.dbsize());
	}
	
	@Test
	public void testFlushDb() throws IOException {
		deerletRedisClient.set("testKey", "testValue");
		deerletRedisClient.flushdb();
		Assert.assertEquals(0, deerletRedisClient.dbsize());
	}
	
	public void testBgSave() {
		Assert.assertTrue(deerletRedisClient.bgsave());
	}

}