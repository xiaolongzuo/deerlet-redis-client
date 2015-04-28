package cn.zxl.deerlet.redis.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.zxl.deerlet.redis.client.command.Bit;
import cn.zxl.deerlet.redis.client.command.BitopOperations;
import cn.zxl.deerlet.redis.client.command.BooleanResultCommand;
import cn.zxl.deerlet.redis.client.command.Command;
import cn.zxl.deerlet.redis.client.command.CommandCache;
import cn.zxl.deerlet.redis.client.command.Commands;
import cn.zxl.deerlet.redis.client.command.Cursor;
import cn.zxl.deerlet.redis.client.command.CursorResultCommand;
import cn.zxl.deerlet.redis.client.command.DefaultCursor;
import cn.zxl.deerlet.redis.client.command.IntResultCommand;
import cn.zxl.deerlet.redis.client.command.LInsertOptions;
import cn.zxl.deerlet.redis.client.command.ListResultCommand;
import cn.zxl.deerlet.redis.client.command.StringResultCommand;
import cn.zxl.deerlet.redis.client.connection.ConnectionPool;
import cn.zxl.deerlet.redis.client.strategy.LoadBalanceStrategy;
import cn.zxl.deerlet.redis.client.strategy.SimpleNodeStrategy;

/**
 * 客户端的默认实现，采用连接池管理连接。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:38:36
 */
public class ClusterDeerletRedisClient extends AbstractDeerletRedisClient implements DeerletRedisClient {

    public ClusterDeerletRedisClient(LoadBalanceStrategy<ConnectionPool> strategy) {
    	super(strategy);
    }

    protected int getServerSize() {
        return strategy.getAll().size();
    }

	/* **************key************* */
    
    @Override
    public int del(String... keys) {
        int result = 0;
        for (int i = 0; i < keys.length; i++) {
            result += executeCommand(keys[i], IntResultCommand.class, Commands.del, keys[i]);
        }
        return result;
    }

    @Override
    public List<String> keys(String pattern) {
        return executeCommand(ListResultCommand.class, Commands.keys, pattern);
    }

    @Override
    public boolean rename(String key, String newKey) {
        return set(newKey, get(key)) && (del(key) == 1);
    }

    @Override
    public boolean renamenx(String key, String newKey) {
        if (exists(newKey)) {
            return false;
        }
        return rename(key, newKey);
    }

    @Override
    public Cursor scan(Cursor cursor, String pattern, Integer count) {
        if (cursor == null) {
            cursor = DefaultCursor.EMPTY_CURSOR;
        }
        List<Integer> cursorList = cursor.getCursorList();
        List<ConnectionPool> connectionPools = strategy.getAll();
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
    public int bitop(BitopOperations operation, String destKey, String... keys) {
    	int result = 0;
    	if (operation != BitopOperations.not) {
    		for (int i = 0; i < keys.length; i++) {
        		int count = strlen(keys[i]) * 8;
    			if (count > result) {
    				result = count;
    			}
    		}
		} else {
			result = strlen(keys[0]) * 8;
		}
    	StringBuffer stringBuffer = new StringBuffer();
    	for (int i = 0; i < result; i++) {
    		int current = getbit(keys[0], i);
    		if (operation != BitopOperations.not) {
    			for (int j = 1; j < keys.length; j++) {
        			current = operation.operate(current, getbit(keys[j], i));
    			}
			} else {
				current = operation.operate(current, -1);
			}
    		stringBuffer.append(current);
		}
    	String bitString = stringBuffer.toString();
    	for (int i = 0; i < bitString.length(); i++) {
			setbit(destKey, i, Bit.create(Integer.valueOf(String.valueOf(bitString.charAt(i)))));
		}
    	return result / 8;
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
        if (strategy instanceof SimpleNodeStrategy<?>) {
            Object[] arguments = new Object[keys.length + values.length];
            for (int i = 0, index = 0; i < values.length; index++) {
                arguments[i++] = keys[index];
                arguments[i++] = values[index];
            }
            return executeCommand(BooleanResultCommand.class, Commands.mset, arguments);
        }
        boolean result = true;
        for (int i = 0; i < keys.length; i++) {
            result &= executeCommand(keys[i], BooleanResultCommand.class, Commands.set, keys[i], values[i]);
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

    /* ************* Hash commands *********************** */
    
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
    
    /* **************** List commands **************** */

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

    /* ************** Set commands ******************** */
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

    /* *************** SortedSet commands *************** */
    
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

    /* ***************** Pub/Sub commands **************** */
    
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

    /* *************** Transaction commands **************** */
    
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

    /*  ****************** Script commands ****************** */
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

    /* **************Connection commands **************** */
    
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
    
    /* ******************** Server commands ******************* */

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
