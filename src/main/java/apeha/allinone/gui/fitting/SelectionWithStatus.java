package apeha.allinone.gui.fitting;

import apeha.allinone.common.Category;
import apeha.allinone.fitting.SearchedSet;
import apeha.allinone.fitting.Selection;
import apeha.allinone.gui.status.Status;
import apeha.allinone.search.ItemWithScore;
import apeha.allinone.search.SearchType;

import java.util.List;

public class SelectionWithStatus extends Selection {
    private Status status;

    public SelectionWithStatus(Status status, String maxSetPrice,
                               String maxPrice, int maxLevel, int limit, SearchType searchType,
                               List<Category> selectedCategories, List<String> selectedParams,
                               List<String> itemNamesToSkip) {
        super(maxSetPrice, maxPrice, maxLevel, limit, searchType,
                selectedCategories, selectedParams, itemNamesToSkip);
        this.status = status;
    }

    @Override
    protected SearchedSet getSearchedSet(List<String> names, List<String> params) {
        status.setStatus(String.format("Поиск комплектов из %d вещей...",
                names.size()));
        return super.getSearchedSet(names, params);
    }

    @Override
    protected List<List<String>> getCartesianProduct(List<String> list1,
                                                     List<String> list2) {
        return super.getCartesianProduct(list1, list2);
    }

    @Override
    protected List<List<String>> getCartesianProduct(
            List<List<String>> cartesian, List<String> list, int limit,
            String maxSetPrice, List<String> params) {
        return super.getCartesianProduct(cartesian, list, limit, maxSetPrice,
                params);
    }

    @Override
    protected List<List<String>> limitCartesianByScore(
            List<List<String>> cartesian, int limit, String maxSetPrice,
            List<String> params) {
        return super.limitCartesianByScore(cartesian, limit, maxSetPrice,
                params);
    }

    @Override
    protected List<Category> getSortedCategories(List<Category> categories) {
        return super.getSortedCategories(categories);
    }

    @Override
    protected List<List<String>> getAllCartesianProducts(String maxSetPrice,
                                                         int limit, List<String> selectedParams,
                                                         List<List<String>> catAndNames) {
        status.setStatus("Поиск всех возможных вариантов...");
        return super.getAllCartesianProducts(maxSetPrice, limit,
                selectedParams, catAndNames);
    }

    @Override
    protected List<ItemWithScore> findItems(String maxPrice, int maxLevel,
                                            SearchType searchType, List<String> selectedParams,
                                            Category category) {
        status.setStatus("Поиск вещей в категории: " + category.getType());
        return super.findItems(maxPrice, maxLevel, searchType, selectedParams,
                category);
    }

    @Override
    public List<SearchedSet> findSets() {
        try {
            List<SearchedSet> sets = super.findSets();
            return sets;
        } catch (NullPointerException e) {
            return null;
        }
    }

}
