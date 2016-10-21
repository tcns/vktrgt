package ru.tcns.vktrgt.domain.util;

import org.apache.commons.collections.FastTreeMap;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupIds;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by timur on 3/26/16.
 */
public class ArrayUtils {
    public <T extends Comparable> List<T> intersect(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>();
        int i1 = 0;
        int i2 = 0;
        while (i1 < list1.size() && i2 < list2.size()) {
            int res = list1.get(i1).compareTo(list2.get(i2));
            if (res == 0) {
                result.add(list1.get(i1));
                i1++;
                i2++;
            } else if (res > 0) {
                i2++;
            } else {
                i1++;
            }
        }
        return result;
    }

    public <T extends Comparable> Map<T, Integer> intersectWithCount(Map<T, Integer> map, List<T> list) {
        Set<Map.Entry<T, Integer>> entrySet = map.entrySet();
        if (map.isEmpty()) {
            map = new HashMap<>();
        }
        for (T e : list) {
            Integer val = map.get(e);
            if (val == null) {
                map.put(e, 1);
            } else {
                map.put(e, val + 1);
            }
        }
        return map;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map, V min) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue(Comparator.<V>reverseOrder()))
            .filter(a -> a.getValue().compareTo(min) >= 0)
            .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }
    public static <K, V extends Collection> Map<K, Integer>
    getCounts(Map<K, V> map) {
        Map<K, Integer> result = new LinkedHashMap<>();
        map.entrySet().parallelStream().forEach(a->result.put(a.getKey(), a.getValue().size()));
        return result;
    }


    public static List<String> getDelimetedLists(int from, int to, int max) {
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = from; i <= to; i++) {
            if (i % max == 0 || i == to) {
                builder.append(i);
                strings.add(builder.toString());
                builder = new StringBuilder();
            } else {
                builder.append(i);
                builder.append(",");
            }
        }
        return strings;

    }


    public static List<String> getDelimetedLists(List<String> numbers, int max) {
        StringBuilder builder = new StringBuilder();
        List<String> response = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            if ((i + 1) % max == 0 || builder.length() > VKDicts.MAX_REQUEST_LENGTH) {
                response.add(builder.toString());
                builder = new StringBuilder();
            }
            builder.append(numbers.get(i));
            if (i < numbers.size() - 1) {
                builder.append(",");
            }
        }
        response.add(builder.toString());
        return response;

    }

    public static<T> List<T> xor (List<T> l1, List<T> l2) {
        List<T> objects = new ArrayList<>();
        objects.addAll(l1);
        objects.addAll(l2);
        l1.retainAll(l2);
        objects.removeAll(l1);
        return objects;
    }
    public static List<String> getDelimetedLists(Integer from, Integer to, Integer max, List<GroupIds> idsList) {
        int start = Collections.binarySearch(idsList, new GroupIds(from));
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        if (!idsList.isEmpty()) {
            for (int i = 0; i < idsList.size() && idsList.get(i).getId() <= to; i++) {
                if (i % max == 0 || i == to) {
                    builder.append(idsList.get(i).getId());
                    strings.add(builder.toString());
                    builder = new StringBuilder();
                } else {
                    builder.append(idsList.get(i).getId());
                    builder.append(",");
                }
            }
            if (idsList.get(idsList.size() - 1).getId() < to) {
                strings.addAll(getDelimetedLists(idsList.get(idsList.size() - 1).getId(), to, max));
            }
        } else {
            strings.addAll(getDelimetedLists(from, to, max));
        }

        return strings;
    }
}
