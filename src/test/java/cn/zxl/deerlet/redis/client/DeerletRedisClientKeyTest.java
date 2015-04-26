package cn.zxl.deerlet.redis.client;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import cn.zxl.deerlet.redis.client.command.Cursor;
import cn.zxl.deerlet.redis.client.command.ObjectSubcommands;
import cn.zxl.deerlet.redis.client.command.Types;

/**
 * 
 * 测试客户端是否可以正常工作
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:45:23
 *
 */
public class DeerletRedisClientKeyTest extends AbstractDeerletRedisClientTest {

	/*
	 * 
    DEL
    DUMP
    EXISTS
    EXPIRE
    EXPIREAT
    KEYS
    MIGRATE
    MOVE
    OBJECT
    PERSIST
    PEXPIRE
    PEXPIREAT
    PTTL
    RANDOMKEY
    RENAME
    RENAMENX
    RESTORE
    SORT
    TTL
    TYPE
    SCAN
	 */
	
	@Test 
	public void testDel() {
		Assert.assertTrue(deerletRedisClient.set("testKey", 10));
		Assert.assertNotNull(deerletRedisClient.get("testKey"));
		deerletRedisClient.del("testKey");
		Assert.assertNull(deerletRedisClient.get("testKey"));
	}
	
	@Test
	public void testDump() throws UnsupportedEncodingException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		byte[] bytes = deerletRedisClient.dump("testKey");
		Assert.assertTrue(deerletRedisClient.restore("testKey2", 0, bytes));
		Assert.assertEquals("TestValue", deerletRedisClient.get("testKey2"));
	}
	
	@Test
	public void testExists() {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertEquals(true, deerletRedisClient.exists("testKey"));
		Assert.assertEquals(false, deerletRedisClient.exists("none"));
	}
	
	@Test
	public void testExpire() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertTrue(deerletRedisClient.expire("testKey", 5));
		Thread.sleep(2000);
		Assert.assertEquals(true, deerletRedisClient.exists("testKey"));
		Thread.sleep(3000);
		Assert.assertEquals(false, deerletRedisClient.exists("testKey"));
	}
	
	@Test
	public void testExpireat() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		int seconds = (int)((new Date().getTime() / 1000) + 5);
		Assert.assertTrue(deerletRedisClient.expireat("testKey", seconds));
		Thread.sleep(2000);
		Assert.assertEquals(true, deerletRedisClient.exists("testKey"));
		Thread.sleep(3000);
		Assert.assertEquals(false, deerletRedisClient.exists("testKey"));
	}
	
	@Test
	public void testKeys() throws InterruptedException {
		for (int i = 0; i < 1000; i++) {
			Assert.assertTrue(deerletRedisClient.set("testKey" + i, "TestValue"));
		}
		List<String> list = deerletRedisClient.keys("*");
		Assert.assertNotNull(list);
		Assert.assertEquals(1000, list.size());
		for (int i = 0; i < 1000; i++) {
			Assert.assertEquals(true, list.contains("testKey" + i));
		}
	}
	
	@Test
	public void testMigrate() {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertTrue(deerletRedisClient.migrate("localhost", "6666", "testKey", 0, -1));
		Assert.assertEquals("TestValue", otherDeerletRedisClient.get("testKey"));
		Assert.assertNull(deerletRedisClient.get("testKey"));
	}
	
	@Test
	public void testMove() {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertTrue(deerletRedisClient.move("testKey", 1));
		Assert.assertTrue(deerletRedisClient.select(1));
		Assert.assertEquals("TestValue", deerletRedisClient.get("testKey"));
		Assert.assertTrue(deerletRedisClient.select(0));
		Assert.assertNull(deerletRedisClient.get("testKey"));
	}
	
	@Test
	public void testObject() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertEquals(1, deerletRedisClient.object(ObjectSubcommands.refcount, "testKey"));
		Assert.assertEquals("raw", deerletRedisClient.object(ObjectSubcommands.encoding, "testKey"));
		Thread.sleep(3000);
		Assert.assertTrue(Integer.valueOf(deerletRedisClient.object(ObjectSubcommands.idletime, "testKey").toString()) >= 3);
	}
	
	@Test
	public void testPersist() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertTrue(deerletRedisClient.expire("testKey", 1));
		Assert.assertTrue(deerletRedisClient.persist("testKey"));
		Thread.sleep(2000);
		Assert.assertTrue(deerletRedisClient.exists("testKey"));
	}
	
	@Test
	public void testPexpire() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertTrue(deerletRedisClient.pexpire("testKey", 5000));
		Thread.sleep(2000);
		Assert.assertEquals(true, deerletRedisClient.exists("testKey"));
		Thread.sleep(3000);
		Assert.assertEquals(false, deerletRedisClient.exists("testKey"));
	}
	
	@Test
	public void testPexpireat() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		long seconds = new Date().getTime() + 5 * 1000;
		Assert.assertTrue(deerletRedisClient.pexpireat("testKey", seconds));
		Thread.sleep(2000);
		Assert.assertEquals(true, deerletRedisClient.exists("testKey"));
		Thread.sleep(3000);
		Assert.assertEquals(false, deerletRedisClient.exists("testKey"));
	}
	
	@Test
	public void testPttl() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertEquals(-1, deerletRedisClient.pttl("testKey"));
		Assert.assertEquals(-2, deerletRedisClient.pttl("none"));
		Assert.assertTrue(deerletRedisClient.pexpire("testKey", 5000));
		Assert.assertEquals(true, deerletRedisClient.pttl("testKey") > 2000);
	}
	
	@Test
	public void testRandomKey() throws InterruptedException {
		Assert.assertNull(deerletRedisClient.randomkey());
		Assert.assertNull(otherDeerletRedisClient.randomkey());
		Assert.assertTrue(otherDeerletRedisClient.set("testKey", "TestValue"));
		Assert.assertEquals("testKey", otherDeerletRedisClient.randomkey());
	}
	
	@Test
	public void testRename() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertTrue(deerletRedisClient.rename("testKey", "testKey1"));
		Assert.assertTrue(deerletRedisClient.exists("testKey1"));
		Assert.assertFalse(deerletRedisClient.exists("testKey"));
	}
	
	@Test
	public void testRenamenx() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertTrue(deerletRedisClient.set("testKey2", "TestValue"));
		Assert.assertTrue(deerletRedisClient.renamenx("testKey", "testKey1"));
		Assert.assertTrue(deerletRedisClient.exists("testKey1"));
		Assert.assertFalse(deerletRedisClient.exists("testKey"));
		Assert.assertFalse(deerletRedisClient.renamenx("testKey1","testKey2"));
	}
	
	@Test
	public void testRestore() throws UnsupportedEncodingException {
		testDump();
	}
	
	@Test
	public void testSort() throws UnsupportedEncodingException {
		Assert.assertEquals(5, deerletRedisClient.lpush("listKey", 4,3,5,1,2));
		List<String> list = deerletRedisClient.sort("listKey", new Object[]{});
		for (int i = 1; i < list.size() + 1; i++) {
			Assert.assertEquals(i, Integer.valueOf(list.get(i - 1)).intValue());
		}
	}
	
	@Test
	public void testTtl() throws InterruptedException {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertEquals(-1, deerletRedisClient.ttl("testKey"));
		Assert.assertEquals(-2, deerletRedisClient.ttl("none"));
		Assert.assertTrue(deerletRedisClient.expire("testKey", 5));
		Assert.assertEquals(true, deerletRedisClient.ttl("testKey") > 2);
	}
	
	@Test
	public void testType() {
		Assert.assertTrue(deerletRedisClient.set("testKey", "TestValue"));
		Assert.assertEquals(Types.string, deerletRedisClient.type("testKey"));
		Assert.assertTrue(deerletRedisClient.set("testKey", 1));
		Assert.assertEquals(Types.string, deerletRedisClient.type("testKey"));
		Assert.assertTrue(deerletRedisClient.lpush("listKey", 1,1,1) > 0);
		Assert.assertEquals(Types.list, deerletRedisClient.type("listKey"));
	}
	
	@Test
	public void testScan() {
		for (int i = 0; i < 1000; i++) {
			Assert.assertTrue(deerletRedisClient.set("testKey" + i, "TestValue"));
		}
		Assert.assertEquals(1000, deerletRedisClient.dbsize());
		Cursor cursor = deerletRedisClient.scan(null, null, 100);
		int sum = cursor.getResultList().size();
		for (int i = 0; i < 100; i++) {
			cursor = deerletRedisClient.scan(cursor, null, 100);
			sum += cursor.getResultList().size();
		}
		Assert.assertEquals(1000, sum);
	}
	
}
