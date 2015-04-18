package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

/**
 * 
 * 结果为boolean类型的命令的实现类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 */
public class BooleanResultCommand extends AbstractCommand<Boolean> {

	public BooleanResultCommand(Connection connection, Commands command) {
		super(connection, command);
	}

	protected Boolean receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLine();
		if (ProtocolUtil.isOk(response)) {
			return true;
		} else {
			throw new RuntimeException(ProtocolUtil.extractResult(response));
		}
	}

}
