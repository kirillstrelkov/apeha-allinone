package apeha.allinone.db;

import apeha.allinone.item.Item;
import apeha.allinone.item.ItemSupportW;

import java.util.List;
import java.util.Map;

public interface ItemRWSupport extends ItemSupportW {
    void setCategoryAndItems(Map<String, List<Item>> catAndItems);
}
