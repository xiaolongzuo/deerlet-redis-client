/**
 * 
 */
package com.zuoxiaolong.deerlet.redis.client.command;

import java.util.List;

/**
 *
 * 游标接口，一个游标代表了一组查询结果和接下来需要扫描的游标
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:36:42
 *
 */
public interface Cursor {

	List<Integer> getCursorList();
	
	List<String> getResultList();
	
	
}
