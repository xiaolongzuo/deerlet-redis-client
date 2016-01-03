package com.zuoxiaolong.deerlet.redis.client.command;

import com.zuoxiaolong.deerlet.redis.client.io.MultibulkInputStream;
import com.zuoxiaolong.deerlet.redis.client.util.ProtocolUtil;

/**
 * 
 * 结果为boolean类型的命令的实现类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 */
public class BooleanResultCommand extends AbstractCommand<Boolean> {

	protected Boolean receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLine();
		if (ProtocolUtil.isOk(response)) {
			return true;
		} else {
			throw new RuntimeException(ProtocolUtil.extractResult(response));
		}
	}

	@Override
	public Boolean merge(Boolean current, Boolean next) {
		return current && next;
	}

}
