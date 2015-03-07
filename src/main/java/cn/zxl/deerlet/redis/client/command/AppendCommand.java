/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.DeerletInputStream;
import cn.zxl.deerlet.redis.client.util.IOUtil;
import cn.zxl.deerlet.redis.client.util.ResponseUtil;

/**
 * @author zuoxiaolong
 *
 */
public class AppendCommand extends AbstractCommand<Integer> {

	/**
	 * @param connection
	 */
	public AppendCommand(Connection connection) {
		super(connection,Commands.append);
	}

	@Override
	protected Integer receive(DeerletInputStream inputStream, Commands command, String... arguments) throws Exception {
		String response = IOUtil.readLineWithoutR(inputStream);
		if (ResponseUtil.isIntResultOk(response)) {
			return Integer.valueOf(ResponseUtil.extractResult(response));
		} else {
			return 0;
		}
	}

}
