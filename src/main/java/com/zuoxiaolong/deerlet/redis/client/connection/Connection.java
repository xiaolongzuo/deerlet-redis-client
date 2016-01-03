package com.zuoxiaolong.deerlet.redis.client.connection;

import com.zuoxiaolong.deerlet.redis.client.io.MultibulkInputStream;
import com.zuoxiaolong.deerlet.redis.client.io.MultibulkOutputStream;

/**
 * 
 * 该接口的目的是为了封装底层socket通信。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:39:37
 *
 */
public interface Connection {

	public MultibulkOutputStream getOutputStream();

	public MultibulkInputStream getInputStream();

	public boolean isClosed();

	public void close();

}
