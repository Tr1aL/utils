package ru.tr1al.util;

import java.util.*;

public class CollectionUtil {

    public static <T> T getRandomObject(List<T> col) {
        return getRandomObject(col, null);
    }

    public static <T> T getRandomObject(List<T> col, Integer seed) {
        if (col == null) throw new NullPointerException("CollectionUtil.getRandomObject: col is null!");
        if (seed != null) {
            Collections.shuffle(col, new Random(seed));
        } else {
            Collections.shuffle(col);
        }
        return col.iterator().next();
    }

    public static <T> Object[] join(T[] o1, T[] o2) {
        List<T> list = new ArrayList<T>();
        for (T o : o1)
            list.add(o);
        for (T o : o2)
            list.add(o);
        return list.toArray();
    }

    public static <T> List<T> getListPart(List<T> col, int part, int total) {

        int allSize = col.size(); //Всего элементов
        int ost = allSize % total; //остаток
        int partSize = (allSize - ost) / total; //сколько в каждой части точно элементов

        if (partSize == 0) {
            if (ost != 0) {
                if (part <= ost) {
                    return col.subList(part - 1, part);
                }
            }
            return new ArrayList<T>();
        } else {
            int begin = (part - 1) * (partSize + (ost != 0 ? 1 : 0));
            int end = part * (partSize + (ost != 0 ? 1 : 0));
            if (end > allSize)
                end = allSize;
            return col.subList(begin, end);
        }

    }

    public static <T> List<T> getListPart_old(List<T> col, int part, int total) {
        //System.out.println("part: " + part);
        int i = col.size() / total;
        //System.out.println("i: " + i);
        int rest = col.size() % total;
        // System.out.println("rest: " + rest);
        int begin = (part - 1) * i;
        //System.out.println("begin: " + begin);
        int beginRest = rest != 0 ? (part - 1 <= rest ? part - 1 : rest) : 0;
        //int beginRest = rest;
        //System.out.println("beginRest: " + beginRest);
        begin = begin + beginRest;
        //System.out.println("begin: " + begin);

        int end = part * i;
        //System.out.println("end: " + end);
        //int endRest = rest != 0 ? part : 0;
        //System.out.println("endRest: " + endRest);
        end += rest;
        if (end > col.size()) end = col.size();
        //System.out.println("end: " + end);


        return col.subList(begin, end);
    }

    public static String arrayToString(Object[] a, String separator) {
        StringBuilder result = new StringBuilder();
        if (a.length > 0) {
            result.append(a[0]);
            for (int i = 1; i < a.length; i++) {
                result.append(separator);
                result.append(a[i]);
            }
        }
        return result.toString();
    }

    public static String listToString(Collection list, String separator) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : list) {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(o);
        }
        return sb.toString();
    }

    public static List<Integer> convertStringListToIndegerList(List<String> list) {
        List<Integer> ret = new ArrayList<>();
        for (String s : list) {
            Integer id = TextUtil.getIntegerOrNull(s.trim());
            if (id != null) {
                ret.add(id);
            }
        }
        return ret;
    }

    public static Set<Integer> convertStringSetToIndegerSet(List<String> list) {
        Set<Integer> ret = new HashSet<>();
        for (String s : list) {
            Integer i = TextUtil.getIntegerOrNull(s);
            if (i != null) {
                ret.add(i);
            }
        }
        return ret;
    }

    public static <T> List<T> compare2Lists(List<T> list1, List<T> list2) {
        List<T> all = new ArrayList<T>();
        List<T> ret = new ArrayList<T>();
        all.addAll(list1);
        all.addAll(list2);
        for (T item : all) {
            if (list1.contains(item) && list2.contains(item)) {
                ret.add(item);
            }
        }
        return ret;
    }

    public static <T> List<List<T>> splitList(List<T> idColl, int size) {
        List<List<T>> metaList = new ArrayList<>();
        if (idColl != null) {
            int counter = (idColl.size() / size);
            if (counter > 0) {
                for (int i = 0; i < counter; i++) {
                    metaList.add(idColl.subList(i * size, (i + 1) * size));
                }
            }
            if ((idColl.size() % size) > 0) {
                metaList.add(idColl.subList(counter * size, idColl.size()));
            }
            return metaList;
        } else {
            return null;
        }
    }

    public static boolean containsIgnoreCase(Collection<String> list, String s) {
        for (String str : list) {
            if (str.equalsIgnoreCase(s))
                return true;
        }
        return false;
    }

    public static boolean contains(Collection col, Object obj){
        return col.contains(obj);
    }
}
