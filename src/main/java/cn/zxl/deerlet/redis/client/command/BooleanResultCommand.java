package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.DeerletInputStream;
import cn.zxl.deerlet.redis.client.util.IOUtil;
import cn.zxl.deerlet.redis.client.util.ResponseUtil;

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

	protected Boolean receive(DeerletInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = IOUtil.readLineWithoutR(inputStream);
		if (ResponseUtil.isOk(response)) {
			return true;
		} else {
			throw new RuntimeException(ResponseUtil.extractResult(response));
		}
	}

}
