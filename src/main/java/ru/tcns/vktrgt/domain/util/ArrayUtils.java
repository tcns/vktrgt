package ru.tcns.vktrgt.domain.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timur on 3/26/16.
 */
public class ArrayUtils {
    public<T extends Comparable> List<T> intersect(List<T>  list1, List<T> list2) {
        List<T> result = new ArrayList<>();
        int i1 = 0;
        int i2 = 0;
        while (i1<list1.size() && i2<list2.size()) {
            int res = list1.get(i1).compareTo(list2.get(i2));
            if (res==0) {
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

    public static List<String> getDelimetedLists(long from, long to, int max) {
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (long i = from; i<=to; i++) {
            if (i%max==0 || i==to) {
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
}
