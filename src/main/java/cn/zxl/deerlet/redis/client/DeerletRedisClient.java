package cn.zxl.deerlet.redis.client;

/**
 * 
 * deerlet客户端的客户端操作接口，其中定义了本组件支持的redis操作。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:37:35
 *
 */
public interface DeerletRedisClient {

	public boolean set(String key, String value);
	
	public int append(String key,String value);

	public String get(String key);

	public boolean flushAll();
	
	public boolean flushDb();

	public int dbSize();
	
	

}
