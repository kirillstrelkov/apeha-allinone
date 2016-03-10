package apeha.allinone.items;

import apeha.allinone.item.Item;
import org.junit.Ignore;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ItemsTesting {
    public static final int SIZE_OF_ALL_ITEMS = 677;
    TextFilesHandler handler = TextFilesHandler.getInstance();

    public static List<String> getItemNames(List<Item> items) {
        List<String> names = new LinkedList<String>();
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item next = iterator.next();
            names.add(next.getName());
        }
        Collections.sort(names);
        return names;
    }

    // @Test
    // public void specificSearchItemByProperty() throws Exception {
    // ItemList items = handler.getAllItems(handler.getFilesKovchegStore());
    // String precision = "Строгий";
    // String precision1 = "Мягкий";
    // int level = 11;
    // BigDecimal price = new BigDecimal("1080.00");
    // String[] str = new String[] { "Сила", "Злость", "Реакция",
    // "Оберег крита противника", "Оберег удачи",
    // "Оберег ответа противника", "Сложение", "Удача" };
    // String[] str1 = new String[] { "Сила", "Злость" };
    // String[] str2 = new String[] { "Сила", "Злость", "Удача", "Сложение" };
    // List<ItemWithScore> found = handler.getSpecificItemByPropertyAndScore(
    // items, str, precision1, level, price);
    // TextFilesHandler.printList(found);
    // }

    @Test
    public void checkSizesOfListsKovchegAndMaltOrden() throws Exception {
        Set<String> set2 = handler.getSetOfNamesMalt();
        // System.out.println("MALT ORDEN\nsize=" + set2.size() + "\n" + set2);
        Set<String> set = handler.getSetOfNamesKovcheg();
        // System.out.println("APEHA KOVCHEG LAVKA\nsize=" + set.size() + "\n" +
        // set);
        assertEquals("From malta and konvcheg no equal", set.size(),
                set2.size());

    }

    @Test
    public void checkItemNamesAndSize() throws Exception {
        Set<String> set = handler.getSetOfNamesKovcheg();
        Set<String> set2 = new TreeSet<String>();

        List<Item> allItems = handler.getAllItems();
        Iterator<Item> it = allItems.iterator();
        while (it.hasNext()) {
            set2.add(it.next().getName());
        }
        String string = set.toString();
        String string2 = set2.toString();
        // System.out.println("Names              :" + string);
        // System.out.println("Names from ItemList:" + string2);
        assertEquals(string, string2);
        assertEquals(string, getItemNames(allItems).toString());
        assertEquals("Incorrect size of ItemList", set.size(), allItems.size());
        assertEquals(set.size(), set2.size());
        assertEquals(SIZE_OF_ALL_ITEMS, set.size());
        assertEquals(SIZE_OF_ALL_ITEMS, set2.size());
        assertEquals(SIZE_OF_ALL_ITEMS, allItems.size());
    }

    @Test
    public void checkDifferenceBetweenSizes() throws Exception {
        Set<String> set = handler.getSetOfNamesMalt();
        // System.out.println("MALT ORDEN\nsize=" + set.size() + "\n" + set);
        Set<String> set2 = handler.getSetOfNamesKovcheg();
        // System.out.println("APEHA KOVCHEG LAVKA\nsize=" + set2.size() + "\n"
        // + set2);
        assertEquals(set.toString(), set2.toString());
        TreeSet<String> diff = (TreeSet<String>) handler.compareGetDifference(
                set, set2);
        if (diff.size() > 0)
            // System.out.println("difference:" + diff.size() + "\n" + diff);
        assertTrue("There is difference between sets", diff.size() == 0);
    }

    @Ignore
    @Test
    public void printItemSize() throws UnsupportedEncodingException {
        long freeMemory = Runtime.getRuntime().freeMemory();
        // System.out.println("mem without list:" + freeMemory);
        List<Item> allItems = handler.getAllItems();
        long afterListMem = Runtime.getRuntime().freeMemory();
        // System.out.println("mem without list:" + afterListMem);
        // System.out.println("mem diff:" + (freeMemory - afterListMem));
        Iterator<Item> iterator = allItems.iterator();
        int maxNameSize = 0;
        int maxBodySize = 0;
        while (iterator.hasNext()) {
            Item next = iterator.next();
            // UTF-8
            int itemNameSize = next.getName().getBytes("UTF-8").length;
            int itemBodySize = next.toString().getBytes("UTF-8").length;
            if (itemNameSize > maxNameSize)
                maxNameSize = itemNameSize;
            if (itemBodySize > maxBodySize)
                maxBodySize = itemBodySize;
            // System.out.println("Size of name:" + itemNameSize);
            // System.out.println("Size of body:" + itemBodySize);
        }

        // System.out.println("Max item name size:" + maxNameSize);
        // System.out.println("Max item body size:" + maxBodySize);
    }

}
