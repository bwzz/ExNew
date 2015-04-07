package com.bwzz.common.utils;

import android.util.Log;

/**
 * @author wanghb
 * @date 15/4/7.
 */
public class L {
    public static String getFileName() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[5];
        return element.getFileName().replace("\\.java$", "");
    }

    public static void i(Object... os) {
        log(Log.INFO, os);
    }

    public static void v(Object... os) {
        log(Log.VERBOSE, os);
    }

    public static void d(Object... os) {
        log(Log.DEBUG, os);
    }

    public static void w(Object... os) {
        log(Log.WARN, os);
    }

    public static void e(Object... os) {
        log(Log.ERROR, os);
    }

    private static void log(int level, Object... os) {
        String tag = getFileName();
        String info = join(os);
        switch (level) {
            case Log.ASSERT:
                Log.e(tag, info);
                break;
            case Log.INFO:
                Log.i(tag, info);
                break;
            case Log.DEBUG:
                Log.d(tag, info);
                break;
            case Log.WARN:
                Log.w(tag, info);
                break;
            case Log.ERROR:
                Log.e(tag, info);
                break;
            case Log.VERBOSE:
                Log.v(tag, info);
                break;
        }

    }

    private static String join(Object[] arr) {
        StackTraceElement element = Thread.currentThread().getStackTrace()[5];
        StringBuffer sb = new StringBuffer();
        sb.append(element.getMethodName());
        sb.append(":");
        sb.append(element.getLineNumber());
        sb.append(" : ");
        if (arr == null || arr.length == 0) {
            return sb.toString();
        }
        for (Object o : arr) {
            sb.append(String.valueOf(o));
        }
        return sb.toString();
    }
}