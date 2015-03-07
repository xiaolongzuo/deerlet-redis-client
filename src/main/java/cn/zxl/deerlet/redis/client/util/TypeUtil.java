package cn.zxl.deerlet.redis.client.util;

/**
 * 
 * 类型处理工具类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:45:00
 *
 */
public abstract class TypeUtil {

	public static String asString(Object o) {
		if (o instanceof String) {
			return "'" + o + "'";
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
