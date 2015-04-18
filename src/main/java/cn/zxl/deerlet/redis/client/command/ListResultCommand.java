/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import java.util.ArrayList;
import java.util.List;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

/**
 * @author zuoxiaolong
 *
 */
public class ListResultCommand extends AbstractCommand<List<String>> {

	/**
	 * @param connection
	 * @param command
	 */
	public ListResultCommand(Connection connection, Commands command) {
		super(connection, command);
	}

	@Override
	protected List<String> receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLine();
		List<String> list = new ArrayList<String>();
		if (ProtocolUtil.isArrayLengthResultOk(response)) {
			int length = Integer.valueOf(ProtocolUtil.extractResult(response));
			for (int i = 0; i < length; i++) {
				String line = inputStream.readLine();
				if (ProtocolUtil.isStringLengthResultOk(line)) {
					list.add(inputStream.readLine());
				}
			}
		}
		return list;
	}

}
