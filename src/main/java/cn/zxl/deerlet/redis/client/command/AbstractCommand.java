package cn.zxl.deerlet.redis.client.command;

import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.io.DeerletInputStream;
import cn.zxl.deerlet.redis.client.io.DeerletOutputStream;

/**
 * 
 * 所有命令实现类的抽象实现，其中封装了命令执行的抽象过程。 通常情况下，子类只需要实现receive()方法。也可以选择性的重写send方法。
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:34:16
 *
 */
public abstract class AbstractCommand<T> implements Command<T> {

	private static final String COMMAND_SEPARATOR = "_";
	
	private Connection connection;

	private Commands command;

	public AbstractCommand(Connection connection) {
		if (connection == null || connection.isClosed()) {
			throw new IllegalStateException("connection is null or is closed!");
		}
		this.connection = connection;
	}

	public AbstractCommand(Connection connection, Commands command) {
		if (connection == null || connection.isClosed()) {
			throw new IllegalStateException("connection is null or is closed!");
		}
		if (command == null) {
			throw new IllegalArgumentException("command can't be null!");
		}
		this.connection = connection;
		this.command = command;
	}

	@SuppressWarnings("unchecked")
	public T execute(Object... arguments) {
		T result = null;
		try {
			send(connection.getOutputStream(), command, arguments);
			result = (T) receive(connection.getInputStream(), command, arguments);
		} catch (Exception e) {
			throw new RuntimeException("command execute failed!", e);
		}
		connection.close();
		return result;
	}

	protected Connection getConnection() {
		return connection;
	}

	protected void send(DeerletOutputStream outputStream, Commands command, Object... arguments) throws Exception {
		if (command.name().indexOf(COMMAND_SEPARATOR) > 0) {
			String[] commands = command.name().split(COMMAND_SEPARATOR);
			outputStream.writeObject(commands[0]);
			outputStream.writeSpace();
			outputStream.writeObject(commands[1]);
		} else {
			outputStream.writeObject(command.name());
		}
		if (arguments != null) {
			for (int i = 0; i < arguments.length; i++) {
				outputStream.writeSpace();
				outputStream.writeObject(arguments[i]);
			}
		}
		outputStream.writeEnter();
		outputStream.flush();
	}

	protected abstract Object receive(DeerletInputStream inputStream, Commands command, Object... arguments) throws Exception;

}
