/**
 * 
 */
package com.zuoxiaolong.deerlet.redis.client.command;

/**
 *
 * 枚举类，代表bitop命令的操作
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:34:16
 *
 */
public enum BitopOperations {

	and {
		@Override
		public int operate(int i, int j) {
			return i & j;
		}
	},or {
		@Override
		public int operate(int i, int j) {
			return i | j;
		}
	},not {
		@Override
		public int operate(int i, int j) {
			return 1 - i;
		}
	},xor {
		@Override
		public int operate(int i, int j) {
			return i ^ j;
		}
	};
	
	public abstract int operate(int i, int j);
}
