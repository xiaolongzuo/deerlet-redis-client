/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 缓存命令实例
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 */
public enum CommandCache {
	
	INSTANCE;
	
	private Map<Class<? extends Command<?>>, Command<?>> cache = new HashMap<Class<? extends Command<?>>, Command<?>>();
	
	@SuppressWarnings("unchecked")
	public <T> Command<T> get(Class<? extends Command<T>> clazz) {
		Command<?> commandInstance = cache.get(clazz);
		if (commandInstance != null) {
			return (Command<T>) commandInstance;
		}
		synchronized (cache) {
			commandInstance = cache.get(clazz);
			if (commandInstance == null) {
				try {
					commandInstance = clazz.getConstructor(new Class<?>[]{}).newInstance();
					cache.put(clazz, commandInstance);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return (Command<T>) commandInstance;
	}
	
}
