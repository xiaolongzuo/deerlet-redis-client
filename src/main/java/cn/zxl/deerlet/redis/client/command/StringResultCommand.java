package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.DeerletInputStream;
import cn.zxl.deerlet.redis.client.util.IOUtil;
import cn.zxl.deerlet.redis.client.util.ResponseUtil;

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
		String response = IOUtil.readLineWithoutR(inputStream);
		String result = null;
		if (ResponseUtil.isStringLengthResultOk(response) && Integer.valueOf(ResponseUtil.extractResult(response)) > 0) {
			result = IOUtil.readLineWithoutR(inputStream); 
		}
		return result;
	}

}
