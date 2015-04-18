/**
 * 
 */
package cn.zxl.deerlet.redis.client.util;

import java.io.UnsupportedEncodingException;

/**
 * @author zuoxiaolong
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
