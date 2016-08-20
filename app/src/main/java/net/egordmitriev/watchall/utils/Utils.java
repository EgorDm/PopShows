package net.egordmitriev.watchall.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class Utils {
    public static String capitalize(String str) {
        int strLen;
        return str != null && (strLen = str.length()) != 0?(new StringBuffer(strLen)).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString():str;
    }

    public static <T> T coalesce(T a, T b) {
        return a != null ? a : b;
    }

    public static <T> T[] concatArray(T[]... object){
        List<T> list=new ArrayList<>();
        for (T[] i : object) {
            list.addAll(Arrays.asList(i));
        }
        //noinspection unchecked
        return list.toArray((T[]) Array.newInstance(object.getClass().getComponentType(), list.size()));
    }

    public static Date convertUnixToDate(long unix) {
        int numberOfDigits = String.valueOf(unix).length();
        if(numberOfDigits <= 10) {
            return new Date(unix*1000L);
        }
        return new Date(unix);
    }

    public static long convertMillsToUnix(long mills) {
        return mills/1000L;
    }
}
