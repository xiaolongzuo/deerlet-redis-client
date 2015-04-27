/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

/**
 * 结果为int类型的命令的实现类
 * @author zuoxiaolong
 *
 */
public class IntResultCommand extends AbstractCommand<Integer> {

	@Override
	protected Integer receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLine();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("command is " + command + ", response is " + response + ", arguments.length = " + arguments.length);
			if (arguments != null ) {
				for (int i = 0;i < arguments.length; i++) {
					LOGGER.debug("arguments[" + i + "] = " + arguments[i]);
				}
			}
		}
		if (ProtocolUtil.isIntResultOk(response)) {
			return Integer.valueOf(ProtocolUtil.extractResult(response));
		} else {
			throw new RuntimeException(ProtocolUtil.extractResult(response));
		}
	}

	@Override
	public Integer merge(Integer current, Integer next) {
		return current + next;
	}

}
