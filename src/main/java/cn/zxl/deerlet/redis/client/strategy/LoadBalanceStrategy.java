/**
 * 
 */
package cn.zxl.deerlet.redis.client.strategy;

import java.util.List;

/**
 * @author zuoxiaolong
 *
 */
public interface LoadBalanceStrategy<T> {

	public T select(String key);
	
	public List<T> getAll();
	
}
