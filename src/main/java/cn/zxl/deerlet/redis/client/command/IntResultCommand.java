/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

/**
 * 结果为int类型的命令的实现类
 * @author zuoxiaolong
 *
 */
public class IntResultCommand extends AbstractCommand<Integer> {

	/**
	 * @param connection
	 */
	public IntResultCommand(Connection connection, Commands command) {
		super(connection, command);
	}

	@Override
	protected Integer receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLine();
		if (ProtocolUtil.isIntResultOk(response)) {
			return Integer.valueOf(ProtocolUtil.extractResult(response));
		} else {
			return 0;
		}
	}

}
