package com.nv.test.util;

public class SingleTestUtils {

	private final static SingleTestUtil util = new SingleTestUtil();

	public static SingleTestUtil getUtil() {
		return util;
	}

	public static class SingleTestUtil {

		private Object in;
		private Object out;

		public SingleTestUtil() {
		}

		public Object getIn() {
			return in;
		}

		public void setIn(Object in) {
			this.in = in;
		}

		public Object getOut() {
			return out;
		}

		public void setOut(Object out) {
			this.out = out;
		}
		
		public void clear() {
			this.in = null;
			this.out = null;
		}
	}
}
