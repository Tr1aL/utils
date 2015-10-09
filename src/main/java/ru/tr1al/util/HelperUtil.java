package ru.tr1al.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class HelperUtil {

    private static final int BUILD_PATH_DIVISION_1 = 1_000_000_000;
    private static final int BUILD_PATH_DIVISION_2 = 1_000_000;
    private static final int BUILD_PATH_DIVISION_3 = 1_000;

    public static String buildPathForId(Integer id) {
        int p1 = Double.valueOf(Math.round(id.doubleValue() / BUILD_PATH_DIVISION_1)).intValue();
        int p2 = Double.valueOf(Math.round(id.doubleValue() / BUILD_PATH_DIVISION_2)).intValue();
        int p3 = Double.valueOf(Math.round(id.doubleValue() / BUILD_PATH_DIVISION_3)).intValue();
        return p1 + File.separator + p2 + File.separator + p3;
    }

    public static <T, O> Map<T, List<O>> listToMapByFieldName(Collection<O> list, String fieldName) {
        Map<T, List<O>> ret = new HashMap<>();
        for (O obj : list) {
            for (Field f : obj.getClass().getDeclaredFields()) {
                if (f.getName().equals(fieldName)) {
                    try {
                        T key = (T) f.get(obj);
                        if (key != null) {
                            List<O> value = ret.get(key);
                            if (value == null) {
                                value = new ArrayList<>();
                            }
                            value.add(obj);
                            ret.put(key, value);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        return ret;
    }

    public static <T, O> Set<T> listToFieldNameValueSet(Collection<O> list, String fieldName) {
        Set<T> ret = new HashSet<>();
        for (O obj : list) {
            for (Field f : obj.getClass().getDeclaredFields()) {
                if (f.getName().equals(fieldName)) {
                    try {
                        if (f.get(obj) != null) {
                            ret.add((T) f.get(obj));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        return ret;
    }
}
