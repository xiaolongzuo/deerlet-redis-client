package cn.zxl.deerlet.redis.client.util;

/**
 * 
 * 响应工具类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:44:47
 *
 */
public abstract class ResponseUtil {

	public static boolean isOk(String response) {
		return "+OK".equals(response);
	}

	public static boolean isGetOk(String response) {
		return response != null && response.startsWith("$");
	}

	public static boolean isDbSizeOk(String response) {
		return response != null && response.startsWith(":");
	}

}
