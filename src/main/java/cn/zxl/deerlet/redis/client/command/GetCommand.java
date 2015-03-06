package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.DeerletInputStream;
import cn.zxl.deerlet.redis.client.util.IOUtil;
import cn.zxl.deerlet.redis.client.util.ResponseUtil;

/**
 * 
 * get命令的实现类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 */
public class GetCommand extends AbstractCommand<String> {

	public GetCommand(Connection connection) {
		super(connection, Commands.get);
	}

	@Override
	protected String receive(DeerletInputStream inputStream, Commands command, String... arguments) throws Exception {
		String response = IOUtil.readLineWithoutR(inputStream);
		if (ResponseUtil.isGetOk(response)) {
			return IOUtil.readLineWithoutR(inputStream);
		} else {
			return null;
		}
	}

}
