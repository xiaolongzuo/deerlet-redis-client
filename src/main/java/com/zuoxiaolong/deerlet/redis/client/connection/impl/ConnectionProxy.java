package com.zuoxiaolong.deerlet.redis.client.connection.impl;

import com.zuoxiaolong.deerlet.redis.client.connection.Connection;
import com.zuoxiaolong.deerlet.redis.client.connection.ConnectionPool;
import com.zuoxiaolong.deerlet.redis.client.io.MultibulkInputStream;
import com.zuoxiaolong.deerlet.redis.client.io.MultibulkOutputStream;

/**
 * 
 * 连接类的代理，为了屏蔽客户端对连接的错误关闭。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:42:19
 *
 */
public class ConnectionProxy implements Connection {

	private Connection connection;

	private ConnectionPool connectionPool;
	
	public ConnectionProxy(Connection connection, ConnectionPool connectionPool) {
		this.connection = connection;
		this.connectionPool = connectionPool;
	}

	public MultibulkOutputStream getOutputStream() {
		return connection.getOutputStream();
	}

	public MultibulkInputStream getInputStream() {
		return connection.getInputStream();
	}

	public boolean isClosed() {
		return connection.isClosed();
	}

	public void close() {
		connectionPool.releaseConnection(this);
	}

	void realClose() {
		connection.close();
	}

}
