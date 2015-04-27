package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.MultibulkInputStream;
import cn.zxl.deerlet.redis.client.io.MultibulkOutputStream;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;
import cn.zxl.deerlet.redis.client.util.TypeUtil;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 
 * 所有命令实现类的抽象实现，其中封装了命令执行的抽象过程。
 * 通常情况下，子类只需要实现receive()方法。也可以选择性的重写send方法。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:34:16
 *
 */
public abstract class AbstractCommand<T> implements Command<T> {
	
	protected final Logger LOGGER = Logger.getLogger(getClass());

	private static final String COMMAND_SEPARATOR = "_";
	
	private static final String SPACE = " ";
	
	@SuppressWarnings("unchecked")
	public T execute(Connection connection,Commands command, Object... arguments) {
		if (connection == null || connection.isClosed()) {
			throw new IllegalStateException("connection is null or is closed!");
		}
		T result = null;
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("send command : " + command + " outputstream : " + connection.getOutputStream());
			}
			send(connection.getOutputStream(), command, arguments);
			connection.getOutputStream().flush();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("receive data , command : " + command + " inputstream : " + connection.getInputStream());
			}
			result = (T) receive(connection.getInputStream(), command, arguments);
			connection.getInputStream().clear();
		} catch (Exception e) {
			throw new RuntimeException("command execute failed!", e);
		}
		connection.close();
		return result;
	}

	protected void send(MultibulkOutputStream outputStream, Commands command, Object... arguments) throws Exception {
		String commandString = command.name();
		if (command.name().indexOf(COMMAND_SEPARATOR) > 0) {
			commandString = command.name().replace(COMMAND_SEPARATOR, SPACE);
		}
		byte[][] argumentBytes = new byte[arguments.length][];
		for (int i = 0; i < arguments.length; i++) {
			if (arguments[i] instanceof byte[]) {
				argumentBytes[i] = (byte[]) arguments[i];
			} else if (List.class.isAssignableFrom(arguments[i].getClass())) {
				List<?> list = (List<?>) arguments[i];
				byte[][] extendArgumentBytes = new byte[arguments.length + list.size() - 1][];
				System.arraycopy(argumentBytes, 0, extendArgumentBytes, 0, i);
				for (int j = 0; j < list.size(); j++) {
					extendArgumentBytes[i++] = TypeUtil.stringToBytes(list.get(j).toString());
				}
				argumentBytes = extendArgumentBytes;
			} else if (arguments[i].getClass().isArray()) {
				Object[] array = (Object[]) arguments[i];
				byte[][] extendArgumentBytes = new byte[arguments.length + array.length - 1][];
				System.arraycopy(argumentBytes, 0, extendArgumentBytes, 0, i);
				for (int j = 0; j < array.length; j++) {
					extendArgumentBytes[i++] = TypeUtil.stringToBytes(array[j].toString());
				}
				argumentBytes = extendArgumentBytes;
			} else {
				argumentBytes[i] = TypeUtil.stringToBytes(arguments[i].toString());
			}
		}
		ProtocolUtil.sendCommand(outputStream, TypeUtil.stringToBytes(commandString) , argumentBytes);
	}
	
	protected abstract Object receive(MultibulkInputStream inputStream, Commands command, Object... arguments) throws Exception;
	
}
