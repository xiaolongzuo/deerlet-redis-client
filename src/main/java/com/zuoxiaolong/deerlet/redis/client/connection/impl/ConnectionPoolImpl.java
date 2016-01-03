package com.zuoxiaolong.deerlet.redis.client.connection.impl;

import com.zuoxiaolong.deerlet.redis.client.config.Server;
import com.zuoxiaolong.deerlet.redis.client.connection.Connection;
import com.zuoxiaolong.deerlet.redis.client.connection.ConnectionPool;

import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * deerlet的连接池。该类是线程安全的。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:41:21
 *
 */
public class ConnectionPoolImpl implements ConnectionPool {

	private LinkedList<Connection> connectionPool;

	private ReentrantLock lock = new ReentrantLock();

	private Condition notEmpty = lock.newCondition();

	private int maxSize;

	private int minIdleSize;

	private int maxIdleSize;

	private int totalSize;

	private Server server;

	public ConnectionPoolImpl(Server server,int initSize, int maxSize, int minIdleSize, int maxIdleSize) {
		if (initSize < minIdleSize || maxSize < initSize || maxSize < minIdleSize || maxIdleSize < minIdleSize || maxSize < maxIdleSize) {
			throw new IllegalArgumentException("must (initSize < minIdleSize) && (maxSize < initSize) && (maxSize < minIdleSize) && (maxIdleSize < minIdleSize) && (maxSize < maxIdleSize)");
		}
		if (server == null) {
			throw new IllegalArgumentException("server can't be null!");
		}
		this.server = server;
		connectionPool = new LinkedList<Connection>();
		incrementPool(initSize);
		this.maxSize = maxSize;
		this.minIdleSize = minIdleSize;
		this.maxIdleSize = maxIdleSize;
	}

	@Override
	public Connection obtainConnection() {
		extend();
		Connection connection = null;
		if (connectionPool.size() > 0) {
			lock.lock();
			try {
				if (connectionPool.size() > 0) {
					connection = connectionPool.removeLast();
				}
			} finally {
				lock.unlock();
			}
		}
		while (connection == null) {
			lock.lock();
			try {
				notEmpty.await();
				if (connectionPool.size() > 0) {
					connection = connectionPool.removeLast();
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}finally {
				lock.unlock();
			}
		}
		return connection;
	}

	private void extend() {
		if (connectionPool.size() < minIdleSize) {
			lock.lock();
			try {
				if (connectionPool.size() < minIdleSize) {
					incrementPool(minIdleSize - connectionPool.size() + 1);
				}
			} finally {
				lock.unlock();
			}
		}
	}

	@Override
	public void releaseConnection(Connection connection) {
		if (!(connection instanceof ConnectionProxy)) {
			throw new RuntimeException("released connection must be proxied!");
		}
		lock.lock();
		try {
			connectionPool.add(connection);
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
		if (connectionPool.size() > maxIdleSize) {
			lock.lock();
			try {
				if (connectionPool.size() > maxIdleSize) {
					decrementPool(connectionPool.size() - maxIdleSize);
				}
			} finally {
				lock.unlock();
			}
		}
	}

	private void incrementPool(int number) {
		if (totalSize >= maxSize) {
			return;
		}
		if (maxSize - totalSize < number) {
			number = maxSize - totalSize;
		}
		lock.lock();
		try {
			for (int i = 0; i < number; i++) {
				connectionPool.add(newConnection());
				totalSize++;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

	private void decrementPool(int number) {
		lock.lock();
		try {
			for (int i = 0; i < number; i++) {
				try {
					((ConnectionProxy) connectionPool.removeLast()).realClose();
				} catch (NoSuchElementException e) {
					break;
				}
				totalSize--;
			}
		} finally {
			lock.unlock();
		}
	}

	private Connection newConnection() throws IOException {
		return new ConnectionProxy(new ConnectionImpl(server), this);
	}

}
