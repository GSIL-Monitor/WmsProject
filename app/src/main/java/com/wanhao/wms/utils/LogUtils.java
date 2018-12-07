package com.wanhao.wms.utils;


import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by mrqiu on 2017/10/17.
 */

public class LogUtils {
    static {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private static final String TAG = "qlLog";
    private static boolean open = true;


    public static void i(String log) {
        i(TAG, log);
    }

    public static void i(Class clazz, String log) {
        if (!open) {
            return;
        }
        Logger.i(log, clazz);
    }

    public static void i(String s, String log) {
        if (!open) {
            return;
        }
        Logger.i(log);
    }

    public static void e(String s, String log) {
        if (!open) {
            return;
        }
        Logger.e(log, s);
    }

    public static void iJson(String json){
        if (!open) {
            return;
        }
        Log.i(TAG, json);
        //Logger.json(json);
    }

    public static void e(Class clazz, String log) {
        if (!open) {
            return;
        }
        Logger.e(log, clazz);
    }
}
