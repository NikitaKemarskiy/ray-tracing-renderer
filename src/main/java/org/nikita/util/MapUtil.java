package org.nikita.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapUtil {
    private MapUtil() {}

    public static <T, K> void addToList(Map<T, List<K>> map, T key, K value) {
        List<K> list = map.get(key);

        if (list == null) {
            list = new LinkedList<>();
        }

        list.add(value);

        map.put(key, list);
    }
}
