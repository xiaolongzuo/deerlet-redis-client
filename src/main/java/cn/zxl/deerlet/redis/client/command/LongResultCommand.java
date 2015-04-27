/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

/**
 * 结果为long类型的命令的实现类
 * @author zuoxiaolong
 *
 */
public class LongResultCommand extends AbstractCommand<Long> {

	@Override
	protected Long receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLine();
		if (ProtocolUtil.isIntResultOk(response)) {
			return Long.valueOf(ProtocolUtil.extractResult(response));
		} else {
			throw new RuntimeException(ProtocolUtil.extractResult(response));
		}
	}

	@Override
	public Long merge(Long current, Long next) {
		return current + next;
	}

}
