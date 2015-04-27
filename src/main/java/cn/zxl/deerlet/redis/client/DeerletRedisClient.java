package cn.zxl.deerlet.redis.client;

import cn.zxl.deerlet.redis.client.command.*;

import java.util.List;

/**
 * 
 * deerlet客户端的客户端操作接口，其中定义了本组件支持的redis操作。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:37:35
 *
 */
public interface DeerletRedisClient {
	
	/** Key commands */
	/*
	 * del,dump,exists,expire,expireat,keys,migrate,move,object,persist
	 * ,pexpire,
	 * pexpireat,pttl,randomkey,rename,renamenx,restore,sort,ttl,type,scan
	 */

	public int del(String... key);

	public byte[] dump(String key);

	public boolean exists(String key);

	public boolean expire(String key, int seconds);

	public boolean expireat(String key, int time);

	public List<String> keys(String pattern);

	public boolean migrate(String host, String port, String key, int dbNumber, long timeout);

	public boolean move(String key, int dbNumber);

	public Object object(ObjectSubcommands subcommand, String key);

	public boolean persist(String key);

	public boolean pexpire(String key, long milliseconds);

	public boolean pexpireat(String key, long milliseconds);

	public long pttl(String key);

	public String randomkey();

	public boolean rename(String key, String newKey);

	public boolean renamenx(String key, String newKey);

	public boolean restore(String key, int ttl, byte[] serializedValue);

	public List<String> sort(String key, Object... arguments);

	public int ttl(String key);

	public Types type(String key);

	public Cursor scan(Cursor cursor, String pattern, Integer count);

	/** String commands */
	/*
	 * append,bitcount,bitop,decr,decrby,get,getbit,getrange,getset,incr,incrby
	 * ,
	 * incrbyfloat,mget,mset,msetnx,psetex,set,setbit,setex,setnx,setrange,strlen
	 */

	public int append(String key, String value);

	public int bitcount(String key, Integer start, Integer stop);

	public int bitop(BitopOperations operation, String destKey, String... keys);

	public int decr(String key);

	public int decrby(String key, int decrement);
	
	public String get(String key);
	
	public int getbit(String key, int offset);
	
	public String getrange(String key, Integer start, Integer stop);

	public String getset(String key, String value);

	public int incr(String key);

	public int incrby(String key, int increment);

	public float incrbyfloat(String key, float increment);

	public List<String> mget(String... keys);

	public boolean mset(String[] keys,Object... values);

	public boolean msetnx(String[] keys,Object... values);

	public boolean psetex(String key,long milliseconds,Object value);

	public boolean set(String key, Object value);

	public Bit setbit(String key,int offset,Bit bit);

	public boolean setex(String key ,int seconds,Object value);

	public boolean setnx(String key,Object value);

	public int setrange(String key,int offset ,Object value);

	public int strlen(String key);

	/** Hash commands */
	/*
	 * hdel,hexists,hget,hgetall,hincrby,hincrbyfloat,hkeys,hlen,hmget,hmset,hset
	 * ,hsetnx,hvals,hscan
	 */

	public void hdel();

	public void hexists();

	public void hget();

	public void hgetall();

	public void hincrby();

	public void hincrbyfloat();

	public void hkeys();

	public void hlen();

	public void hmget();

	public void hmset();

	public void hset();

	public void hsetnx();

	public void hvals();

	public void hscan();

	/** List commands */
	/*
	 * blpop,brpop,brpoplpush,lindex,linsert,llen,lpop,lpush,lpushx,lrange,lrem,lset
	 * ,ltrim,rpop,rpoplpush,rpush,rpushx
	 */

	public void blpop();

	public void brpop();

	public void brpoplpush();

	public boolean lset(String listKey, int index, Object value);

	public int lpush(String listKey, Object... values);

	public List<String> lrange(String listKey, int start, int stop);

	public int llen(String listKey);

	public int lpushx(String listKey, Object value);

	public String lpop(String listKey);

	public int lrem(String listKey, int count, Object value);

	public String lindex(String listKey, int index);

	public int linsert(String listKey, LInsertOptions option, Object pivot, Object value);

	public boolean ltrim(String listKey, int start, int stop);

	public void rpop();

	public void rpoplpush();

	public void rpush();

	public void rpushx();

	/** Set commands */
	/*
	 * sadd,scard,sdiff,sdiffstore,sinter,sinterstore,sismember,smembers,smove,spop
	 * ,srandmember,srem,sunion,sunionstore,sscan
	 */

	public void sadd();

	public void scard();

	public void sdiff();

	public void sdiffstore();

	public void sinter();

	public void sinterstore();

	public void sismember();

	public void smembers();

	public void smove();

	public void spop();

	public void srandmember();

	public void srem();

	public void sunion();

	public void sunionstore();

	public void sscan();

	/** SortedSet commands */
	/*
	 * zadd,zcard,zcount,zincrby,zrange,zrangebyscore,zrank,zrem,zremrangebyrank
	 * ,zremrangebyscore,zrevrange,zrevrangebyscore,zrevrank,zscore,zunionstore,
	 * zinterstore ,zscan,zrangebylex,zlexcount,zremrangebylex
	 */

	public void zadd();

	public void zcard();

	public void zcount();

	public void zincrby();

	public void zrange();

	public void zrangebyscore();

	public void zrank();

	public void zrem();

	public void zremrangebyrank();

	public void zremrangebyscore();

	public void zrevrange();

	public void zrevrangebyscore();

	public void zrevrank();

	public void zscore();

	public void zunionstore();

	public void zinterstore();

	public void zscan();

	public void zrangebylex();

	public void zlexcount();

	public void zremrangebylex();

	/** HyperLog commands */
	/* pfadd,pfcount,pfmerge */

	public void pfadd();

	public void pfcount();

	public void pfmerge();

	/** Pub/Sub commands */
	/* psubscribe,publish,pubsub,punsubscribe,subscribe,unsubscribe */

	public void psubscribe();

	public void publish();

	public void pubsub();

	public void punsubscribe();

	public void subscribe();

	public void unsubscribe();

	/** Transaction commands */
	/* discard,exec,multi,unwatch,watch */

	public void discard();

	public void exec();

	public void multi();

	public void unwatch();

	public void watch();

	/** Script commands */
	/* eval,evalsha,script_exists,script_flush,script_kill,script_load */

	public void eval();

	public void evalsha();

	public void scriptexists();

	public void scriptflush();

	public void scriptkill();

	public void scriptload();

	/** Connection commands */
	/* auth,echo,ping,quit,select */

	public void auth();

	public void echo();

	public void ping();

	public void quit();

	public boolean select(int index);

	/** Server commands */
	/*
	 * bgrewriteaof,bgsave,client_getname,client_kill,client_list,client_setname
	 * ,
	 * config_get,config,resetstat,config_rewrite,config_set,dbsize,debug_object
	 * ,
	 * debug_segfault,flushall,flushdb,info,lastsave,monitor,psync,save,shutdown
	 * ,slaveof,showlog,sync,time
	 */

	public boolean bgrewriteaof();

	public boolean bgsave();

	public void clientgetname();

	public void clientkill();

	public void clientlist();

	public void clientsetname();

	public void configget();

	public void config();

	public void resetstat();

	public void configrewrite();

	public void configset();

	public int dbsize();

	public void debugobject();

	public void debugsegfault();

	public boolean flushall();

	public boolean flushdb();

	public void info();

	public void lastsave();

	public void monitor();

	public void psync();

	public void save();

	public void shutdown();

	public void slaveof();

	public void showlog();

	public void sync();

	public void time();

}
