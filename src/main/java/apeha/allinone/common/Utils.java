package apeha.allinone.common;

import apeha.allinone.item.Item;
import apeha.allinone.item.SimpleItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static DateTimeFormatter dateFormat = DateTimeFormat
            .forPattern("dd.MM.yy HH:mm");

    public static <T extends SimpleItem> T getItemFromList(String name,
                                                           List<T> list) {
        for (T next : list) {
            if (next.getName().equals(name))
                return next;
        }
        return null;
    }

    public static int getInteger(String pattern, String text) {
        Matcher matcher = Pattern.compile(pattern).matcher(text);

        if (matcher.find()) {
            String group = matcher.group();
            matcher = Pattern.compile("\\-*\\d+").matcher(group);

            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            } else
                return -1;
        } else
            return -1;
    }

    public static DateTime getCalendarFrom(String text) {
        int days = getInteger("\\d+д", text);
        int hours = getInteger("\\d+ч", text);
        int mins = getInteger("\\d+мин", text);

        if (days == -1)
            days = 0;
        if (hours == -1)
            hours = 0;
        if (mins == -1)
            mins = 0;
        if (days == 0 && hours == 0 && mins == 0)
            return null;
        else {
            return DateTime.now().plusDays(days).plusHours(hours)
                    .plusMinutes(mins);
        }
    }

    public static String formatStringFrom(DateTime date) {
        return dateFormat.print(date);
    }

    public static DateTime formatDateFrom(String date) {
        return dateFormat.parseDateTime(date);
    }

    public static Map<Category, List<Item>> getSortedCategoryAndItem(Map<Category, List<Item>> map) {
        Map<Category, List<Item>> sortedMap = Maps.newLinkedHashMap();
        List<String> listCat = Lists.newLinkedList();

        for (Category key : map.keySet())
            listCat.add(key.getType());

        Collections.sort(listCat);

        for (String next : listCat)
            for (Category key : map.keySet()) {
                if (key.getType().equals(next)) {
                    List<Item> list = map.get(key);

                    Collections.sort(list);

                    sortedMap.put(key, list);
                    break;
                }
            }

        return sortedMap;
    }

}
