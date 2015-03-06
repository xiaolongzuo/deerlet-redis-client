/**
 * 
 */
package cn.zxl.deerlet.redis.client.connection.pool;

import cn.zxl.deerlet.redis.client.connection.Connection;

/**
 * @author zuoxiaolong
 *
 */
public interface ConnectionPool {

	public Connection obtainConnection();

	public void releaseConnection(Connection connection);

}