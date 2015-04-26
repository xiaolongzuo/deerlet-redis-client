/**
 * 
 */
package cn.zxl.deerlet.redis.client.strategy;

import java.util.Collections;
import java.util.List;

/**
 * @author zuoxiaolong
 *
 */
public class SimpleNodeStrategy<T> implements LoadBalanceStrategy<T> {
	
	private T node;
	
	private List<T> nodes;

	public SimpleNodeStrategy(List<T> nodes) {
		if (nodes == null || nodes.size() == 0) {
			throw new IllegalArgumentException("nodes can't be empty!");
		}
		this.node = nodes.get(0);
		this.nodes = Collections.unmodifiableList(nodes);
	}
	

	/* (non-Javadoc)
	 * @see cn.zxl.deerlet.redis.client.config.LoadBalanceStrategy#select(java.lang.String)
	 */
	@Override
	public T select(String key) {
		return node;
	}


	/* (non-Javadoc)
	 * @see cn.zxl.deerlet.redis.client.config.LoadBalanceStrategy#getAll()
	 */
	@Override
	public List<T> getAll() {
		return nodes;
	}

}
