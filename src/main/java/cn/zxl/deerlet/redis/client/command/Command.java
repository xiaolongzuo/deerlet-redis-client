package cn.zxl.deerlet.redis.client.command;

/**
 * 
 * 命令接口，这是命令模式中的命令接口，实际的命令执行会交给连接处理
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:32:49
 *
 */
public interface Command<T> {

	public T execute(String... arguments);

}
