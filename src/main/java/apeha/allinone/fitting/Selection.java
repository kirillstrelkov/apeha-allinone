package apeha.allinone.fitting;

import apeha.allinone.common.Category;
import apeha.allinone.db.DbHandler;
import apeha.allinone.item.ModItem;
import apeha.allinone.search.ItemSearch;
import apeha.allinone.search.ItemWithScore;
import apeha.allinone.search.SearchType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Selection {
    static List<Category> SORTED_CATEGORIES = Lists.newArrayList(Category.ARMS,
            Category.SHIELDS, Category.AMULETS, Category.BELTS, Category.RINGS,
            Category.GLOVES, Category.VAMBRACES, Category.HELMETS,
            Category.ARMOURS, Category.BOOTS);
    final int INNER_LIMIT = 100;
    String maxSetPrice;
    String maxPrice;
    int maxLevel;
    int limit;
    SearchType searchType;
    List<Category> selectedCategories;
    List<String> selectedParams;
    List<String> itemNamesToSkip;

    private boolean stopped;

    Selection() {

    }

    public Selection(String maxSetPrice, String maxPrice, int maxLevel,
                     int limit, SearchType searchType,
                     List<Category> selectedCategories, List<String> selectedParams,
                     List<String> itemNamesToSkip) {
        this.maxSetPrice = maxSetPrice;
        this.maxPrice = maxPrice;
        this.maxLevel = maxLevel;
        this.limit = limit;
        this.searchType = searchType;
        this.selectedCategories = selectedCategories;
        this.selectedParams = selectedParams;
        this.itemNamesToSkip = itemNamesToSkip;
        stopped = false;
    }

    public static List<SearchedSet> selectSets(String maxSetPrice,
                                               String maxPrice, int maxLevel, int limit, SearchType searchType,
                                               List<Category> selectedCategories, List<String> selectedParams) {
        return selectSets(maxSetPrice, maxPrice, maxLevel, limit, searchType,
                selectedCategories, selectedParams, null);
    }

    public static List<SearchedSet> selectSets(String maxSetPrice,
                                               String maxPrice, int maxLevel, int limit, SearchType searchType,
                                               List<Category> selectedCategories, List<String> selectedParams,
                                               List<String> itemNamesToSkip) {
        Selection selection = new Selection(maxSetPrice, maxPrice, maxLevel,
                limit, searchType, selectedCategories, selectedParams,
                itemNamesToSkip);
        return selection.findSets();
    }

    public synchronized boolean isStopped() {
        return stopped;
    }

    public synchronized void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    List<SearchedSet> getSets(List<List<String>> list, List<String> params) {
        if (isStopped()) {
            return null;
        }

        List<SearchedSet> sets = Lists.newLinkedList();

        for (List<String> names : list)
            sets.add(getSearchedSet(names, params));

        return sets;
    }

    protected SearchedSet getSearchedSet(List<String> names, List<String> params) {
        if (isStopped()) {
            return null;
        }

        SearchedSet set = new SearchedSet();
        for (String name : names) {
            ModItem item = new ModItem(DbHandler.getItemByName(name));
            Category category = DbHandler.getCategoryByItemName(name);

            set.putOn(category, item);
        }

        set.updateScore(params);

        return set;
    }

    protected List<List<String>> getCartesianProduct(List<String> list1,
                                                     List<String> list2) {
        if (isStopped()) {
            return null;
        }

        List<List<String>> list = Lists.newLinkedList();

        for (String string1 : list1) {
            for (String string2 : list2) {
                list.add(Lists.newArrayList(string1, string2));
            }
        }

        return list;
    }

    protected List<List<String>> getCartesianProduct(
            List<List<String>> cartesian, List<String> list, int limit,
            String maxSetPrice, List<String> params) {
        if (isStopped()) {
            return null;
        }

        List<List<String>> newCartesian = Lists.newLinkedList();

        if (cartesian.size() == 0) {
            for (String name : list) {
                newCartesian.add(Lists.newArrayList(name));
            }
        } else {
            for (List<String> names : cartesian) {
                for (String name : list) {
                    List<String> tmp = Lists.newLinkedList(names);
                    tmp.add(name);

                    newCartesian.add(tmp);
                }
            }
        }

        newCartesian = limitCartesianByScore(newCartesian, limit, maxSetPrice,
                params);

        return newCartesian;
    }

    protected List<List<String>> limitCartesianByScore(
            List<List<String>> cartesian, int limit, String maxSetPrice,
            List<String> params) {
        if (isStopped()) {
            return null;
        }

        BigDecimal maxPrice = new BigDecimal(maxSetPrice);
        Map<Integer, List<List<String>>> map = Maps.newTreeMap();
        List<Integer> scores = Lists.newLinkedList();

        for (List<String> names : cartesian) {
            SearchedSet set = getSearchedSet(names, params);
            int score = set.getScore();

            if (!scores.contains(score))
                scores.add(score);

            if (map.containsKey(score)) {
                List<List<String>> list = map.get(score);
                list.add(names);
            } else {
                List<List<String>> list = Lists.newLinkedList();
                list.add(names);
                map.put(score, list);
            }

            if (map.size() > limit) {
                List<Integer> newLinkedList = Lists.newLinkedList(map.keySet());
                Collections.reverse(newLinkedList);

                Map<Integer, List<List<String>>> tmpMap = Maps.newTreeMap();
                for (int tmpScore : newLinkedList) {
                    tmpMap.put(tmpScore, map.get(tmpScore));
                }
                map = tmpMap;
            }

        }

        Collections.sort(scores);
        Collections.reverse(scores);

        if (isStopped()) {
            return null;
        }

        List<List<String>> cartesianSorted = Lists.newLinkedList();
        for (int score : scores) {
            List<SearchedSet> sets = Lists.newLinkedList();
            for (int mapScore : map.keySet())
                if (mapScore == score) {
                    List<List<String>> list = map.get(score);

                    for (List<String> names : list) {
                        SearchedSet set = this.getSearchedSet(names, params);

                        if (!sets.contains(set)) {
                            sets.add(set);
                            if (set.getPrice().compareTo(maxPrice) <= 0) {
                                Collections.sort(names);
                                cartesianSorted.add(names);
                                if (cartesianSorted.size() >= INNER_LIMIT) {
                                    return cartesianSorted;
                                }
                            }
                        }
                    }
                }
        }

        return cartesianSorted;
    }

    protected List<Category> getSortedCategories(List<Category> categories) {
        if (isStopped()) {
            return null;
        }

        List<Category> sortedCategories = Lists.newLinkedList();

        for (Category cat : SORTED_CATEGORIES)
            if (categories.contains(cat)) {
                int times = 1;
                if (cat.equals(Category.RINGS))
                    times = 8;
                else if (cat.equals(Category.ARMS)
                        || cat.equals(Category.SHIELDS))
                    times = 2;
                for (int i = 0; i < times; i++)
                    sortedCategories.add(cat);
            }

        return sortedCategories;
    }

    public List<SearchedSet> findSets() {
        if (isStopped()) {
            return null;
        }

        List<List<String>> catAndNames = Lists.newLinkedList();

        for (Category category : getSortedCategories(selectedCategories)) {
            List<String> names = Lists.newLinkedList();
            List<ItemWithScore> found = findItems(maxPrice, maxLevel,
                    searchType, selectedParams, category);

            addNamesIfAllowed(names, found);
            catAndNames.add(names);
        }

        List<List<String>> cartesianProduct = getAllCartesianProducts(
                maxSetPrice, limit, selectedParams, catAndNames);

        List<SearchedSet> sets = getSets(cartesianProduct, selectedParams);
        if (sets.size() > limit)
            sets = sets.subList(0, limit);

        if (isStopped()) {
            return null;
        }

        Collections.sort(sets);
        Collections.reverse(sets);

        return sets;
    }

    private void addNamesIfAllowed(List<String> names, List<ItemWithScore> found) {
        for (ItemWithScore item : found) {
            String name = item.getName();
            if (itemNamesToSkip == null || !itemNamesToSkip.contains(name))
                names.add(name);
        }
    }

    protected List<List<String>> getAllCartesianProducts(String maxSetPrice,
                                                         int limit, List<String> selectedParams,
                                                         List<List<String>> catAndNames) {
        if (isStopped()) {
            return null;
        }

        List<List<String>> cartesianProduct = Lists.newLinkedList();

        for (List<String> list : catAndNames) {
            if (list.size() > 0) {
                List<List<String>> cartesianProduct2 = getCartesianProduct(
                        cartesianProduct, list, limit, maxSetPrice,
                        selectedParams);
                cartesianProduct = cartesianProduct2;
            }
        }

        return cartesianProduct;
    }

    protected List<ItemWithScore> findItems(String maxPrice, int maxLevel,
                                            SearchType searchType, List<String> selectedParams,
                                            Category category) {
        List<ItemWithScore> found = ItemSearch.findItems(maxPrice, maxLevel,
                category.getType(), searchType, selectedParams);
        return found;
    }

}