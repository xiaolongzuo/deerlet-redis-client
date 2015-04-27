package cn.zxl.deerlet.redis.client;

import cn.zxl.deerlet.redis.client.command.*;
import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.connection.ConnectionFactory;
import cn.zxl.deerlet.redis.client.connection.ConnectionPool;
import cn.zxl.deerlet.redis.client.strategy.SimpleNodeStrategy;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 客户端的默认实现，采用连接池管理连接。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:38:36
 *
 */
public class DeerletRedisClientImpl implements DeerletRedisClient {
	
	protected final Logger LOGGER = Logger.getLogger(getClass());

	private ConnectionFactory connectionFactory;
	
	public DeerletRedisClientImpl(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	protected int getServerSize() {
		return connectionFactory.getLoadBalanceStrategy().getAll().size();
	}

	protected <T> T executeCommand(Class<? extends Command<T>> commandClass, Commands command, Object... arguments) {
		Command<T> commandInstance = CommandCache.INSTANCE.get(commandClass);
		T result = null;
		List<ConnectionPool> connectionPools = connectionFactory.getLoadBalanceStrategy().getAll();
		for (int i = 0; i < connectionPools.size(); i++) {
			if (result == null) {
				result = executeCommand(connectionPools.get(i).obtainConnection(), commandInstance, command, arguments);
			} else {
				result = commandInstance.merge(result, executeCommand(connectionPools.get(i).obtainConnection(), commandInstance, command, arguments));
			}
		}
		return result;
	}

	protected <T> T executeCommand(String key, Class<? extends Command<T>> commandClass, Commands command, Object... arguments) {
		Command<T> commandInstance = CommandCache.INSTANCE.get(commandClass);
		return executeCommand(connectionFactory.createConnection(key), commandInstance, command, arguments);
	}

	protected <T> T executeCommand(Connection connection, Command<T> commandInstance, Commands command, Object... arguments) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("execute command[" + command + "],use connection :" + connection);
			}
			return commandInstance.execute(connection, command, arguments);
		} catch (Exception e) {
			throw new RuntimeException("init command class failed!", e);
		}
	}
	
	/* **************key************* */

	@Override
	public int del(String key) {
		return executeCommand(key, IntResultCommand.class, Commands.del, key);
	}

	@Override
	public byte[] dump(String key) {
		return executeCommand(key, ByteArrayResultCommand.class, Commands.dump, key);
	}

	@Override
	public boolean exists(String key) {
		return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.exists, key));
	}

	@Override
	public boolean expire(String key, int seconds) {
		return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.expire, key, seconds));
	}

	@Override
	public boolean expireat(String key, int time) {
		return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.expireat, key, time));
	}

	@Override
	public List<String> keys(String pattern) {
		return executeCommand(ListResultCommand.class, Commands.keys, pattern);
	}

	@Override
	public boolean migrate(String host, String port, String key, int dbNumber, long timeout) {
		return executeCommand(key, BooleanResultCommand.class, Commands.migrate, host, port, key, dbNumber, timeout);
	}

	@Override
	public boolean move(String key, int dbNumber) {
		return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.move, key, dbNumber));
	}

	@Override
	public Object object(ObjectSubcommands subcommand, String key) {
		if (subcommand == ObjectSubcommands.encoding) {
			return executeCommand(key, StringResultCommand.class, Commands.object, subcommand, key);
		} else {
			return executeCommand(key, IntResultCommand.class, Commands.object, subcommand, key);
		}
	}

	@Override
	public boolean persist(String key) {
		return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.persist, key));
	}

	@Override
	public boolean pexpire(String key, long milliseconds) {
		return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.pexpire, key, milliseconds));
	}

	@Override
	public boolean pexpireat(String key, long milliseconds) {
		return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.pexpireat, key, milliseconds));
	}

	@Override
	public long pttl(String key) {
		return executeCommand(key, LongResultCommand.class, Commands.pttl, key);
	}

	@Override
	public String randomkey() {
		return executeCommand(null, StringResultCommand.class, Commands.randomkey);
	}

	@Override
	public boolean rename(String key, String newKey) {
		return executeCommand(key, BooleanResultCommand.class, Commands.rename, key, newKey);
	}

	@Override
	public boolean renamenx(String key, String newKey) {
		return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.renamenx, key, newKey));
	}

	@Override
	public boolean restore(String key, int ttl, byte[] serializedValue) {
		return executeCommand(key, BooleanResultCommand.class, Commands.restore, new Object[] { key, ttl, serializedValue });
	}

	@Override
	public List<String> sort(String key, Object... arguments) {
		return executeCommand(key, ListResultCommand.class, Commands.sort, key, arguments);
	}

	@Override
	public int ttl(String key) {
		return executeCommand(key, IntResultCommand.class, Commands.ttl, key);
	}

	@Override
	public Types type(String key) {
		return executeCommand(key, TypesResultCommand.class, Commands.type, key);
	}

	@Override
	public Cursor scan(Cursor cursor, String pattern, Integer count) {
		if (cursor == null) {
			cursor = DefaultCursor.EMPTY_CURSOR;
		}
		List<Integer> cursorList = cursor.getCursorList();
		List<ConnectionPool> connectionPools = connectionFactory.getLoadBalanceStrategy().getAll();
		if (cursorList.size() != connectionPools.size() && cursor != DefaultCursor.EMPTY_CURSOR) {
			throw new IllegalArgumentException("cursorList.size() != connectionPools.size()!");
		}
		Command<Cursor> cursorResultCommand = CommandCache.INSTANCE.get(CursorResultCommand.class);
		Cursor result = new DefaultCursor(new ArrayList<Integer>(), new ArrayList<String>());
		for (int i = 0; i < connectionPools.size(); i++) {
			List<Object> arguments = new ArrayList<Object>();
			if (cursor == DefaultCursor.EMPTY_CURSOR) {
				arguments.add(0);
			} else if (cursorList.get(i) < 0) {
				result = cursorResultCommand.merge(result, new DefaultCursor(Arrays.asList(-1), new ArrayList<String>()));
				continue;
			} else {
				arguments.add(cursorList.get(i));
			}
			if (pattern != null) {
				arguments.add("match");
				arguments.add(pattern);
			}
			if (count != null) {
				arguments.add("count");
				arguments.add(count);
			}
			result = cursorResultCommand.merge(result, executeCommand(connectionPools.get(i).obtainConnection(), cursorResultCommand, Commands.scan, arguments.toArray()));
		}
		return result;
	}

	/* **************string**************** */

	@Override
	public int append(String key, String value) {
		return executeCommand(key, IntResultCommand.class, Commands.append, key, value);
	}

	@Override
	public int bitcount(String key, Integer start, Integer stop) {
		if (start == null && stop == null) {
			return executeCommand(key, IntResultCommand.class, Commands.bitcount, key);
		} else if (start != null && stop == null) {
			return executeCommand(key, IntResultCommand.class, Commands.bitcount, key, start);
		} else {
			return executeCommand(key, IntResultCommand.class, Commands.bitcount, key, start, stop);
		}
	}

	@Override
	public int bitop(BitopOperations operation, String destKey, String... keys) {
		return executeCommand(destKey, IntResultCommand.class, Commands.bitop, operation, destKey, keys);
	}

	@Override
	public int decr(String key) {
		return executeCommand(key, IntResultCommand.class, Commands.decr, key);
	}

	@Override
	public int decrby(String key, int decrement) {
		return executeCommand(key, IntResultCommand.class, Commands.decrby, key, decrement);
	}

	@Override
	public String get(String key) {
		return executeCommand(key, StringResultCommand.class, Commands.get, key);
	}

	@Override
	public int getbit(String key, int offset) {
		return executeCommand(key, IntResultCommand.class, Commands.getbit, key, offset);
	}

	@Override
	public String getrange(String key, Integer start, Integer stop) {
		if (start == null && stop == null) {
			return executeCommand(key, StringResultCommand.class, Commands.getrange, key);
		} else if (start != null && stop == null) {
			return executeCommand(key, StringResultCommand.class, Commands.getrange, key, start);
		} else {
			return executeCommand(key, StringResultCommand.class, Commands.getrange, key, start, stop);
		}
	}

	@Override
	public String getset(String key, String value) {
		return executeCommand(key, StringResultCommand.class, Commands.getset, key, value);
	}

	@Override
	public int incr(String key) {
		return executeCommand(key, IntResultCommand.class, Commands.incr, key);
	}

	@Override
	public int incrby(String key, int increment) {
		return executeCommand(key, IntResultCommand.class, Commands.incrby, key, increment);
	}

	@Override
	public float incrbyfloat(String key, float increment) {
		return Float.valueOf(executeCommand(key, StringResultCommand.class, Commands.incrbyfloat, key, increment));
	}

	@Override
	public List<String> mget(String... keys) {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < keys.length; i++) {
			result.add(executeCommand(keys[i], StringResultCommand.class, Commands.get, keys[i]));
		}
		return result;
	}

	@Override
	public boolean mset(String[] keys, Object... values) {
		if (keys.length != values.length) {
			throw new IllegalArgumentException("keys.length must equals values.length");
		} 
		if (connectionFactory.getLoadBalanceStrategy() instanceof SimpleNodeStrategy<?>) {
			Object[] arguments = new Object[keys.length + values.length];
			for (int i = 0 ,index = 0; i < values.length; index++) {
				arguments[i++] = keys[index];
				arguments[i++] = values[index];
			}
			return executeCommand(BooleanResultCommand.class, Commands.mset, arguments);
		}
		boolean result = true;
		for (int i = 0; i < keys.length; i++) {
			result &= executeCommand(keys[i], BooleanResultCommand.class, Commands.set, keys[i] ,values[i]);
			if (!result) {
				for (int j = i; j >= 0; j--) {
					del(keys[j]);
				}
			}
		}
		return result;
	}

	@Override
	public boolean msetnx(String[] keys, Object... values) {
		if (keys.length != values.length) {
			throw new IllegalArgumentException("keys.length must equals values.length");
		} 
		boolean exists = false;
		for (int i = 0; i < keys.length; i++) {
			if (exists(keys[i])) {
				exists = true;
				break;
			}
		}
		if (exists) {
			return false;
		}
		return mset(keys, values);
	}

	@Override
	public boolean psetex(String key, long milliseconds, Object value) {
		return executeCommand(key, BooleanResultCommand.class, Commands.psetex, key, milliseconds, value);
	}

	@Override
	public boolean set(String key, Object value) {
		return executeCommand(key, BooleanResultCommand.class, Commands.set, key, value);
	}

	@Override
	public Bit setbit(String key, int offset, Bit bit) {
		return Bit.create(executeCommand(key, IntResultCommand.class, Commands.setbit, key, offset, bit));
	}

	@Override
	public boolean setex(String key, int seconds, Object value) {
		return executeCommand(key, BooleanResultCommand.class, Commands.setex, key, seconds, value);
	}

	@Override
	public boolean setnx(String key, Object value) {
		return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.setnx, key, value));
	}

	@Override
	public int setrange(String key, int offset, Object value) {
		return executeCommand(key, IntResultCommand.class, Commands.setrange, key, offset, value);
	}

	@Override
	public int strlen(String key) {
		return executeCommand(key, IntResultCommand.class, Commands.strlen, key);
	}

	@Override
	public int hdel(String key,String... fields) {
		return executeCommand(key, IntResultCommand.class, Commands.hdel, key,fields);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hexists()
	 */
	@Override
	public void hexists() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hget()
	 */
	@Override
	public String hget(String key,String field) {
		return executeCommand(key, StringResultCommand.class, Commands.hget, key,field);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hgetall()
	 */
	@Override
	public void hgetall() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hincrby()
	 */
	@Override
	public void hincrby() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hincrbyfloat()
	 */
	@Override
	public void hincrbyfloat() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hkeys()
	 */
	@Override
	public void hkeys() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hlen()
	 */
	@Override
	public void hlen() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hmget()
	 */
	@Override
	public void hmget() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hmset()
	 */
	@Override
	public void hmset() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hset()
	 */
	@Override
	public int hset(String key,String field,String value) {
		return executeCommand(key, IntResultCommand.class, Commands.hset, key,field,value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hsetnx()
	 */
	@Override
	public void hsetnx() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hvals()
	 */
	@Override
	public void hvals() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#hscan()
	 */
	@Override
	public void hscan() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#blpop()
	 */
	@Override
	public void blpop() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#brpop()
	 */
	@Override
	public void brpop() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#brpoplpush()
	 */
	@Override
	public void brpoplpush() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean lset(String listKey, int index, Object value) {
		return executeCommand(listKey, BooleanResultCommand.class, Commands.lset, listKey, index, value);
	}

	@Override
	public int lpush(String listKey, Object... values) {
		return executeCommand(listKey, IntResultCommand.class, Commands.lpush, listKey, Arrays.asList(values));
	}

	@Override
	public List<String> lrange(String listKey, int start, int stop) {
		return executeCommand(listKey, ListResultCommand.class, Commands.lrange, listKey, start, stop);
	}

	@Override
	public int llen(String listKey) {
		return executeCommand(listKey, IntResultCommand.class, Commands.llen, listKey);
	}

	@Override
	public int lpushx(String listKey, Object value) {
		return executeCommand(listKey, IntResultCommand.class, Commands.lpushx, listKey, value);
	}

	@Override
	public String lpop(String listKey) {
		return executeCommand(listKey, StringResultCommand.class, Commands.lpop, listKey);
	}

	@Override
	public int lrem(String listKey, int count, Object value) {
		return executeCommand(listKey, IntResultCommand.class, Commands.lrem, listKey, count, value);
	}

	@Override
	public String lindex(String listKey, int index) {
		return executeCommand(listKey, StringResultCommand.class, Commands.lindex, listKey, index);
	}

	@Override
	public int linsert(String listKey, LInsertOptions option, Object pivot, Object value) {
		return executeCommand(listKey, IntResultCommand.class, Commands.linsert, listKey, option, pivot, value);
	}

	@Override
	public boolean ltrim(String listKey, int start, int stop) {
		return executeCommand(listKey, BooleanResultCommand.class, Commands.ltrim, listKey, start, stop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#rpop()
	 */
	@Override
	public void rpop() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#rpoplpush()
	 */
	@Override
	public void rpoplpush() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#rpush()
	 */
	@Override
	public void rpush() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#rpushx()
	 */
	@Override
	public void rpushx() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#sadd()
	 */
	@Override
	public void sadd() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#scard()
	 */
	@Override
	public void scard() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#sdiff()
	 */
	@Override
	public void sdiff() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#sdiffstore()
	 */
	@Override
	public void sdiffstore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#sinter()
	 */
	@Override
	public void sinter() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#sinterstore()
	 */
	@Override
	public void sinterstore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#sismember()
	 */
	@Override
	public void sismember() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#smembers()
	 */
	@Override
	public void smembers() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#smove()
	 */
	@Override
	public void smove() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#spop()
	 */
	@Override
	public void spop() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#srandmember()
	 */
	@Override
	public void srandmember() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#srem()
	 */
	@Override
	public void srem() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#sunion()
	 */
	@Override
	public void sunion() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#sunionstore()
	 */
	@Override
	public void sunionstore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#sscan()
	 */
	@Override
	public void sscan() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zadd()
	 */
	@Override
	public void zadd() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zcard()
	 */
	@Override
	public void zcard() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zcount()
	 */
	@Override
	public void zcount() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zincrby()
	 */
	@Override
	public void zincrby() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zrange()
	 */
	@Override
	public void zrange() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zrangebyscore()
	 */
	@Override
	public void zrangebyscore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zrank()
	 */
	@Override
	public void zrank() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zrem()
	 */
	@Override
	public void zrem() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zremrangebyrank()
	 */
	@Override
	public void zremrangebyrank() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zremrangebyscore()
	 */
	@Override
	public void zremrangebyscore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zrevrange()
	 */
	@Override
	public void zrevrange() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zrevrangebyscore()
	 */
	@Override
	public void zrevrangebyscore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zrevrank()
	 */
	@Override
	public void zrevrank() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zscore()
	 */
	@Override
	public void zscore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zunionstore()
	 */
	@Override
	public void zunionstore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zinterstore()
	 */
	@Override
	public void zinterstore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zscan()
	 */
	@Override
	public void zscan() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zrangebylex()
	 */
	@Override
	public void zrangebylex() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zlexcount()
	 */
	@Override
	public void zlexcount() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#zremrangebylex()
	 */
	@Override
	public void zremrangebylex() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#pfadd()
	 */
	@Override
	public void pfadd() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#pfcount()
	 */
	@Override
	public void pfcount() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#pfmerge()
	 */
	@Override
	public void pfmerge() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#psubscribe()
	 */
	@Override
	public void psubscribe() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#publish()
	 */
	@Override
	public void publish() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#pubsub()
	 */
	@Override
	public void pubsub() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#punsubscribe()
	 */
	@Override
	public void punsubscribe() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#subscribe()
	 */
	@Override
	public void subscribe() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#unsubscribe()
	 */
	@Override
	public void unsubscribe() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#discard()
	 */
	@Override
	public void discard() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#exec()
	 */
	@Override
	public void exec() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#multi()
	 */
	@Override
	public void multi() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#unwatch()
	 */
	@Override
	public void unwatch() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#watch()
	 */
	@Override
	public void watch() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#eval()
	 */
	@Override
	public void eval() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#evalsha()
	 */
	@Override
	public void evalsha() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#scriptexists()
	 */
	@Override
	public void scriptexists() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#scriptflush()
	 */
	@Override
	public void scriptflush() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#scriptkill()
	 */
	@Override
	public void scriptkill() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#scriptload()
	 */
	@Override
	public void scriptload() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#auth()
	 */
	@Override
	public void auth() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#echo()
	 */
	@Override
	public void echo() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#ping()
	 */
	@Override
	public void ping() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#quit()
	 */
	@Override
	public void quit() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#select()
	 */
	@Override
	public boolean select(int index) {
		return executeCommand(BooleanResultCommand.class, Commands.select, index);
	}

	@Override
	public boolean bgsave() {
		return executeCommand(BooleanResultCommand.class, Commands.bgsave);
	}

	@Override
	public boolean bgrewriteaof() {
		return executeCommand(BooleanResultCommand.class, Commands.bgrewriteaof);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#clientgetname()
	 */
	@Override
	public void clientgetname() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#clientkill()
	 */
	@Override
	public void clientkill() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#clientlist()
	 */
	@Override
	public void clientlist() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#clientsetname()
	 */
	@Override
	public void clientsetname() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#configget()
	 */
	@Override
	public void configget() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#config()
	 */
	@Override
	public void config() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#resetstat()
	 */
	@Override
	public void resetstat() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#configrewrite()
	 */
	@Override
	public void configrewrite() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#configset()
	 */
	@Override
	public void configset() {
		// TODO Auto-generated method stub

	}

	@Override
	public int dbsize() {
		return executeCommand(IntResultCommand.class, Commands.dbsize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#debugobject()
	 */
	@Override
	public void debugobject() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#debugsegfault()
	 */
	@Override
	public void debugsegfault() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean flushall() {
		return executeCommand(BooleanResultCommand.class, Commands.flushall);
	}

	@Override
	public boolean flushdb() {
		return executeCommand(BooleanResultCommand.class, Commands.flushdb);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#info()
	 */
	@Override
	public void info() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#lastsave()
	 */
	@Override
	public void lastsave() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#monitor()
	 */
	@Override
	public void monitor() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#psync()
	 */
	@Override
	public void psync() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#save()
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#shutdown()
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#slaveof()
	 */
	@Override
	public void slaveof() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#showlog()
	 */
	@Override
	public void showlog() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#sync()
	 */
	@Override
	public void sync() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#time()
	 */
	@Override
	public void time() {
		// TODO Auto-generated method stub

	}

}
