/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 结果为列表类型的命令的实现类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 */
public class ListResultCommand extends AbstractCommand<List<String>> {

	@Override
	protected List<String> receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLine();
		List<String> list = new ArrayList<String>();
		if (ProtocolUtil.isArrayLengthResultOk(response)) {
			int length = Integer.valueOf(ProtocolUtil.extractResult(response));
			for (int i = 0; i < length; i++) {
				String line = inputStream.readLine();
				if (ProtocolUtil.isStringLengthResultOk(line)) {
					list.add(inputStream.readLine());
				}
			}
		} else {
			throw new RuntimeException(ProtocolUtil.extractResult(response));
		}
		return list;
	}

	@Override
	public List<String> merge(List<String> current, List<String> next) {
		current.addAll(next);
		return current;
	}

}
