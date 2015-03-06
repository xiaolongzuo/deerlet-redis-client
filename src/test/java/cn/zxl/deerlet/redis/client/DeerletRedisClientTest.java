package cn.zxl.deerlet.redis.client;

import java.io.IOException;
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
}
