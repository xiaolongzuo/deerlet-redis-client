/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

import java.util.List;

/**
 * @author zuoxiaolong
 *
 */
public interface Cursor {

	public List<Integer> getCursorList();
	
	public List<String> getResultList();
	
	
}
