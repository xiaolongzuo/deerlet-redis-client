/**
 * 
 */
package cn.zxl.deerlet.redis.client.strategy;

import java.util.List;

/**
 *
 * 负载均衡接口，目前主要有两种实现，单点以及一致性Hash。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:43:51
 *
 * @see cn.zxl.deerlet.redis.client.strategy.ConsistencyHashStrategy
 * @see cn.zxl.deerlet.redis.client.strategy.SimpleNodeStrategy
 */
public interface LoadBalanceStrategy<T> {

	public T select(String key);
	
	public List<T> getAll();
	
}
