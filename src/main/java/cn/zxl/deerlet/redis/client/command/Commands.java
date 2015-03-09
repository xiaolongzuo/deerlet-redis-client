package cn.zxl.deerlet.redis.client.command;

/**
 * 
 * 命令集合类。该类的设计目的是为了规范命令类型。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:07
 *
 */
public enum Commands {
	
	/** Key */
	del,dump,exists,expire,expireat,keys,migrate,move,object,persist
	,pexpire,pexpireat,pttl,randomkey,rename,renamenx,restore,sort,ttl,type,scan,
	
	/** String */
	append,bitcount,bitop,decr,decrby,get,getbit,getrange,getset,incr,incrby
	,incrbyfloat,mget,mset,msetnx,psetex,set,setbit,setex,setnx,setrange,strlen,
	
	/** Hash */
	hdel,hexists,hget,hgetall,hincrby,hincrbyfloat,hkeys,hlen,hmget,hmset,hset
	,hsetnx,hvals,hscan,
	
	/** List */
	blpop,brpop,brpoplpush,lindex,linsert,llen,lpop,lpush,lpushx,lrange,lrem,lset
	,ltrim,rpop,rpoplpush,rpush,rpushx,
	
	/** Set */
	sadd,scard,sdiff,sdiffstore,sinter,sinterstore,sismember,smembers,smove,spop
	,srandmember,srem,sunion,sunionstore,sscan,
	
	/** SortedSet */
	zadd,zcard,zcount,zincrby,zrange,zrangebyscore,zrank,zrem,zremrangebyrank
	,zremrangebyscore,zrevrange,zrevrangebyscore,zrevrank,zscore,zunionstore,zinterstore
	,zscan,zrangebylex,zlexcount,zremrangebylex,
	
	/** HyperLog */
	pfadd,pfcount,pfmerge,
	
	/** Pub/Sub */
	psubscribe,publish,pubsub,punsubscribe,subscribe,unsubscribe,
	
	/** Transaction */
	discard,exec,multi,unwatch,watch,
	
	/** Script */
	eval,evalsha,script_exists,script_flush,script_kill,script_load,
	
	/** Connection */
	auth,echo,ping,quit,select,
	
	/** Server */
	bgrewriteaof,bgsave,client_getname,client_kill,client_list,client_setname
	,config_get,config,resetstat,config_rewrite,config_set,dbsize,debug_object
	,debug_segfault,flushall,flushdb,info,lastsave,monitor,psync,save,shutdown
	,slaveof,showlog,sync,time
	

}
