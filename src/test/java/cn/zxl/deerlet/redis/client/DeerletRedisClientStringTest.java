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
public class DeerletRedisClientStringTest extends AbstractDeerletRedisClientTest {

	@Test
	public void testGetAndSet() throws IOException {
		deerletRedisClient.set("testKey", "testValue");
		Assert.assertEquals("testValue", deerletRedisClient.get("testKey"));
	}
	
	@Test
	public void testGetAndSetChinese() throws IOException {
		deerletRedisClient.set("testKey", "你好");
		Assert.assertEquals("你好", deerletRedisClient.get("testKey"));
	}

	@Test
	public void testAppend() throws IOException {
		deerletRedisClient.set("testKey", "testValue");
		int length = deerletRedisClient.append("testKey", "Append");
		Assert.assertEquals("testValue".length() + "Append".length(), length);
		Assert.assertEquals("testValueAppend", deerletRedisClient.get("testKey"));
	}
	
	@Test 
	public void testIncrBy() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 + 2, deerletRedisClient.incrby("testKey", 2));
	}
	
	@Test 
	public void testIncr() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 + 1, deerletRedisClient.incr("testKey"));
	}
	
	@Test 
	public void testIncrByFloat() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10+1.1F, deerletRedisClient.incrbyfloat("testKey", 1.1F));
	}
	
	@Test 
	public void testDecrBy() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 - 2, deerletRedisClient.decrby("testKey", 2));
	}
	
	@Test 
	public void testDecr() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 - 1, deerletRedisClient.decr("testKey"));
	}
	
}
