package cn.zxl.deerlet.redis.client;

import java.io.UnsupportedEncodingException;

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
public class DeerletRedisClientKeyTest extends AbstractDeerletRedisClientTest {

	@Test 
	public void testDel() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertNotNull(deerletRedisClient.get("testKey"));
		deerletRedisClient.del("testKey");
		Assert.assertNull(deerletRedisClient.get("testKey"));
	}
	
	@Test
	public void testDump() throws UnsupportedEncodingException {
		deerletRedisClient.set("testKey", "TestValue");
		byte[] bytes = deerletRedisClient.dump("testKey");
		Assert.assertTrue(deerletRedisClient.restore("testKey2", 0, bytes));
		Assert.assertEquals("TestValue", deerletRedisClient.get("testKey2"));
	}
	
}
