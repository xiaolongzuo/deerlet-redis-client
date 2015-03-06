package cn.zxl.deerlet.redis.client.connection.pool;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.factory.ConnectionFactory;

/**
 * 
 * deerlet的连接池。该类是线程安全的。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:41:21
 *
 */
public class ConnectionPoolImpl implements ConnectionPool {

	private static final int INIT_SIZE = 10;

	private static final int MAX_SIZE = 100;

	private static final int MIN_IDLE_SIZE = 10;

	private static final int MAX_IDLE_SIZE = 20;

	private LinkedList<Connection> connectionPool;

	private ReentrantLock lock = new ReentrantLock();

	private Condition isHasConnectionReleased = lock.newCondition();

	private int maxSize = MAX_SIZE;

	private int minIdleSize = MIN_IDLE_SIZE;

	private int maxIdleSize = MAX_IDLE_SIZE;

	private int totalSize = 0;

	public ConnectionPoolImpl() {
		this(INIT_SIZE, MAX_SIZE, MIN_IDLE_SIZE, MAX_IDLE_SIZE);
	}

	public ConnectionPoolImpl(int initSize) {
		this(initSize, MAX_SIZE, MIN_IDLE_SIZE, MAX_IDLE_SIZE);
	}

	public ConnectionPoolImpl(int initSize, int maxSize) {
		this(initSize, maxSize, MIN_IDLE_SIZE, MAX_IDLE_SIZE);
	}

	public ConnectionPoolImpl(int initSize, int maxSize, int minIdleSize) {
		this(initSize, maxSize, minIdleSize, MAX_IDLE_SIZE);
	}

	public ConnectionPoolImpl(int initSize, int maxSize, int minIdleSize, int maxIdleSize) {
		if (initSize < minIdleSize || maxSize < initSize || maxSize < minIdleSize || maxIdleSize < minIdleSize || maxSize < maxIdleSize) {
			throw new IllegalArgumentException("must (initSize < minIdleSize) && (maxSize < initSize) && (maxSize < minIdleSize) && (maxIdleSize < minIdleSize) && (maxSize < maxIdleSize)");
		}
		connectionPool = new LinkedList<Connection>();
		incrementPool(initSize);
		this.maxSize = maxSize;
		this.minIdleSize = minIdleSize;
		this.maxIdleSize = maxIdleSize;
	}

	/* (non-Javadoc)
	 * @see cn.zxl.deerlet.redis.client.connection.pool.ConnectionPool#get()
	 */
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
				try {
					isHasConnectionReleased.await();
					if (connectionPool.size() > 0) {
						connection = connectionPool.removeLast();
					}
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			} finally {
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

	/* (non-Javadoc)
	 * @see cn.zxl.deerlet.redis.client.connection.pool.ConnectionPool#release(cn.zxl.deerlet.redis.client.connection.Connection)
	 */
	@Override
	public void releaseConnection(Connection connection) {
		if (!(connection instanceof ConnectionProxy)) {
			throw new RuntimeException("released connection must be proxied!");
		}
		lock.lock();
		try {
			connectionPool.add(connection);
			isHasConnectionReleased.signal();
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
				connectionPool.add(new ConnectionProxy(ConnectionFactory.getConnection(), this));
				totalSize++;
			}
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

}
