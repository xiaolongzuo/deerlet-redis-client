/**
 * 
 */
package cn.zxl.deerlet.redis.client.command;

/**
 *
 * 枚举类，代表bit
 *
 * @author zuoxiaolong
 * @since 2015 2015年3月6日 下午11:34:16
 *
 */
public enum Bit {

	zero {
		@Override
		public int value() {
			return 0;
		}
	},one {
		@Override
		public int value() {
			return 1;
		}
	};
	
	public abstract int value();
	
	public String toString(){
		return String.valueOf(value());
	}
	
	public static Bit create(int i) {
		if (i == 0) {
			return Bit.zero;
		} else if (i == 1) {
			return Bit.one;
		} else {
			return null;
		}
	}
	
}
