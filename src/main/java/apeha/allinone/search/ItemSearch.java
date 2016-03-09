package apeha.allinone.search;

import apeha.allinone.common.Category;
import apeha.allinone.common.Utils;
import apeha.allinone.db.DbHandler;
import apeha.allinone.item.Item;
import apeha.allinone.item.Property;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.util.*;

public class ItemSearch {
    private ItemSearch() {
    }

    public static List<ItemWithScore> findItems(String maxPrice, int maxLevel,
                                                int limit, String category, SearchType searchType,
                                                List<String> params) {
        List<Item> dbItems = DbHandler.getItems();
        Map<Category, List<Item>> categoryAndItems = DbHandler
                .getCategoryAndItems();
        List<ItemWithScore> itemsWithScore = Lists.newArrayList();
        List<Item> items = null;

        if (category.equals(Category.ALL.getType()))
            items = dbItems;
        else
            items = categoryAndItems.get(Category.getCategoryByType(category));

        for (Item item : items) {
            ItemWithScore iws = getItemWithScore(item, maxPrice, maxLevel,
                    searchType, params);
            if (iws != null) {
                itemsWithScore.add(iws);
            }
        }

        if (searchType.equals(SearchType.OPTIMAL)) {
            itemsWithScore = getItemListWithScoreInDescOrder(itemsWithScore,
                    DbHandler.DB_SIZE);
            itemsWithScore = getSortedAndOptimizedItems(itemsWithScore, limit);
        } else
            itemsWithScore = getItemListWithScoreInDescOrder(itemsWithScore,
                    limit);

        return itemsWithScore;
    }

    private static List<ItemWithScore> getItemListWithScoreInDescOrder(
            List<ItemWithScore> found, int limit) {
        TreeSet<Integer> set = Sets.newTreeSet();

        for (ItemWithScore item : found) {
            set.add(item.getGoodStats());
        }

        List<ItemWithScore> list = Lists.newLinkedList();
        Iterator<Integer> itSet = set.descendingIterator();
        while (itSet.hasNext()) {
            int nextVal = itSet.next();
            for (ItemWithScore item : found) {
                if (item.getGoodStats() == nextVal) {
                    list.add(item);
                    if (list.size() == limit)
                        return list;
                }
            }
        }

        return list;
    }

    private static ItemWithScore getItemWithScore(Item item, String maxPrice,
                                                  int maxLevel, SearchType searchType, List<String> properties) {
        Map<Property, String> propertiesAndValues = item
                .getPropertiesAndValues();
        BigDecimal itemPrice = new BigDecimal(
                propertiesAndValues.get(Property.PRICE_IN_SHOP));
        BigDecimal bdmaxPrice = new BigDecimal(maxPrice);
        itemPrice = itemPrice.setScale(2);
        bdmaxPrice = bdmaxPrice.setScale(2);
        String level = propertiesAndValues.get(Property.REQUIRED_LEVEL);
        int levelMin = 0;
        int levelMax = 0;

        if (level.contains("-")) {
            levelMin = Utils.getInteger("\\d+-", level);
            levelMax = Utils.getInteger("-\\d+", level);
        } else {
            levelMin = Integer.parseInt(level);
            levelMax = levelMin;
        }

        if (itemPrice.compareTo(bdmaxPrice) <= 0 && levelMin <= maxLevel
                && levelMax <= maxLevel) {
            Property[] props = new Property[properties.size()];

            for (int i = 0; i < properties.size(); i++) {
                props[i] = Property.getPropertyFrom(properties.get(i));
                String value = propertiesAndValues.get(props[i]);

                if (searchType.equals(SearchType.STRONG) && (value == null))
                    return null;
                if (Property.STATS.contains(props[i]) && value != null
                        && value.contains("-"))
                    return null;
            }

            ItemWithScore iws = new ItemWithScore(item);
            iws.computeScore(props);

            if (searchType.equals(SearchType.OPTIMAL)
                    && (iws.getGoodStats() - iws.getBadStats()) <= 0)
                return null;

            return iws;
        } else
            return null;
    }

    public static List<ItemWithScore> getSortedAndOptimizedItems(
            List<ItemWithScore> found, int limit) {
        List<ItemWithScore> sorted = Lists.newLinkedList();
        Set<Integer> ints = Sets.newTreeSet();

        for (ItemWithScore item : found) {
            int diff = item.getGoodStats() - item.getBadStats();
            ints.add(diff);
        }

        List<Integer> values = new LinkedList<Integer>(ints);
        Collections.sort(values);
        Collections.reverse(values);

        for (int i = 0; i < values.size(); i++) {
            int integer = values.get(i);

            for (ItemWithScore item : found) {
                int diff = item.getGoodStats() - item.getBadStats();
                if (diff == integer)
                    sorted.add(item);
                if (limit != -1 && sorted.size() == limit)
                    return sorted;
            }
        }

        return sorted;
    }

    public static List<ItemWithScore> sortItems(List<ItemWithScore> found) {
        return getSortedAndOptimizedItems(found, -1);
    }

    public static List<ItemWithScore> findItems(String maxPrice, int maxLevel,
                                                String category, SearchType searchType, List<String> params) {
        return findItems(maxPrice, maxLevel, DbHandler.DB_SIZE, category,
                searchType, params);
    }

    public static List<ItemWithScore> getSortedItemsWithScore(
            List<ItemWithScore> items) {
        return getSortedItemsWithScore(items, items.size());
    }

    public static List<ItemWithScore> getSortedItemsWithScore(
            List<ItemWithScore> items, int limit) {
        List<Integer> ints = Lists.newLinkedList();
        List<ItemWithScore> sorted = Lists.newLinkedList();

        for (ItemWithScore item : items) {
            int score = item.getGoodStats() - item.getBadStats();
            if (!ints.contains(score))
                ints.add(score);
        }

        Collections.sort(ints);
        Collections.reverse(ints);

        for (int score : ints) {
            for (ItemWithScore item : items) {
                if (score == (item.getGoodStats() - item.getBadStats())) {
                    sorted.add(item);
                    if (sorted.size() >= limit)
                        return sorted;
                }
            }
        }

        return sorted;
    }

}
