package cn.zxl.deerlet.redis.client.connection;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.io.MultibulkOutputStream;

/**
 * 
 * 连接的实现类。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:40:24
 *
 */
public class ConnectionImpl implements Connection {

	private Socket socket = new Socket();

	private MultibulkOutputStream outputStream;

	private MultibulkInputStream inputStream;

	public ConnectionImpl(int port) throws IOException {
		this(Inet4Address.getLocalHost(), port);
	}

	public ConnectionImpl(InetAddress address, int port) throws IOException {
		this(address.getHostAddress(), port);
	}

	public ConnectionImpl(String address, int port) throws IOException {
		socket.setKeepAlive(true);
		socket.connect(new InetSocketAddress(address, port));
		outputStream = new MultibulkOutputStream(socket.getOutputStream());
		inputStream = new MultibulkInputStream(socket.getInputStream());
	}

	@Override
	public boolean isClosed() {
		return socket.isClosed();
	}

	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public MultibulkOutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public MultibulkInputStream getInputStream() {
		return inputStream;
	}

}
