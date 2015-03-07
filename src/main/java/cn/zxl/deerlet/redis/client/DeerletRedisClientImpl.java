package cn.zxl.deerlet.redis.client;

import cn.zxl.deerlet.redis.client.command.AppendCommand;
import cn.zxl.deerlet.redis.client.command.Command;
import cn.zxl.deerlet.redis.client.command.Commands;
import cn.zxl.deerlet.redis.client.command.DbSizeCommand;
import cn.zxl.deerlet.redis.client.command.GetCommand;
import cn.zxl.deerlet.redis.client.command.ResultlessCommand;
import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.connection.pool.ConnectionPool;
import cn.zxl.deerlet.redis.client.util.TypeUtil;

/**
 * 
 * 客户端的默认实现，采用连接池管理连接。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:38:36
 *
 */
public class DeerletRedisClientImpl implements DeerletRedisClient {

	private ConnectionPool connectionPool;

	public DeerletRedisClientImpl(ConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}

	public Connection obtainConnection() {
		return connectionPool.obtainConnection();
	}

	@Override
	public boolean set(String key, String value) {
		Command<Boolean> command = new ResultlessCommand(obtainConnection(), Commands.set);
		return command.execute(key, TypeUtil.asString(value));
	}

	@Override
	public String get(String key) {
		Command<String> command = new GetCommand(obtainConnection());
		return command.execute(key);
	}

	@Override
	public boolean flushAll() {
		Command<Boolean> command = new ResultlessCommand(obtainConnection(), Commands.flushall);
		return command.execute();
	}

	@Override
	public int dbSize() {
		Command<Integer> command = new DbSizeCommand(obtainConnection());
		return command.execute();
	}

	@Override
	public int append(String key, String value) {
		Command<Integer> command = new AppendCommand(obtainConnection());
		return command.execute(key,TypeUtil.asString(value));
	}

	@Override
	public boolean flushDb() {
		Command<Boolean> command = new ResultlessCommand(obtainConnection(), Commands.flushdb);
		return command.execute();
	}

}
