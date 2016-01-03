/**
 * 
 */
package com.zuoxiaolong.deerlet.redis.client.command;

import com.zuoxiaolong.deerlet.redis.client.io.MultibulkInputStream;
import com.zuoxiaolong.deerlet.redis.client.util.ProtocolUtil;

/**
 *
 * 结果为字节数组类型的命令的实现类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
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
