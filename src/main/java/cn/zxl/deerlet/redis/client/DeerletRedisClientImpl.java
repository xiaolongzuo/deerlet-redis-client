package cn.zxl.deerlet.redis.client;

import java.util.List;

import cn.zxl.deerlet.redis.client.command.IntResultCommand;
import cn.zxl.deerlet.redis.client.command.Command;
import cn.zxl.deerlet.redis.client.command.Commands;
import cn.zxl.deerlet.redis.client.command.LInsertOptions;
import cn.zxl.deerlet.redis.client.command.ListResultCommand;
import cn.zxl.deerlet.redis.client.command.StringResultCommand;
import cn.zxl.deerlet.redis.client.command.BooleanResultCommand;
import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.connection.pool.ConnectionPool;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

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
		return executeCommand(BooleanResultCommand.class, Commands.set, key, value);
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
		return executeCommand(IntResultCommand.class, Commands.append, key, value);
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
	public boolean bgRewriteAof() {
		return executeCommand(BooleanResultCommand.class, Commands.bgrewriteaof);
	}
	
	@Override
	public boolean exists(String key) {
		return ProtocolUtil.intResultToBooleanResult(executeCommand(IntResultCommand.class, Commands.exists, key));
	}
	
	@Override
	public boolean expire(String key, int seconds) {
		return ProtocolUtil.intResultToBooleanResult(executeCommand(IntResultCommand.class, Commands.expire, key, seconds));
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

	@Override
	public boolean lset(String listKey, int index, Object value) {
		return executeCommand(BooleanResultCommand.class, Commands.lset, listKey, index, value);
	}

	@Override
	public int lpush(String listKey, Object... values) {
		return executeCommand(IntResultCommand.class, Commands.lpush, listKey, values);
	}

	@Override
	public List<String> lrange(String listKey, int start, int stop) {
		return executeCommand(ListResultCommand.class, Commands.lrange, listKey, start, stop);
	}

	@Override
	public int llen(String listKey) {
		return executeCommand(IntResultCommand.class, Commands.llen, listKey);
	}

	@Override
	public int lpushx(String listKey, Object value) {
		return executeCommand(IntResultCommand.class, Commands.lpushx, listKey, value);
	}

	@Override
	public String lpop(String listKey) {
		return executeCommand(StringResultCommand.class, Commands.lpop, listKey);
	}

	@Override
	public int lrem(String listKey, int count, Object value) {
		return executeCommand(IntResultCommand.class, Commands.lrem, listKey, count, value);
	}

	@Override
	public String lindex(String listKey, int index) {
		return executeCommand(StringResultCommand.class, Commands.lindex, listKey, index);
	}

	@Override
	public int linsert(String listKey, LInsertOptions option, Object pivot, Object value) {
		return executeCommand(IntResultCommand.class, Commands.linsert, listKey, option, pivot, value);
	}

	@Override
	public boolean ltrim(String listKey, int start, int stop) {
		return executeCommand(BooleanResultCommand.class, Commands.ltrim, listKey, start, stop);
	}
	
}
