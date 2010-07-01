package singletons;


public class Debug {
	public static boolean DEBUG = true;
	public static final boolean TRACE = true;

	public static void error(String function, String errorMsg) {
		if (Debug.DEBUG) {
			System.err.println(function + ": " + errorMsg);
			if (Debug.TRACE) {
				Thread.dumpStack();
				System.exit(0);
			}
		}
	}
}
