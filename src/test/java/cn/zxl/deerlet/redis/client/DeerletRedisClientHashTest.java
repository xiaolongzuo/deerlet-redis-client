package cn.zxl.deerlet.redis.client;

import org.junit.Test;

import junit.framework.Assert;

/**
 * 
 * 测试客户端是否可以正常工作
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:45:23
 *
 */
public class DeerletRedisClientHashTest extends AbstractDeerletRedisClientTest {
	
	
	@Test
	public void testHget(){
		deerletRedisClient.hset("testKey", "testField", "testValue");
		Assert.assertEquals("testValue", deerletRedisClient.hget("testKey","testField"));
	}
	
	@Test
	public void testHset() {
		Assert.assertSame(1,deerletRedisClient.hset("testKey", "testField", "testValue"));
		Assert.assertSame(0,deerletRedisClient.hset("testKey", "testField", "tesValueOther"));
		Assert.assertEquals("tesValueOther", deerletRedisClient.hget("testKey","testField"));
	}
	
}
