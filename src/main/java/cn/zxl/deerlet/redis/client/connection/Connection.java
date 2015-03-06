package cn.zxl.deerlet.redis.client.connection;

import cn.zxl.deerlet.redis.client.io.DeerletInputStream;
import cn.zxl.deerlet.redis.client.io.DeerletOutputStream;

/**
 * 
 * 该接口的目的是为了封装底层socket通信。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:39:37
 *
 */
public interface Connection {

	public DeerletOutputStream getOutputStream();

	public DeerletInputStream getInputStream();

	public boolean isClosed();

	public void close();

}
