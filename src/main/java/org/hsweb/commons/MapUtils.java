package org.hsweb.commons;


import java.util.*;

public class MapUtils {

    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static Map<String, Object> removeEmptyValue(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<>();
        if (map == null)
            return newMap;
        map.entrySet().stream().filter(entry -> !StringUtils.isNullOrEmpty(entry.getValue())).forEach(entry -> {
            newMap.put(entry.getKey(), entry.getValue());
        });
        return newMap;
    }

    public static <K extends Comparable, V> Map<K, V> sortMapByKey(Map<K, V> data) {
        Map<K, V> data_ = new LinkedHashMap<>();
        List<K> list = new LinkedList<>(data.keySet());
        Collections.sort(list);
        for (K k : list) {
            data_.put(k, data.get(k));
        }
        return data_;
    }


}
