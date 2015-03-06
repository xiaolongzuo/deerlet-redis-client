package cn.zxl.deerlet.redis.client.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * 为了方便读取操作的装饰类。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:43:51
 *
 */
public class DeerletInputStream {

	private InputStream inputStream;

	public DeerletInputStream(InputStream inputStream) {
		if (inputStream == null) {
			throw new NullPointerException();
		}
		this.inputStream = inputStream;
	}

	public int read() throws IOException {
		return inputStream.read();
	}

	public int read(byte[] b) throws IOException {
		return inputStream.read(b);
	}

	public int read(byte[] b, int off, int len) throws IOException {
		return inputStream.read(b, off, len);
	}

	public String readLine() throws IOException {
		int b = 0;
		byte[] bytes = new byte[0];
		while ((b = inputStream.read()) > 0) {
			if (b == '\n') {
				break;
			} else {
				bytes = putOneByte(bytes, b);
			}
		}
		return new String(bytes, "utf-8");
	}

	public String readLineWithoutR() throws IOException {
		int b = 0;
		byte[] bytes = new byte[0];
		while ((b = inputStream.read()) > 0) {
			if (b == '\n') {
				break;
			} else if (b == '\r') {
				continue;
			} else {
				bytes = putOneByte(bytes, b);
			}
		}
		return new String(bytes, "utf-8");
	}

	private byte[] putOneByte(byte[] bytes, int b) {
		byte[] temp = new byte[bytes.length + 1];
		System.arraycopy(bytes, 0, temp, 0, bytes.length);
		temp[bytes.length] = (byte) b;
		return temp;
	}

}
