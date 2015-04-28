/**
 * 
 */
package cn.zxl.deerlet.redis.client;

import cn.zxl.deerlet.redis.client.command.*;
import cn.zxl.deerlet.redis.client.connection.ConnectionPool;
import cn.zxl.deerlet.redis.client.strategy.LoadBalanceStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public void hget() {
		// TODO Auto-generated method stub

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
	public void hset() {
		// TODO Auto-generated method stub

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

	/**************************** list *************************/

	@Override
	public List<String> blpop(String[] keys, int timeout) {
		return executeCommand(null , ListResultCommand.class, Commands.blpop, keys , timeout);
	}

	@Override
	public List<String> brpop(String[] keys, int timeout) {
		return executeCommand(null , ListResultCommand.class, Commands.brpop, keys , timeout);
	}

	@Override
	public List<String> brpoplpush(String sourceKey, String destKey, int timeout) {
		return executeCommand(null , ListResultCommand.class, Commands.brpoplpush, sourceKey , destKey, timeout);
	}

	@Override
	public String rpoplpush(String sourceKey, String destKey) {
		return executeCommand(null, StringResultCommand.class, Commands.rpoplpush, sourceKey, destKey);
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

	/* ***************** HyperLog commands ************** */

	@Override
	public int pfcount(String... keys) {
		return executeCommand(null, IntResultCommand.class , Commands.pfcount, keys);
	}

	@Override
	public boolean pfmerge(String destkey, String... sourcekeys) {
		return executeCommand(null, BooleanResultCommand.class , Commands.pfmerge, destkey, sourcekeys);
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
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#select(int)
	 */
	@Override
	public boolean select(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#bgrewriteaof()
	 */
	@Override
	public boolean bgrewriteaof() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#bgsave()
	 */
	@Override
	public boolean bgsave() {
		// TODO Auto-generated method stub
		return false;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#dbsize()
	 */
	@Override
	public int dbsize() {
		// TODO Auto-generated method stub
		return 0;
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
		return executeCommand(null, BooleanResultCommand.class, Commands.flushall);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zxl.deerlet.redis.client.DeerletRedisClient#flushdb()
	 */
	@Override
	public boolean flushdb() {
		// TODO Auto-generated method stub
		return false;
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
