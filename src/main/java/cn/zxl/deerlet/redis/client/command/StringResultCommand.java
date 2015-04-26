package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;
import cn.zxl.deerlet.redis.client.util.TypeUtil;

/**
 * 
 * 结果为string类型的实现类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 */
public class StringResultCommand extends AbstractCommand<String> {

	@Override
	protected String receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLine();
		String result = null;
		if (ProtocolUtil.isStringLengthResultOk(response)) {
			if (Integer.valueOf(ProtocolUtil.extractResult(response)) > 0) {
				result = TypeUtil.bytesToString(inputStream.readLineBytes());
			}
		} else {
			throw new RuntimeException(ProtocolUtil.extractResult(response));
		}
		return result;
	}

	@Override
	public String merge(String current, String next) {
		throw new UnsupportedOperationException();
	}

}
