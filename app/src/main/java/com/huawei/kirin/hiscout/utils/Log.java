package com.huawei.kirin.hiscout.utils;


/**
 * Created by spock on 16-3-2.
 */
public class Log {
    private static final String TAG = "KirinPMP";
    private static boolean DEBUG = true;

    public static void d(String className, String content) {
        if(DEBUG) {
        	android.util.Log.d(TAG, "[" + className + "]---------" + content);
        }
    }

    public static void e(String className, String content) {
        if(DEBUG) {
        	android.util.Log.e(TAG, "[" + className + "]######" + content + "######");
        }
    }

    public static void e(String className, String content, Throwable exception) {
        if(DEBUG) {
        	android.util.Log.e(TAG, "[" + className + "]---------" + content, exception);
        }
    }

}


