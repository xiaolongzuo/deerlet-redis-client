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

	  set, get, flushall, dbsize, append, flushdb, bgsave, bgrewriteaof, exists, expire
	, decr, decrby, del, incr, incrby, incrbyfloat
	, lset, lpush, lrange, llen, lpushx, lpop, lrem, lindex, linsert, ltrim
	, 

}
