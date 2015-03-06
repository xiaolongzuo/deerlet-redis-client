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

	public static String asString(String s) {
		return "'" + s + "'";
	}

	public static String[] asString(String[] ss, int... indexs) {
		if (indexs != null) {
			for (int i = 0; i < indexs.length; i++) {
				ss[indexs[i]] = asString(ss[indexs[i]]);
			}
		}
		return ss;
	}

}
