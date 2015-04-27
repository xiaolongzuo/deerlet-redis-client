/**
 * 
 */
package cn.zxl.deerlet.redis.client;

import java.util.List;

import org.apache.log4j.Logger;

import cn.zxl.deerlet.redis.client.command.Bit;
import cn.zxl.deerlet.redis.client.command.BooleanResultCommand;
import cn.zxl.deerlet.redis.client.command.ByteArrayResultCommand;
import cn.zxl.deerlet.redis.client.command.Command;
import cn.zxl.deerlet.redis.client.command.CommandCache;
import cn.zxl.deerlet.redis.client.command.Commands;
import cn.zxl.deerlet.redis.client.command.IntResultCommand;
import cn.zxl.deerlet.redis.client.command.ListResultCommand;
import cn.zxl.deerlet.redis.client.command.LongResultCommand;
import cn.zxl.deerlet.redis.client.command.ObjectSubcommands;
import cn.zxl.deerlet.redis.client.command.StringResultCommand;
import cn.zxl.deerlet.redis.client.command.Types;
import cn.zxl.deerlet.redis.client.command.TypesResultCommand;
import cn.zxl.deerlet.redis.client.connection.Connection;
import cn.zxl.deerlet.redis.client.connection.ConnectionPool;
import cn.zxl.deerlet.redis.client.strategy.LoadBalanceStrategy;
import cn.zxl.deerlet.redis.client.util.ProtocolUtil;

/**
 * @author zuoxiaolong
 *
 */
public abstract class AbstractDeerletRedisClient implements DeerletRedisClient {

	protected final Logger LOGGER = Logger.getLogger(getClass());
	
	protected LoadBalanceStrategy<ConnectionPool> strategy;

	/**
	 * 
	 */
	public AbstractDeerletRedisClient(LoadBalanceStrategy<ConnectionPool> strategy) {
		super();
		this.strategy = strategy;
	}

	protected <T> T executeCommand(Class<? extends Command<T>> commandClass, Commands command, Object... arguments) {
        Command<T> commandInstance = CommandCache.INSTANCE.get(commandClass);
        T result = null;
        List<ConnectionPool> connectionPools = strategy.getAll();
        for (int i = 0; i < connectionPools.size(); i++) {
            if (result == null) {
                result = executeCommand(connectionPools.get(i).obtainConnection(), commandInstance, command, arguments);
            } else {
                result = commandInstance.merge(result, executeCommand(connectionPools.get(i).obtainConnection(), commandInstance, command, arguments));
            }
        }
        return result;
    }

    protected <T> T executeCommand(String key, Class<? extends Command<T>> commandClass, Commands command, Object... arguments) {
        Command<T> commandInstance = CommandCache.INSTANCE.get(commandClass);
        return executeCommand(strategy.select(key).obtainConnection(), commandInstance, command, arguments);
    }

    protected <T> T executeCommand(Connection connection, Command<T> commandInstance, Commands command, Object... arguments) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("execute command[" + command + "],use connection :" + connection);
            }
            return commandInstance.execute(connection, command, arguments);
        } catch (Exception e) {
            throw new RuntimeException("init command class failed!", e);
        }
    }
    
    /****************** key ******************/
    
    @Override
	public byte[] dump(String key) {
	    return executeCommand(key, ByteArrayResultCommand.class, Commands.dump, key);
	}

	@Override
	public boolean exists(String key) {
	    return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.exists, key));
	}
    
    @Override
    public boolean expire(String key, int seconds) {
        return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.expire, key, seconds));
    }

    @Override
    public boolean expireat(String key, int time) {
        return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.expireat, key, time));
    }
    
    @Override
    public boolean migrate(String host, String port, String key, int dbNumber, long timeout) {
        return executeCommand(key, BooleanResultCommand.class, Commands.migrate, host, port, key, dbNumber, timeout);
    }

    @Override
    public boolean move(String key, int dbNumber) {
        return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.move, key, dbNumber));
    }

    @Override
    public Object object(ObjectSubcommands subcommand, String key) {
        if (subcommand == ObjectSubcommands.encoding) {
            return executeCommand(key, StringResultCommand.class, Commands.object, subcommand, key);
        } else {
            return executeCommand(key, IntResultCommand.class, Commands.object, subcommand, key);
        }
    }

    @Override
    public boolean persist(String key) {
        return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.persist, key));
    }

    @Override
    public boolean pexpire(String key, long milliseconds) {
        return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.pexpire, key, milliseconds));
    }

    @Override
    public boolean pexpireat(String key, long milliseconds) {
        return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.pexpireat, key, milliseconds));
    }

    @Override
    public long pttl(String key) {
        return executeCommand(key, LongResultCommand.class, Commands.pttl, key);
    }
    
    @Override
    public String randomkey() {
        return executeCommand(null, StringResultCommand.class, Commands.randomkey);
    }
    
    @Override
    public boolean restore(String key, int ttl, byte[] serializedValue) {
        return executeCommand(key, BooleanResultCommand.class, Commands.restore, new Object[]{key, ttl, serializedValue});
    }

    @Override
    public List<String> sort(String key, Object... arguments) {
        return executeCommand(key, ListResultCommand.class, Commands.sort, key, arguments);
    }

    @Override
    public int ttl(String key) {
        return executeCommand(key, IntResultCommand.class, Commands.ttl, key);
    }

    @Override
    public Types type(String key) {
        return executeCommand(key, TypesResultCommand.class, Commands.type, key);
    }
    
    /***************string**************** */

    @Override
    public int append(String key, String value) {
        return executeCommand(key, IntResultCommand.class, Commands.append, key, value);
    }

    @Override
    public int bitcount(String key, Integer start, Integer stop) {
        if (start == null && stop == null) {
            return executeCommand(key, IntResultCommand.class, Commands.bitcount, key);
        } else if (start != null && stop == null) {
            return executeCommand(key, IntResultCommand.class, Commands.bitcount, key, start);
        } else {
            return executeCommand(key, IntResultCommand.class, Commands.bitcount, key, start, stop);
        }
    }
    
    @Override
    public int decr(String key) {
        return executeCommand(key, IntResultCommand.class, Commands.decr, key);
    }

    @Override
    public int decrby(String key, int decrement) {
        return executeCommand(key, IntResultCommand.class, Commands.decrby, key, decrement);
    }

    @Override
    public String get(String key) {
        return executeCommand(key, StringResultCommand.class, Commands.get, key);
    }

    @Override
    public int getbit(String key, int offset) {
        return executeCommand(key, IntResultCommand.class, Commands.getbit, key, offset);
    }

    @Override
    public String getrange(String key, Integer start, Integer stop) {
        if (start == null && stop == null) {
            return executeCommand(key, StringResultCommand.class, Commands.getrange, key);
        } else if (start != null && stop == null) {
            return executeCommand(key, StringResultCommand.class, Commands.getrange, key, start);
        } else {
            return executeCommand(key, StringResultCommand.class, Commands.getrange, key, start, stop);
        }
    }

    @Override
    public String getset(String key, String value) {
        return executeCommand(key, StringResultCommand.class, Commands.getset, key, value);
    }

    @Override
    public int incr(String key) {
        return executeCommand(key, IntResultCommand.class, Commands.incr, key);
    }

    @Override
    public int incrby(String key, int increment) {
        return executeCommand(key, IntResultCommand.class, Commands.incrby, key, increment);
    }

    @Override
    public float incrbyfloat(String key, float increment) {
        return Float.valueOf(executeCommand(key, StringResultCommand.class, Commands.incrbyfloat, key, increment));
    }
    
    @Override
    public boolean psetex(String key, long milliseconds, Object value) {
        return executeCommand(key, BooleanResultCommand.class, Commands.psetex, key, milliseconds, value);
    }

    @Override
    public boolean set(String key, Object value) {
        return executeCommand(key, BooleanResultCommand.class, Commands.set, key, value);
    }

    @Override
    public Bit setbit(String key, int offset, Bit bit) {
        return Bit.create(executeCommand(key, IntResultCommand.class, Commands.setbit, key, offset, bit));
    }

    @Override
    public boolean setex(String key, int seconds, Object value) {
        return executeCommand(key, BooleanResultCommand.class, Commands.setex, key, seconds, value);
    }

    @Override
    public boolean setnx(String key, Object value) {
        return ProtocolUtil.intResultToBooleanResult(executeCommand(key, IntResultCommand.class, Commands.setnx, key, value));
    }

    @Override
    public int setrange(String key, int offset, Object value) {
        return executeCommand(key, IntResultCommand.class, Commands.setrange, key, offset, value);
    }

    @Override
    public int strlen(String key) {
        return executeCommand(key, IntResultCommand.class, Commands.strlen, key);
    }

}