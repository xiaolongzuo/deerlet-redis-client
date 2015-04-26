/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import java.util.ArrayList;
import java.util.List;

import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

/**
 * @author zuoxiaolong
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
