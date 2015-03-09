package cn.zxl.deerlet.redis.client.util;

/**
 * 
 * 响应工具类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:44:47
 *
 */
public abstract class ProtocolUtil {
	
	private static final String trueResultPrefix = "+";
	
	private static final String falseResultPrefix = "-";
	
	private static final String intResultPrefix = ":";
	
	private static final String stringLengthResultPrefix = "$";
	
	private static final String arrayLengthResultPrefix = "*";
	
	public static int space() {
		return '\r';
	}
	
	public static int enter() {
		return '\n';
	}

	public static boolean isOk(String response) {
		return response != null && response.startsWith(trueResultPrefix);
	}
	
	public static boolean isError(String response) {
		return response != null && response.startsWith(falseResultPrefix);
	}
	
	public static boolean isStringLengthResultOk(String response) {
		return response != null && response.startsWith(stringLengthResultPrefix);
	}

	public static boolean isIntResultOk(String response) {
		return response != null && response.startsWith(intResultPrefix);
	}
	
	public static boolean isArrayLengthResultOk(String response){
		return response != null && response.startsWith(arrayLengthResultPrefix);
	}
	
	public static boolean intResultToBooleanResult(String response) {
		return response != null && isIntResultOk(response) && Integer.valueOf(extractResult(response)) > 0;
	}
	
	public static boolean intResultToBooleanResult(int intResult) {
		return intResult > 0;
	}
	
	public static String extractResult(String response){
		return (response == null || response.length() == 0) ? null : response.substring(1);
	}

}
