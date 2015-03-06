package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.DeerletInputStream;
import cn.zxl.deerlet.redis.client.util.IOUtil;
import cn.zxl.deerlet.redis.client.util.ResponseUtil;

/**
 * 
 * dbsize命令的实现类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 */
public class DbSizeCommand extends AbstractCommand<Integer> {

	public DbSizeCommand(Connection connection) {
		super(connection, Commands.dbsize);
	}

	@Override
	protected Integer receive(DeerletInputStream inputStream, Commands command, String... arguments) throws Exception {
		String response = IOUtil.readLineWithoutR(inputStream);
		if (ResponseUtil.isDbSizeOk(response)) {
			return Integer.valueOf(response.split(":")[1]);
		} else {
			throw new RuntimeException("dbsize command exception.");
		}
	}

}
