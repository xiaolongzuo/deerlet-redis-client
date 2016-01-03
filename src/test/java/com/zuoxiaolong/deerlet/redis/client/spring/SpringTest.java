/**
 * 
 */
package com.zuoxiaolong.deerlet.redis.client.spring;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zuoxiaolong.deerlet.redis.client.DeerletRedisClient;

/**
 * 
 * 测试与spring集成是否可以正常工作
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:45:23
 *
 */
public class SpringTest {
	
	private static DeerletRedisClient deerletRedisClient = null;

	@BeforeClass
	public static void testFactoryBean(){
		ApplicationContext applicationContext = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
		deerletRedisClient = applicationContext.getBean(DeerletRedisClient.class);
		Assert.assertNotNull(deerletRedisClient);
	}
	
	@Test
	public void testCommand() {
		deerletRedisClient.set("testKey","testValue");
		Assert.assertEquals("testValue", deerletRedisClient.get("testKey"));
		Assert.assertEquals(1, deerletRedisClient.dbsize());
		deerletRedisClient.flushall();
		Assert.assertEquals(0, deerletRedisClient.dbsize());
	}
	
}
