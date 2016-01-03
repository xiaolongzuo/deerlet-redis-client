package com.zuoxiaolong.deerlet.redis.client;

import java.util.List;

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
public class DeerletRedisClientListTest extends AbstractDeerletRedisClientTest {

	@Test 
	public void testLPushAndLLen() {
		deerletRedisClient.lpush("testList", "1", "2", "3", "4", "5");
		Assert.assertEquals(5, deerletRedisClient.llen("testList"));
		Assert.assertEquals("5", deerletRedisClient.lindex("testList", 0));
	}
	
	@Test 
	public void testLIndex() {
		deerletRedisClient.lpush("testList", "1", "2", "3", "4", "5");
		Assert.assertEquals("5", deerletRedisClient.lindex("testList", 0));
		Assert.assertEquals("4", deerletRedisClient.lindex("testList", 1));
		Assert.assertEquals("3", deerletRedisClient.lindex("testList", 2));
		Assert.assertEquals("2", deerletRedisClient.lindex("testList", 3));
		Assert.assertEquals("1", deerletRedisClient.lindex("testList", 4));
		
		Assert.assertEquals("1", deerletRedisClient.lindex("testList", -1));
		Assert.assertEquals("2", deerletRedisClient.lindex("testList", -2));
		Assert.assertEquals("3", deerletRedisClient.lindex("testList", -3));
		Assert.assertEquals("4", deerletRedisClient.lindex("testList", -4));
		Assert.assertEquals("5", deerletRedisClient.lindex("testList", -5));
	}
	
	@Test 
	public void testLRange() {
		Object[] ss = new Object[]{"1", "2", "3", "4", "5"};
		deerletRedisClient.lpush("testList", ss);
		List<String> list = deerletRedisClient.lrange("testList", 0, -1);
		Assert.assertEquals(ss.length, list.size());
		for (int i = 0; i < list.size(); i++) {
			Assert.assertEquals(ss[i], list.get(ss.length - i - 1));
		}
	}
	
}
