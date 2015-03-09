package cn.zxl.deerlet.redis.client.util;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * 
 * 类型处理工具类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:45:00
 *
 */
public abstract class TypeUtil {
	
	public static byte[] asBytes(Object o) {
		try {
			return asString(o).getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String asString(Object o) {
		if (o instanceof String) {
			return "'" + o + "'";
		} else if (o.getClass().isArray()) {
			StringWriter s = new StringWriter();
			Object[] oo = (Object[]) o;
			for (int i = 0; i < oo.length; i++) {
				s.append(asString(oo[i]));
				if (i < oo.length - 1) {
					s.append((char)ProtocolUtil.space());
				}
			}
			return s.toString();
		} else {
			return o.toString();
		}
	}

	public static Object[] asString(Object[] oo, int... indexs) {
		if (indexs != null) {
			for (int i = 0; i < indexs.length; i++) {
				oo[indexs[i]] = asString(oo[indexs[i]]);
			}
		}
		return oo;
	}
	
}
