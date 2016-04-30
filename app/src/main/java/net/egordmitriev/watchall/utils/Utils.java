package net.egordmitriev.watchall.utils;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class Utils {
    public static String capitalize(String str) {
        int strLen;
        return str != null && (strLen = str.length()) != 0?(new StringBuffer(strLen)).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString():str;
    }
}
