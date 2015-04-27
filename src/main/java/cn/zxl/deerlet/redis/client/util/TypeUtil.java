/**
 * 
 */
package cn.zxl.deerlet.redis.client.util;

import java.io.UnsupportedEncodingException;

/**
 *
 * 工具类，封装了一些类型转换的操作。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:44:47
 *
 */
public abstract class TypeUtil {
	
	private static final String CHARSET = "UTF-8";

	public static byte[] stringToBytes(String s){
		try {
			return s.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String bytesToString(byte[] b){
		try {
			return new String(b,CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
}
