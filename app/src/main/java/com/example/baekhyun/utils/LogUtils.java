package com.example.baekhyun.utils;

import android.util.Log;

public class LogUtils {
    private static int current = 4;
    private static int dd = 4;
    private static int ii = 3;
    private static int ee = 2;
    private static int ww = 1;

    public static void d(Object object, String log) {
        if (current >= dd)
            Log.d(object.getClass().getSimpleName(), log);
    }

    public static void i(Object object, String log) {
        if (current >= ii)
            Log.i(object.getClass().getSimpleName(), log);
    }

    public static void w(Object object, String log) {
        if (current >= ww)
            Log.w(object.getClass().getSimpleName(), log);
    }

    public static void e(Object object, String log) {
        if (current >= ee)
            Log.e(object.getClass().getSimpleName(), log);
    }
}
