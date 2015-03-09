package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.DeerletInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

/**
 * 
 * 结果为string类型的实现类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 */
public class StringResultCommand extends AbstractCommand<String> {

	public StringResultCommand(Connection connection, Commands command) {
		super(connection, command);
	}

	@Override
	protected String receive(DeerletInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLineWithoutR();
		String result = null;
		if (ProtocolUtil.isStringLengthResultOk(response) && Integer.valueOf(ProtocolUtil.extractResult(response)) > 0) {
			result = inputStream.readLineWithoutR(); 
		}
		return result;
	}

}
