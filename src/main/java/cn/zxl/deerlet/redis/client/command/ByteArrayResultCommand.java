/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

/**
 * @author zuoxiaolong
 *
 */
public class ByteArrayResultCommand extends AbstractCommand<byte[]>{

	public ByteArrayResultCommand(Connection connection, Commands command) {
		super(connection, command);
	}

	@Override
	protected byte[] receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLine();
		byte[] bytes = null;
		int length = 0;
		if (ProtocolUtil.isStringLengthResultOk(response) && (length = Integer.valueOf(ProtocolUtil.extractResult(response))) > 0) {
			bytes = new byte[length];
			inputStream.read(bytes, 0, length); 
		}
		return bytes;
	}
	
}
