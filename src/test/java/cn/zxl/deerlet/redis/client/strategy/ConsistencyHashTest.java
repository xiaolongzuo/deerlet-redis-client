package cn.zxl.deerlet.redis.client.strategy;

import cn.zxl.deerlet.redis.client.config.Servers;
import cn.zxl.deerlet.redis.client.connection.ConnectionPool;
import cn.zxl.deerlet.redis.client.connection.impl.ConnectionPoolImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * <p/>
 * </p>
 *
 * @author 左潇龙
 * @since 4/27/2015 5:30 PM
 */
public class ConsistencyHashTest {

    @Test
    public void testConsistencyHash() {
        List<ConnectionPool> nodes = new ArrayList<ConnectionPool>();
        nodes.add(new ConnectionPoolImpl(Servers.newServer("localhost", 6379),10,20,10,20));
        nodes.add(new ConnectionPoolImpl(Servers.newServer("localhost", 6379),10,20,10,20));
        LoadBalanceStrategy<ConnectionPool> loadBalanceStrategy = new ConsistencyHashStrategy<ConnectionPool>(nodes);
        for (int j = 0 ; j < 1000000; j++) {
            String key = UUID.randomUUID().toString();
            ConnectionPool node = loadBalanceStrategy.select(key);
            for (int i = 0 ; i < 1000 ; i++) {
                Assert.assertEquals(node, loadBalanceStrategy.select(key));
            }
        }
    }

}
