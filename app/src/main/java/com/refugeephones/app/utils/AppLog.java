package com.refugeephones.app.utils;

/**
 * A centralized class to offer logging.
 * Future extensions may support saving certain logs onto SDCard too.
 * @author Waqas
 *
 */
public class AppLog {
	
	private static final String App_Name = "AppLog";
	
	
	/*public static void assertion(String msg){ Insert(android.util.Log.ASSERT, null, msg); }
	public static void assertion(String tag, String msg){ Insert(android.util.Log.ASSERT, tag, msg); }
	public static void assertion(Class<?> c , String msg){ Insert(android.util.Log.ASSERT, c.getName(), msg); }*/
	
	//public static void debug(String msg){ Insert(android.util.Log.DEBUG, null, msg, null); }
	public static void debug(String TAG, String msg){ Insert(android.util.Log.DEBUG, TAG, msg, null); }
	public static void debug(String TAG, String msg, Throwable exception){ Insert(android.util.Log.DEBUG, TAG, msg, exception); }
	//public static void debug(Class<?> c , String msg){ Insert(android.util.Log.DEBUG, c.getName(), msg, null); }
	
	//public static void error(String msg){ Insert(android.util.Log.ERROR, null, msg, null); }
	public static void error(String TAG, String msg){ Insert(android.util.Log.ERROR, TAG, msg, null); }
	public static void error(String TAG, String msg, Throwable exception){ Insert(android.util.Log.ERROR, TAG, msg, exception); }
	//public static void error(Class<?> c , String msg){ Insert(android.util.Log.ERROR, c.getName(), msg, null); }
	
	//public static void verbose(String msg){ Insert(android.util.Log.VERBOSE, null, msg, null); }
	public static void verbose(String TAG, String msg){ Insert(android.util.Log.VERBOSE, TAG, msg, null); }
	public static void verbose(String TAG, String msg, Throwable exception){ Insert(android.util.Log.VERBOSE, TAG, msg, exception); }
	//public static void verbose(Class<?> c , String msg){ Insert(android.util.Log.VERBOSE, c.getName(), msg, null); }
	
	//public static void warn(String msg){ Insert(android.util.Log.WARN, null, msg, null); }
	public static void warn(String TAG, String msg){ Insert(android.util.Log.WARN, TAG, msg, null); }
	public static void warn(String TAG, String msg, Throwable exception){ Insert(android.util.Log.WARN, TAG, msg, exception); }
	//public static void warn(Class<?> c , String msg){ Insert(android.util.Log.WARN, c.getName(), msg, null); }
	
	//public static void info(String msg){ Insert(android.util.Log.INFO, null, msg, null); }
	public static void info(String TAG, String msg){ Insert(android.util.Log.INFO, TAG, msg, null); }
	public static void info(String TAG, String msg, Throwable exception){ Insert(android.util.Log.INFO, TAG, msg, exception); }
	//public static void info(Class<?> c , String msg){ Insert(android.util.Log.INFO, c.getName(), msg, null); }
	
	
	
	/*
	 public static int LOGLEVEL = 1;
public static boolean WARN = LOGLEVEL > 1;
public static boolean DEBUG = LOGLEVEL > 0;
...
    if (DEBUG) Log.v(TAG, "Message here");
    if (WARN) Log.w(TAG, "WARNING HERE");

	}*/
	
	
	
	private static void Insert(int logType, String tag, String msg, Throwable exception) {
		// Log.d(this.getClass().getName(), "Performing Bluetooth Discovery");
		if (tag == null || tag.trim().length() < 1)
			tag = App_Name;

		switch (logType) {
		/*case android.util.Log.ASSERT:
			android.util.Log.wtf(tag, msg);
			break;*/
		case android.util.Log.VERBOSE:
			//if(!PhoniroApplication.getInstance().isReleasedVersion()){
				if(exception==null) android.util.Log.v(tag, msg);
				else android.util.Log.v(tag, msg, exception);
			//}
			break;
		case android.util.Log.DEBUG:
			//if(!PhoniroApplication.getInstance().isReleasedVersion()){
				if(exception==null) android.util.Log.d(tag, msg);
				else  android.util.Log.d(tag, msg, exception);
			//}
			break;
		case android.util.Log.WARN:
			if(exception==null) android.util.Log.w(tag, msg);
			else android.util.Log.w(tag, msg, exception);
			break;
		case android.util.Log.ERROR:
			if(exception==null) android.util.Log.e(tag, msg);
			else android.util.Log.e(tag, msg, exception);
			break;
		default:
			if(exception==null) android.util.Log.i(tag, msg);
			else android.util.Log.i(tag, msg, exception);
		}
	}
	
}
