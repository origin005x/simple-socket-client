package com.origin.simple_socket.utils;

import android.util.Log;

import com.origin.simple_socket.BuildConfig;

/**
 * 日志打印类，需要打印日志的时候请使用此类来处理，以便在不需要打印日志的时候统一关闭日志打印，或者统一设置日志打印级别
 *
 * @author fantao
 */
public class LogUtil {
    /**
     * 日志开关
     */
    private static boolean logFlagIsOpen = true;
    private static int logLevel = 0;

    private static String format(String args) {
        StackTraceElement tr = new Throwable().getStackTrace()[2];
        return ("[" + tr.getClassName() + "." + tr.getMethodName() + ":"
                + tr.getLineNumber() + "] " + args + " [threadName:" + Thread.currentThread().getName() + "]\n");
    }

    /**
     * 打印log
     *
     * @param tag     标记
     * @param msg     消息
     * @param logType 消息类型："i" , "e", "d", "v", "w"
     */
    public static void log(String tag, String msg, String logType) {
        if (!logFlagIsOpen) {
            return;
        }
        int curLevel = 0;
        if ("v".equals(logType)) {
            curLevel = 0;
        } else if ("d".equals(logType)) {
            curLevel = 1;
        } else if ("i".equals(logType)) {
            curLevel = 2;
        } else if ("w".equals(logType)) {
            curLevel = 3;
        } else if ("e".equals(logType)) {
            curLevel = 4;
        }
        if (curLevel >= logLevel) {
            if ("i".equals(logType)) {
                Log.i(tag == null ? "" : tag, msg == null ? "" : format(msg));
            } else if ("e".equals(logType)) {
                Log.e(tag == null ? "" : tag, msg == null ? "" : format(msg));
            } else if ("d".equals(logType)) {
                Log.d(tag == null ? "" : tag, msg == null ? "" : format(msg));
            } else if ("v".equals(logType)) {
                Log.v(tag == null ? "" : tag, msg == null ? "" : format(msg));
            } else if ("w".equals(logType)) {
                Log.w(tag == null ? "" : tag, msg == null ? "" : format(msg));
            }
        }
    }

    public static void v(String tag, String msg) {
        log(tag, msg, "v");
    }

    public static void d(String tag, String msg) {
        log(tag, msg, "d");
    }

    public static void i(String tag, String msg) {
        log(tag, msg, "i");
    }

    public static void w(String tag, String msg) {
        log(tag, msg, "w");
    }

    public static void e(String tag, String msg) {
        log(tag, msg, "e");
    }

    public static void setLogFlag(boolean isOpen) {
        logFlagIsOpen = isOpen;
    }

    /**
     * 设置日志打印的级别
     *
     * @param level 0:v, 1:d, 2:i, 3:w, 4:e,
     */
    public static void setLogLevel(int level) {
        if (level >= 0 && level <= 4) {
            logLevel = level;
        }
    }
}
