package cn.zxl.deerlet.redis.client;

import cn.zxl.deerlet.redis.client.command.IntResultCommand;
import cn.zxl.deerlet.redis.client.command.Command;
import cn.zxl.deerlet.redis.client.command.Commands;
import cn.zxl.deerlet.redis.client.command.StringResultCommand;
import cn.zxl.deerlet.redis.client.command.BooleanResultCommand;
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

	protected Connection obtainConnection() {
		return connectionPool.obtainConnection();
	}
	
	protected <T> T executeCommand(Class<? extends Command<T>> commandClass, Commands command, Object... arguments){
		try {
			Command<T> commandInstance = commandClass.getConstructor(Connection.class, Commands.class).newInstance(obtainConnection(), command);
			return commandInstance.execute(arguments);
		} catch (Exception e) {
			throw new RuntimeException("init command class failed!");
		}
	}

	@Override
	public boolean set(String key, Object value) {
		return executeCommand(BooleanResultCommand.class, Commands.set, key, TypeUtil.asString(value));
	}

	@Override
	public String get(String key) {
		return executeCommand(StringResultCommand.class, Commands.get, key);
	}

	@Override
	public boolean flushAll() {
		return executeCommand(BooleanResultCommand.class, Commands.flushall);
	}

	@Override
	public int dbSize() {
		return executeCommand(IntResultCommand.class, Commands.dbsize);
	}

	@Override
	public int append(String key, String value) {
		return executeCommand(IntResultCommand.class, Commands.append, key, TypeUtil.asString(value));
	}

	@Override
	public boolean flushDb() {
		return executeCommand(BooleanResultCommand.class, Commands.flushdb);
	}

	@Override
	public boolean bgSave() {
		return executeCommand(BooleanResultCommand.class, Commands.bgsave);
	}

	@Override
	public int decr(String key) {
		return executeCommand(IntResultCommand.class, Commands.decr, key);
	}

	@Override
	public int decrBy(String key, int decrement) {
		return executeCommand(IntResultCommand.class, Commands.decrby, key ,decrement);
	}

	@Override
	public int del(String key) {
		return executeCommand(IntResultCommand.class, Commands.del, key);
	}

	@Override
	public int incr(String key) {
		return executeCommand(IntResultCommand.class, Commands.incr, key);
	}

	@Override
	public int incrBy(String key, int increment) {
		return executeCommand(IntResultCommand.class, Commands.incrby, key, increment);
	}

	@Override
	public float incrByFloat(String key, float increment) {
		return Float.valueOf(executeCommand(StringResultCommand.class, Commands.incrbyfloat, key, increment));
	}
	
}
