/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

/**
 * @author zuoxiaolong
 *
 */
public class ByteArrayResultCommand extends AbstractCommand<byte[]>{

	@Override
	protected byte[] receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLine();
		byte[] bytes = null;
		int length = 0;
		if (ProtocolUtil.isStringLengthResultOk(response) && (length = Integer.valueOf(ProtocolUtil.extractResult(response))) > 0) {
			bytes = new byte[length];
			inputStream.read(bytes, 0, length); 
		}  else {
			throw new RuntimeException(ProtocolUtil.extractResult(response));
		}
		return bytes;
	}

	@Override
	public byte[] merge(byte[] current, byte[] next) {
		byte[] merged = new byte[current.length + next.length];
		System.arraycopy(current, 0, merged, 0, current.length);
		System.arraycopy(next, 0, merged, current.length, next.length);
		return merged;
	}
	
}
