/**
 * 
 */
package com.zuoxiaolong.deerlet.redis.client.connection;


/**
 *
 * 该接口定义了连接池需要提供的两个功能，获取连接和释放连接。
 * 注意，该接口的实现类必须是线程安全的。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 * @see Connection
 */
public interface ConnectionPool {

	public Connection obtainConnection();

	public void releaseConnection(Connection connection);
	
}