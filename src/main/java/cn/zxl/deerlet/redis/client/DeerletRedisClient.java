package cn.zxl.deerlet.redis.client;

import java.util.List;

import cn.zxl.deerlet.redis.client.command.LInsertOptions;

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
	/* del,dump,exists,expire,expireat,keys,migrate,move,object,persist
	,pexpire,pexpireat,pttl,randomkey,rename,renamenx,restore,sort,ttl,type,scan */
	
	public int del(String key);
	
	public byte[] dump(String key);
	
	public boolean exists(String key);
	
	public boolean expire(String key, int seconds);
	
	public void expireat();
	
	public void keys();
	
	public void migrate();
	
	public void move();
	
	public void object();
	
	public void persist();
	
	public void pexpire();
	
	public void pexpireat();
	
	public void pttl();
	
	public void randomkey();
	
	public void rename();
	
	public void renamenx();
	
	public boolean restore(String key, int ttl, byte[] serializedValue);
	
	public void sort();
	
	public void ttl();
	
	public void type();
	
	public void scan();
	

	/** String commands */
	/* append,bitcount,bitop,decr,decrby,get,getbit,getrange,getset,incr,incrby
	,incrbyfloat,mget,mset,msetnx,psetex,set,setbit,setex,setnx,setrange,strlen */
	
	public int append(String key,String value);
	
	public void bitcount();
	
	public void bitop();
	
	public int decr(String key);
	
	public int decrby(String key, int decrement);

	public int incr(String key);
	
	public int incrby(String key, int increment);
	
	public float incrbyfloat(String key, float increment);
	
	public String get(String key);
	
	public void getbit();
	
	public void getrange();
	
	public void getset();
	
	public void mget();
	
	public void mset();
	
	public void msetnx();
	
	public void psetex();
	
	public boolean set(String key, Object value);
	
	public void setbit();
	
	public void setex();
	
	public void setnx();
	
	public void setrange();
	
	public void strlen();
	
	
	/** Hash commands */
	/* hdel,hexists,hget,hgetall,hincrby,hincrbyfloat,hkeys,hlen,hmget,hmset,hset
	,hsetnx,hvals,hscan */
	
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
	/* blpop,brpop,brpoplpush,lindex,linsert,llen,lpop,lpush,lpushx,lrange,lrem,lset
	,ltrim,rpop,rpoplpush,rpush,rpushx */
	
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
	
	public int linsert(String listKey, LInsertOptions option,Object pivot,Object value);
	
	public boolean ltrim(String listKey, int start, int stop);
	
	public void rpop();
	
	public void rpoplpush();
	
	public void rpush();
	
	public void rpushx();
	
	
	/** Set commands */
	/* sadd,scard,sdiff,sdiffstore,sinter,sinterstore,sismember,smembers,smove,spop
	,srandmember,srem,sunion,sunionstore,sscan */
	
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
	/* zadd,zcard,zcount,zincrby,zrange,zrangebyscore,zrank,zrem,zremrangebyrank
	,zremrangebyscore,zrevrange,zrevrangebyscore,zrevrank,zscore,zunionstore,zinterstore
	,zscan,zrangebylex,zlexcount,zremrangebylex */
	
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
	
	public void select();
	
	
	/** Server commands */
	/* bgrewriteaof,bgsave,client_getname,client_kill,client_list,client_setname
	,config_get,config,resetstat,config_rewrite,config_set,dbsize,debug_object
	,debug_segfault,flushall,flushdb,info,lastsave,monitor,psync,save,shutdown
	,slaveof,showlog,sync,time */
	
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
