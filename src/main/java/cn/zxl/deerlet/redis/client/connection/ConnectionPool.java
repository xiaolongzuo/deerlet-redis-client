/**
 * 
 */
package cn.zxl.deerlet.redis.client.connection;


/**
 * @author zuoxiaolong
 *
 */
public interface ConnectionPool {

	public Connection obtainConnection();

	public void releaseConnection(Connection connection);
	
}