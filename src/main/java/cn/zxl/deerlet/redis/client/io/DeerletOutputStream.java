package cn.zxl.deerlet.redis.client.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 
 * 为了方便写入操作的装饰类。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:43:51
 *
 */
public class DeerletOutputStream {

	private OutputStream outputStream;

	public DeerletOutputStream(OutputStream outputStream) {
		if (outputStream == null) {
			throw new NullPointerException();
		}
		this.outputStream = outputStream;
	}

	public void write(int b) throws IOException {
		outputStream.write(b);
	}

	public void write(byte[] b) throws IOException {
		outputStream.write(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		outputStream.write(b, off, len);
	}

	public void flush() throws IOException {
		outputStream.flush();
	}

	public void writeSpace() throws IOException {
		outputStream.write('\r');
	}

	public void writeEnter() throws IOException {
		outputStream.write('\n');
	}

	public void writeString(String s) throws UnsupportedEncodingException, IOException {
		outputStream.write(s.getBytes("utf-8"));
	}

}
