package cn.zxl.deerlet.redis.client.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cn.zxl.deerlet.redis.client.command.Commands;
import cn.zxl.deerlet.redis.client.io.DeerletInputStream;
import cn.zxl.deerlet.redis.client.io.DeerletOutputStream;

/**
 * 
 * IO工具类
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:44:29
 *
 */
public abstract class IOUtil {

	public static void write(DeerletOutputStream outputStream, Commands command, String[] arguments) throws UnsupportedEncodingException, IOException {
		outputStream.writeString(command.name());
		if (arguments != null) {
			for (int i = 0; i < arguments.length; i++) {
				outputStream.writeSpace();
				outputStream.writeString(arguments[i]);
			}
		}
		outputStream.writeEnter();
		outputStream.flush();
	}

	public static String readLine(DeerletInputStream inputStream) throws IOException {
		return inputStream.readLine();
	}

	public static String readLineWithoutR(DeerletInputStream inputStream) throws IOException {
		return inputStream.readLineWithoutR();
	}

}
