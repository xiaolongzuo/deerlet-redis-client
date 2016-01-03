/**
 * 
 */
package com.zuoxiaolong.deerlet.redis.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zuoxiaolong.deerlet.redis.client.command.BitopOperations;
import com.zuoxiaolong.deerlet.redis.client.command.BooleanResultCommand;
import com.zuoxiaolong.deerlet.redis.client.command.Commands;
import com.zuoxiaolong.deerlet.redis.client.command.Cursor;
import com.zuoxiaolong.deerlet.redis.client.command.CursorResultCommand;
import com.zuoxiaolong.deerlet.redis.client.command.DefaultCursor;
import com.zuoxiaolong.deerlet.redis.client.command.IntResultCommand;
import com.zuoxiaolong.deerlet.redis.client.command.LInsertOptions;
import com.zuoxiaolong.deerlet.redis.client.command.ListResultCommand;
import com.zuoxiaolong.deerlet.redis.client.connection.ConnectionPool;
import com.zuoxiaolong.deerlet.redis.client.strategy.LoadBalanceStrategy;

/**
 * @author zuoxiaolong
 *
 */
public class SimpleNodeDeerletRedisClient extends AbstractDeerletRedisClient {

	public SimpleNodeDeerletRedisClient(LoadBalanceStrategy<ConnectionPool> strategy) {
		super(strategy);
	}

	@Override
	public int del(String... keys) {
		return executeCommand(null, IntResultCommand.class, Commands.del, Arrays.asList(keys).toArray());
	}

	@Override
	public List<String> keys(String pattern) {
		return executeCommand(null, ListResultCommand.class, Commands.keys, pattern);
	}

	@Override
	public boolean rename(String key, String newKey) {
		return executeCommand(null, BooleanResultCommand.class, Commands.rename, key, newKey);
	}

	@Override
	public boolean renamenx(String key, String newKey) {
		return executeCommand(null, BooleanResultCommand.class, Commands.renamenx, key, newKey);
	}

	@Override
	public Cursor scan(Cursor cursor, String pattern, Integer count) {
		List<Object> arguments = new ArrayList<Object>();
		if (cursor == null || cursor == DefaultCursor.EMPTY_CURSOR || cursor.getCursorList() == null || cursor.getCursorList().size() == 0) {
			arguments.add(0);
		} else {
			arguments.add(cursor.getCursorList().get(0));
		}
		if (pattern != null) {
			arguments.add("match");
			arguments.add(pattern);
		}
		if (count != null) {
			arguments.add("count");
			arguments.add(count);
		}
		return executeCommand(null, CursorResultCommand.class, Commands.scan, arguments.toArray());
	}

	@Override
	public int bitop(BitopOperations operation, String destKey, String... keys) {
		return executeCommand(destKey, IntResultCommand.class, Commands.bitop, operation, destKey, keys);
	}

	@Override
	public List<String> mget(String... keys) {
		return executeCommand(null, ListResultCommand.class, Commands.mget, Arrays.asList(keys).toArray());
	}

	@Override
	public boolean mset(String[] keys, Object... values) {
		if (keys.length != values.length) {
			throw new IllegalArgumentException("keys.length != values.length!");
		}
		Object[] arguments = new Object[keys.length + values.length];
		for (int i = 0 ,index = 0; i < keys.length; i++) {
			arguments[index++] = keys[i];
			arguments[index++] = values[i];
		}
		return executeCommand(null, BooleanResultCommand.class, Commands.mset, arguments);
	}

	@Override
	public boolean msetnx(String[] keys, Object... values) {
		if (keys.length != values.length) {
			throw new IllegalArgumentException("keys.length != values.length!");
		}
		Object[] arguments = new Object[keys.length + values.length];
		for (int i = 0 ,index = 0; i < keys.length; i++) {
			arguments[index++] = keys[i];
			arguments[index++] = values[i];
		}
		return executeCommand(null, BooleanResultCommand.class, Commands.msetnx, arguments);
	}

	@Override
	public void hdel() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hexists()
	 */
	@Override
	public void hexists() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hget()
	 */
	@Override
	public void hget() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hgetall()
	 */
	@Override
	public void hgetall() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hincrby()
	 */
	@Override
	public void hincrby() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hincrbyfloat()
	 */
	@Override
	public void hincrbyfloat() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hkeys()
	 */
	@Override
	public void hkeys() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hlen()
	 */
	@Override
	public void hlen() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hmget()
	 */
	@Override
	public void hmget() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hmset()
	 */
	@Override
	public void hmset() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hset()
	 */
	@Override
	public void hset() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hsetnx()
	 */
	@Override
	public void hsetnx() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hvals()
	 */
	@Override
	public void hvals() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#hscan()
	 */
	@Override
	public void hscan() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#blpop()
	 */
	@Override
	public void blpop() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#brpop()
	 */
	@Override
	public void brpop() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#brpoplpush()
	 */
	@Override
	public void brpoplpush() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DeerletRedisClient#lset(java.lang.String,
	 * int, java.lang.Object)
	 */
	@Override
	public boolean lset(String listKey, int index, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DeerletRedisClient#lpush(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public int lpush(String listKey, Object... values) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DeerletRedisClient#lrange(java.lang.String,
	 * int, int)
	 */
	@Override
	public List<String> lrange(String listKey, int start, int stop) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DeerletRedisClient#llen(java.lang.String)
	 */
	@Override
	public int llen(String listKey) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DeerletRedisClient#lpushx(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public int lpushx(String listKey, Object value) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DeerletRedisClient#lpop(java.lang.String)
	 */
	@Override
	public String lpop(String listKey) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DeerletRedisClient#lrem(java.lang.String,
	 * int, java.lang.Object)
	 */
	@Override
	public int lrem(String listKey, int count, Object value) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DeerletRedisClient#lindex(java.lang.String,
	 * int)
	 */
	@Override
	public String lindex(String listKey, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DeerletRedisClient#linsert(java.lang.String,
	 * LInsertOptions, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public int linsert(String listKey, LInsertOptions option, Object pivot, Object value) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DeerletRedisClient#ltrim(java.lang.String,
	 * int, int)
	 */
	@Override
	public boolean ltrim(String listKey, int start, int stop) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#rpop()
	 */
	@Override
	public void rpop() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#rpoplpush()
	 */
	@Override
	public void rpoplpush() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#rpush()
	 */
	@Override
	public void rpush() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#rpushx()
	 */
	@Override
	public void rpushx() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#sadd()
	 */
	@Override
	public void sadd() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#scard()
	 */
	@Override
	public void scard() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#sdiff()
	 */
	@Override
	public void sdiff() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#sdiffstore()
	 */
	@Override
	public void sdiffstore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#sinter()
	 */
	@Override
	public void sinter() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#sinterstore()
	 */
	@Override
	public void sinterstore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#sismember()
	 */
	@Override
	public void sismember() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#smembers()
	 */
	@Override
	public void smembers() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#smove()
	 */
	@Override
	public void smove() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#spop()
	 */
	@Override
	public void spop() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#srandmember()
	 */
	@Override
	public void srandmember() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#srem()
	 */
	@Override
	public void srem() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#sunion()
	 */
	@Override
	public void sunion() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#sunionstore()
	 */
	@Override
	public void sunionstore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#sscan()
	 */
	@Override
	public void sscan() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zadd()
	 */
	@Override
	public void zadd() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zcard()
	 */
	@Override
	public void zcard() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zcount()
	 */
	@Override
	public void zcount() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zincrby()
	 */
	@Override
	public void zincrby() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zrange()
	 */
	@Override
	public void zrange() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zrangebyscore()
	 */
	@Override
	public void zrangebyscore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zrank()
	 */
	@Override
	public void zrank() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zrem()
	 */
	@Override
	public void zrem() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zremrangebyrank()
	 */
	@Override
	public void zremrangebyrank() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zremrangebyscore()
	 */
	@Override
	public void zremrangebyscore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zrevrange()
	 */
	@Override
	public void zrevrange() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zrevrangebyscore()
	 */
	@Override
	public void zrevrangebyscore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zrevrank()
	 */
	@Override
	public void zrevrank() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zscore()
	 */
	@Override
	public void zscore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zunionstore()
	 */
	@Override
	public void zunionstore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zinterstore()
	 */
	@Override
	public void zinterstore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zscan()
	 */
	@Override
	public void zscan() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zrangebylex()
	 */
	@Override
	public void zrangebylex() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zlexcount()
	 */
	@Override
	public void zlexcount() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#zremrangebylex()
	 */
	@Override
	public void zremrangebylex() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#pfadd()
	 */
	@Override
	public void pfadd() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#pfcount()
	 */
	@Override
	public void pfcount() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#pfmerge()
	 */
	@Override
	public void pfmerge() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#psubscribe()
	 */
	@Override
	public void psubscribe() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#publish()
	 */
	@Override
	public void publish() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#pubsub()
	 */
	@Override
	public void pubsub() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#punsubscribe()
	 */
	@Override
	public void punsubscribe() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#subscribe()
	 */
	@Override
	public void subscribe() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#unsubscribe()
	 */
	@Override
	public void unsubscribe() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#discard()
	 */
	@Override
	public void discard() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#exec()
	 */
	@Override
	public void exec() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#multi()
	 */
	@Override
	public void multi() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#unwatch()
	 */
	@Override
	public void unwatch() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#watch()
	 */
	@Override
	public void watch() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#eval()
	 */
	@Override
	public void eval() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#evalsha()
	 */
	@Override
	public void evalsha() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#scriptexists()
	 */
	@Override
	public void scriptexists() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#scriptflush()
	 */
	@Override
	public void scriptflush() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#scriptkill()
	 */
	@Override
	public void scriptkill() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#scriptload()
	 */
	@Override
	public void scriptload() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#auth()
	 */
	@Override
	public void auth() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#echo()
	 */
	@Override
	public void echo() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#ping()
	 */
	@Override
	public void ping() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#quit()
	 */
	@Override
	public void quit() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#select(int)
	 */
	@Override
	public boolean select(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#bgrewriteaof()
	 */
	@Override
	public boolean bgrewriteaof() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#bgsave()
	 */
	@Override
	public boolean bgsave() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#clientgetname()
	 */
	@Override
	public void clientgetname() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#clientkill()
	 */
	@Override
	public void clientkill() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#clientlist()
	 */
	@Override
	public void clientlist() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#clientsetname()
	 */
	@Override
	public void clientsetname() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#configget()
	 */
	@Override
	public void configget() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#config()
	 */
	@Override
	public void config() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#resetstat()
	 */
	@Override
	public void resetstat() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#configrewrite()
	 */
	@Override
	public void configrewrite() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#configset()
	 */
	@Override
	public void configset() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#dbsize()
	 */
	@Override
	public int dbsize() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#debugobject()
	 */
	@Override
	public void debugobject() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#debugsegfault()
	 */
	@Override
	public void debugsegfault() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean flushall() {
		return executeCommand(null, BooleanResultCommand.class, Commands.flushall);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#flushdb()
	 */
	@Override
	public boolean flushdb() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#info()
	 */
	@Override
	public void info() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#lastsave()
	 */
	@Override
	public void lastsave() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#monitor()
	 */
	@Override
	public void monitor() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#psync()
	 */
	@Override
	public void psync() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#save()
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#shutdown()
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#slaveof()
	 */
	@Override
	public void slaveof() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#showlog()
	 */
	@Override
	public void showlog() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#sync()
	 */
	@Override
	public void sync() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DeerletRedisClient#time()
	 */
	@Override
	public void time() {
		// TODO Auto-generated method stub

	}

}
