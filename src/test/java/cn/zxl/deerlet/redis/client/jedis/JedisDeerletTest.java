/**
 * 
 */
package cn.zxl.deerlet.redis.client.jedis;

import cn.zxl.deerlet.redis.client.AbstractDeerletRedisClientTest;
import org.apache.log4j.Logger;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zuoxiaolong
 *
 */
public class JedisDeerletTest extends AbstractDeerletRedisClientTest {
	
	private static final Logger LOGGER = Logger.getLogger(JedisDeerletTest.class);
	
	private static final int times = 10000;

	@Test
	public void testCommon() {
		long time = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			deerletRedisClient.set("key" + i, "value" + i);
		}
		LOGGER.info("deerlet执行" + times + "次set操作花费" + (System.currentTimeMillis()-time) + "ms");
		
		deerletRedisClient.flushall();
		
		Jedis jedis = new Jedis("localhost");
		time = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			jedis.set("key" + i, "value" + i);
		}
		LOGGER.info("jedis执行" + times + "次set操作花费" + (System.currentTimeMillis()-time) + "ms");
		jedis.close();
	}
	
	@Test
	public void testConcurrent() throws InterruptedException{
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		
		final CountDownLatch countDownLatch1 = new CountDownLatch(times);
		long time = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			final int index = i;
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					deerletRedisClient.set("key" + index, "value" + index);
					countDownLatch1.countDown();
				}
			});
		}
		countDownLatch1.await();
		LOGGER.info("deerlet并发执行" + times + "次set操作花费" + (System.currentTimeMillis()-time) + "ms");
		
		deerletRedisClient.flushall();
		
		final ThreadLocal<Jedis> threadLocal = new ThreadLocal<Jedis>();
		final CountDownLatch countDownLatch2 = new CountDownLatch(times);
		time = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			final int index = i;
			executorService.execute(new Runnable() {
				
				@Override
				public void run() {
					if (threadLocal.get() == null) {
						threadLocal.set(new Jedis("localhost"));
					}
					threadLocal.get().set("key" + index, "value" + index);
					countDownLatch2.countDown();
				}
			});
		}
		countDownLatch2.await();
		LOGGER.info("jedis并发执行" + times + "次set操作花费" + (System.currentTimeMillis()-time) + "ms");
	}
	
}
