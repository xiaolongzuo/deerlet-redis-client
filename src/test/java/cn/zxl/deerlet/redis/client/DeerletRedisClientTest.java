package cn.zxl.deerlet.redis.client;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.zxl.deerlet.redis.client.connection.pool.ConnectionPoolImpl;

/**
 * 
 * 测试客户端是否可以正常工作
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:45:23
 *
 */
public class DeerletRedisClientTest {

	private DeerletRedisClient deerletRedisClient = new DeerletRedisClientImpl(new ConnectionPoolImpl());

	@Before
	public void before() {
		deerletRedisClient.flushAll();
		Assert.assertEquals(0, deerletRedisClient.dbSize());
	}

	@After
	public void after() {
		deerletRedisClient.flushAll();
		Assert.assertEquals(0, deerletRedisClient.dbSize());
	}

	@Test
	public void testGetAndSet() throws IOException {
		deerletRedisClient.set("testKey", "testValue");
		Assert.assertEquals("testValue", deerletRedisClient.get("testKey"));
	}

	@Test
	public void testDbSize() throws IOException {
		int size = 1000;
		for (int i = 0; i < size; i++) {
			deerletRedisClient.set("testKey" + i, "testValue");
		}
		Assert.assertEquals(size, deerletRedisClient.dbSize());
	}
	
	@Test
	public void testAppend() throws IOException {
		deerletRedisClient.set("testKey", "testValue");
		int length = deerletRedisClient.append("testKey", "Append");
		Assert.assertEquals("testValue".length() + "Append".length(), length);
		Assert.assertEquals("testValueAppend", deerletRedisClient.get("testKey"));
	}
	
	@Test
	public void testFlushDb() throws IOException {
		deerletRedisClient.set("testKey", "testValue");
		deerletRedisClient.flushDb();
		Assert.assertEquals(0, deerletRedisClient.dbSize());
	}
	
	@Test 
	public void testIncrBy() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 + 2, deerletRedisClient.incrBy("testKey", 2));
	}
	
	@Test 
	public void testIncr() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 + 1, deerletRedisClient.incr("testKey"));
	}
	
	@Test 
	public void testIncrByFloat() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10+1.1F, deerletRedisClient.incrByFloat("testKey", 1.1F));
	}
	
	@Test 
	public void testDecrBy() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 - 2, deerletRedisClient.decrBy("testKey", 2));
	}
	
	@Test 
	public void testDecr() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertEquals(10 - 1, deerletRedisClient.decr("testKey"));
	}
	
	@Test 
	public void testDel() {
		deerletRedisClient.set("testKey", 10);
		Assert.assertNotNull(deerletRedisClient.get("testKey"));
		deerletRedisClient.del("testKey");
		Assert.assertNull(deerletRedisClient.get("testKey"));
	}
	
	@Test 
	public void testBgSave() {
		Assert.assertTrue(deerletRedisClient.bgSave());
	}

	@Test
	public void testMultiThread() throws IOException, InterruptedException, BrokenBarrierException {
		int size = 1000;
		final CyclicBarrier cyclicBarrier = new CyclicBarrier(size + 1);
		for (int i = 0; i < size; i++) {
			final int fi = i;
			new Thread(new Runnable() {
				public void run() {
					deerletRedisClient.set("testKey" + fi, "testValue");
					try {
						cyclicBarrier.await();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					} catch (BrokenBarrierException e) {
						throw new RuntimeException(e);
					}
				}
			}).start();
		}
		cyclicBarrier.await();
		Assert.assertEquals(size, deerletRedisClient.dbSize());
	}
	
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
