package org.hsweb.commons;


import java.util.List;

public class ListUtils {

    public static String toString(Object... objs) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < objs.length; i++) {
            if (i != 0)
                buffer.append(",");
            buffer.append(objs[i]);
        }
        return buffer.toString();
    }

    public static Integer[] stringArr2intArr(String[] arr) {
        Integer[] i = new Integer[arr.length];
        int index = 0;
        for (String str : arr) {
            if (StringUtils.isInt(str)) {
                i[index++] = Integer.parseInt(str);
            }
        }
        return i;
    }
}
