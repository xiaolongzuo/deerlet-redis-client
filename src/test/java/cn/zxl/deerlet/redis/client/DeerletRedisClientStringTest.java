package cn.zxl.deerlet.redis.client;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import cn.zxl.deerlet.redis.client.command.Bit;
import cn.zxl.deerlet.redis.client.command.BitopOperations;

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
	public void testAppend() throws IOException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "testValue"));
		int length = deerletRedisClient.append("testKey", "Append");
		Assert.assertEquals("testValue".length() + "Append".length(), length);
		Assert.assertEquals("testValueAppend", deerletRedisClient.get("testKey"));
	}
	
	@Test
	public void testBitcount() throws IOException {
		deerletRedisClient.setbit("testKey", 0, Bit.one);
		Assert.assertEquals(1, deerletRedisClient.bitcount("testKey", 0, 0));
		deerletRedisClient.setbit("testKey", 1, Bit.one);
		deerletRedisClient.setbit("testKey", 2, Bit.zero);
		deerletRedisClient.setbit("testKey", 3, Bit.one);
		Assert.assertEquals(3, deerletRedisClient.bitcount("testKey", 0, 4));
	}
	
	@Test
	public void testBitop() throws IOException {
		deerletRedisClient.setbit("testKey1", 0, Bit.one);
		deerletRedisClient.setbit("testKey1", 1, Bit.one);
		deerletRedisClient.setbit("testKey1", 2, Bit.zero);
		deerletRedisClient.setbit("testKey1", 3, Bit.one);
		
		deerletRedisClient.setbit("testKey2", 0, Bit.zero);
		deerletRedisClient.setbit("testKey2", 1, Bit.one);
		deerletRedisClient.setbit("testKey2", 2, Bit.one);
		deerletRedisClient.setbit("testKey2", 3, Bit.one);
		
		deerletRedisClient.bitop(BitopOperations.and, "testKey", "testKey1", "testKey2");
		Assert.assertEquals(0, deerletRedisClient.getbit("testKey", 0));
		Assert.assertEquals(1, deerletRedisClient.getbit("testKey", 1));
		Assert.assertEquals(0, deerletRedisClient.getbit("testKey", 2));
		Assert.assertEquals(1, deerletRedisClient.getbit("testKey", 3));
		
		deerletRedisClient.bitop(BitopOperations.not, "testKey", "testKey1");
		Assert.assertEquals(0, deerletRedisClient.getbit("testKey", 0));
		Assert.assertEquals(0, deerletRedisClient.getbit("testKey", 1));
		Assert.assertEquals(1, deerletRedisClient.getbit("testKey", 2));
		Assert.assertEquals(0, deerletRedisClient.getbit("testKey", 3));
		
		deerletRedisClient.bitop(BitopOperations.or, "testKey", "testKey1", "testKey2");
		Assert.assertEquals(1, deerletRedisClient.getbit("testKey", 0));
		Assert.assertEquals(1, deerletRedisClient.getbit("testKey", 1));
		Assert.assertEquals(1, deerletRedisClient.getbit("testKey", 2));
		Assert.assertEquals(1, deerletRedisClient.getbit("testKey", 3));
		
		deerletRedisClient.bitop(BitopOperations.xor, "testKey", "testKey1", "testKey2");
		Assert.assertEquals(1, deerletRedisClient.getbit("testKey", 0));
		Assert.assertEquals(0, deerletRedisClient.getbit("testKey", 1));
		Assert.assertEquals(1, deerletRedisClient.getbit("testKey", 2));
		Assert.assertEquals(0, deerletRedisClient.getbit("testKey", 3));
	}
	
	@Test 
	public void testDecr() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 - 1, deerletRedisClient.decr("testKey"));
	}
	
	@Test 
	public void testDecrBy() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 - 2, deerletRedisClient.decrby("testKey", 2));
	}
	
	@Test
	public void testGet() throws IOException {
		deerletRedisClient.set("testKey", "testValue");
		Assert.assertEquals("testValue", deerletRedisClient.get("testKey"));
	}
	
	@Test
	public void testGetChinese() throws IOException {
		deerletRedisClient.set("testKey", "你好");
		Assert.assertEquals("你好", deerletRedisClient.get("testKey"));
	}

	@Test
	public void testGetbit() throws IOException {
		deerletRedisClient.setbit("testKey", 0, Bit.one);
		Assert.assertEquals(1, deerletRedisClient.bitcount("testKey", 0, 0));
	}
	
	@Test
	public void testGetrange() throws IOException {
		deerletRedisClient.set("testKey", "testValue");
		Assert.assertEquals("estV", deerletRedisClient.getrange("testKey", 1, 4));
		Assert.assertEquals("t", deerletRedisClient.getrange("testKey", 0, 0));
	}
	
	@Test
	public void testGetset() throws IOException {
		Assert.assertEquals(null, deerletRedisClient.getset("testKey", "testValue1"));
		Assert.assertEquals("testValue1", deerletRedisClient.getset("testKey", "testValue2"));
		Assert.assertEquals("testValue2", deerletRedisClient.get("testKey"));
	}
	
	@Test 
	public void testIncr() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 + 1, deerletRedisClient.incr("testKey"));
	}
	
	@Test 
	public void testIncrBy() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 + 2, deerletRedisClient.incrby("testKey", 2));
	}
	
	@Test 
	public void testIncrByFloat() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10+1.1F, deerletRedisClient.incrbyfloat("testKey", 1.1F));
	}
	
	@Test 
	public void testMget() {
		Assert.assertTrue(deerletRedisClient.set("test1", "value1"));
		Assert.assertTrue(deerletRedisClient.set("test2", "value2"));
		Assert.assertTrue(deerletRedisClient.set("test3", "value3"));
		List<String> result = deerletRedisClient.mget("test1","test2","test3");
		Assert.assertNotNull(result);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals("value1", result.get(0));
		Assert.assertEquals("value2", result.get(1));
		Assert.assertEquals("value3", result.get(2));
	}
	
	@Test 
	public void testMset() {
		Assert.assertTrue(deerletRedisClient.mset(new String[]{"test1","test2","test3"}, "value1","value2","value3"));
		List<String> result = deerletRedisClient.mget("test1","test2","test3");
		Assert.assertNotNull(result);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals("value1", result.get(0));
		Assert.assertEquals("value2", result.get(1));
		Assert.assertEquals("value3", result.get(2));
		
		Assert.assertTrue(deerletRedisClient.mset(new String[]{"test1","test2","test3"}, "value1","value2","value3"));
	}
	
	@Test 
	public void testMsetnx() {
		Assert.assertTrue(deerletRedisClient.msetnx(new String[]{"test1","test2","test3"}, "value1","value2","value3"));
		List<String> result = deerletRedisClient.mget("test1","test2","test3");
		Assert.assertNotNull(result);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals("value1", result.get(0));
		Assert.assertEquals("value2", result.get(1));
		Assert.assertEquals("value3", result.get(2));
		
		Assert.assertFalse(deerletRedisClient.msetnx(new String[]{"test1","test2","test3"}, "value1","value2","value3"));
	}
	
	@Test
	public void testPsetex() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.psetex("testKey", 3000, "testValue"));
		Assert.assertTrue(deerletRedisClient.exists("testKey"));
		Thread.sleep(3000);
		Assert.assertFalse(deerletRedisClient.exists("testKey"));
	}
	
	@Test
	public void testSet() throws IOException {
		testGet();
	}
	
	@Test
	public void testSetbit() throws IOException {
		testGetbit();
	}
	
	@Test
	public void testSetex() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.setex("testKey", 3, "testValue"));
		Assert.assertTrue(deerletRedisClient.exists("testKey"));
		Thread.sleep(3000);
		Assert.assertFalse(deerletRedisClient.exists("testKey"));
	}
	
	@Test
	public void testSetnx(){
		Assert.assertTrue(deerletRedisClient.setnx("testKey", "testValue"));
		Assert.assertEquals("testValue", deerletRedisClient.get("testKey"));
		Assert.assertFalse(deerletRedisClient.setnx("testKey", "testValue"));
	}
	
	@Test
	public void testSetrange(){
		Assert.assertTrue(deerletRedisClient.set("testKey", "testValue"));
		deerletRedisClient.setrange("testKey", 3, "change");
		Assert.assertEquals("teschange", deerletRedisClient.get("testKey"));
	}
	
	@Test
	public void testStrlen(){
		Assert.assertTrue(deerletRedisClient.set("testKey", "testValue"));
		Assert.assertEquals(9, deerletRedisClient.strlen("testKey"));
	}
	
}
