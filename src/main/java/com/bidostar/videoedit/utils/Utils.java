package com.bidostar.videoedit.utils;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by small.cao on 2017/12/19.
 */

public class Utils {

    /**
     * check whether /storage/sdcard1 is mounted.
     */
    public static boolean isStorageMounted(String path) {
        File f = new File(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Environment.getExternalStorageState(f).equals(
                    Environment.MEDIA_MOUNTED);
        }
        return false;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String)(get.invoke(c, key, "unknown" ));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return value;
        }
    }
}
