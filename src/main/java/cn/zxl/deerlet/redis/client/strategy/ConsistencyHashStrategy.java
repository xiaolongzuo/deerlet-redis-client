/**
 * 
 */
package cn.zxl.deerlet.redis.client.strategy;

import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author zuoxiaolong
 *
 */
public class ConsistencyHashStrategy<T> implements LoadBalanceStrategy<T> {
	
	private static final int DEFAULT_VIRTUAL_NUMBER = 4;
	
	private final int virtualNumber = DEFAULT_VIRTUAL_NUMBER;

	private volatile TreeMap<Long, T> virtualNodeMap;
	
	private volatile List<T> nodes;
	
	public ConsistencyHashStrategy(List<T> nodes) {
		remap(nodes);
	}
	
	public synchronized void remap(List<T> nodes) {
		if (nodes == null || nodes.size() == 0) {
			throw new IllegalArgumentException("nodes can't be empty!");
		}
		TreeMap<Long, T> virtualNodeMap = new TreeMap<Long, T>();
		for (int i = 0; i < nodes.size(); i++) {
			T node = nodes.get(i);
			for (int j = 0; j < virtualNumber; j++) {
				virtualNodeMap.put(hash(node.toString() + j), node);
			}
		}
		this.virtualNodeMap = virtualNodeMap;
		this.nodes = Collections.unmodifiableList(nodes);
	} 
	
	public T select(String key) {
		if (key == null) {
			return select(UUID.randomUUID().toString());
		}
		Long hash = hash(key);
		SortedMap<Long, T> map = virtualNodeMap.tailMap(hash);
		if (map.isEmpty()) {
			return virtualNodeMap.get(virtualNodeMap.firstKey());
		} else {
			return virtualNodeMap.get(map.firstKey());
		}
	}
	
	private Long hash(String key) {
		return Long.valueOf(key.hashCode());
	}

	@Override
	public List<T> getAll() {
		return nodes;
	}
	
}
