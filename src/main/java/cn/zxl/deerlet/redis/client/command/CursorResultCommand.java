/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

/**
 * @author zuoxiaolong
 *
 */
public class CursorResultCommand extends AbstractCommand<Cursor> {

	@Override
	protected Cursor receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception {
		String response = inputStream.readLine();
		List<String> list = new ArrayList<String>();
		Integer cursor = null;
		if (ProtocolUtil.isArrayLengthResultOk(response)) {
			response = inputStream.readLine();
			if (ProtocolUtil.isStringLengthResultOk(response)) {
				cursor = Integer.valueOf(inputStream.readLine());
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("scan command ,cursor: " + cursor);
			}
			response = inputStream.readLine();
			if (ProtocolUtil.isArrayLengthResultOk(response)) {
				int length = Integer.valueOf(ProtocolUtil.extractResult(response));
				for (int i = 0; i < length; i++) {
					String line = inputStream.readLine();
					if (ProtocolUtil.isStringLengthResultOk(line)) {
						list.add(inputStream.readLine());
					}
				}
			}
		} else {
			throw new RuntimeException(ProtocolUtil.extractResult(response));
		}
		return new DefaultCursor(Arrays.asList(cursor == 0 ? -1 : cursor), list);
	}

	@Override
	public Cursor merge(Cursor current, Cursor next) {
		List<Integer> cursorList = new ArrayList<Integer>();
		List<String> resultList = new ArrayList<String>();
		cursorList.addAll(current.getCursorList());
		resultList.addAll(current.getResultList());
		cursorList.addAll(next.getCursorList());
		resultList.addAll(next.getResultList());
		return new DefaultCursor(cursorList, resultList);
	}

}
