package org.hswebframework.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ListUtils {
    public static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

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

    public static <T> List<T> merge(List<T> list, List<T>... lists) {
        return merge(new ArrayList<>(), list, lists);
    }

    public static <T> List<T> merge(List<T> target, List<T> list, List<T>... lists) {
        target.addAll(list);
        for (int i = 0; i < lists.length; i++) {
            target.addAll(lists[i]);
        }
        return target;
    }

    public static <T> List<T> merge(Supplier<List<T>> supplier, List<T> list, List<T>... lists) {
        return merge(supplier.get(), list, lists);
    }
}
