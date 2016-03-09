package apeha.allinone.db;

import apeha.allinone.common.Category;
import apeha.allinone.item.Item;

import java.util.List;
import java.util.Map;

public interface ItemHandler {
    List<Item> getItems();

    Map<Category, List<Item>> getCategoryAndItems();
}
