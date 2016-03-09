package apeha.allinone.db;

import apeha.allinone.common.Category;
import apeha.allinone.item.Item;
import apeha.allinone.item.Property;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DbHandler {
    public final static int DB_SIZE;
    public final static String TOP_PRICE;
    public final static int TOP_LEVEL;
    private final static ItemHandler itemHandler;
    private final static Map<Category, List<Item>> categoryAndItems;
    private final static Map<String, Item> nameAndItems;
    private final static Map<String, Category> nameAndcategories;
    private final static List<Item> items;

    static {
        itemHandler = new XMLHandler();
        categoryAndItems = itemHandler.getCategoryAndItems();
        items = itemHandler.getItems();
        nameAndItems = Maps.newHashMap();
        nameAndcategories = Maps.newHashMap();

        BigDecimal price = BigDecimal.ZERO;
        int topLevel = 0;

        for (Category cat : categoryAndItems.keySet()) {
            for (Item item : categoryAndItems.get(cat)) {
                String level = item.getPropertiesAndValues().get(
                        Property.REQUIRED_LEVEL);
                String iprice = item.getPropertiesAndValues().get(
                        Property.PRICE_IN_SHOP);

                try {
                    int ilevel = Integer.parseInt(level);
                    if (ilevel > topLevel)
                        topLevel = ilevel;
                } catch (NumberFormatException e) {
                }

                BigDecimal bd = new BigDecimal(iprice);
                if (bd.compareTo(price) > 0) {
                    price = bd;
                }

                String name = item.getName();
                nameAndItems.put(name, item);
                nameAndcategories.put(name, cat);
            }
        }

        TOP_PRICE = price.toString();
        TOP_LEVEL = topLevel;
        DB_SIZE = items.size();
    }

    public static ItemHandler getHandler() {
        return itemHandler;
    }

    public static Map<Category, List<Item>> getCategoryAndItems() {
        return categoryAndItems;
    }

    public static List<Item> getItems() {
        return items;
    }

    public static Item getItemByName(String name) {
        return nameAndItems.get(name);
    }

    public static Category getCategoryByItemName(String name) {
        return nameAndcategories.get(name);
    }

}
