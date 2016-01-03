/**
 * 
 */
package com.zuoxiaolong.deerlet.redis.client.strategy;

import org.apache.log4j.Logger;

import java.util.*;

/**
 *
 * 一致性hash策略的实现
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:43:51
 *
 */
public class ConsistencyHashStrategy<T> implements LoadBalanceStrategy<T> {

	private static final Logger LOGGER = Logger.getLogger(ConsistencyHashStrategy.class);
	
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
				virtualNodeMap.put(hash("node-" + i + "-virtual-" + j), node);
			}
		}
		this.virtualNodeMap = virtualNodeMap;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("virtualNodeMap is [" + virtualNodeMap + "]");
		}
		this.nodes = Collections.unmodifiableList(nodes);
	} 
	
	public T select(String key) {
		if (key == null) {
			return select(UUID.randomUUID().toString());
		}
		Long hash = hash(key);
		SortedMap<Long, T> map = virtualNodeMap.tailMap(hash);
		T result = null;
		if (map.isEmpty()) {
			result = virtualNodeMap.get(virtualNodeMap.firstKey());
		} else {
			result = virtualNodeMap.get(map.firstKey());
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("key is [" + key + "],hashcode is [" + hash + "],result is [" + result + "]");
		}
		return result;
	}
	
	private Long hash(String key) {
		return Long.valueOf(key.hashCode());
	}

	@Override
	public List<T> getAll() {
		return nodes;
	}
	
}
