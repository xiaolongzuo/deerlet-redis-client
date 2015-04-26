package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;

/**
 * 
 * 命令接口，这是命令模式中的命令接口，实际的命令执行会交给连接处理
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:32:49
 *
 */
public interface Command<T> {

	public T execute(Connection connection,Commands command, Object... arguments);
	
	public T merge(T current, T next);

}
