package apeha.allinone.search;

import apeha.allinone.common.Category;
import apeha.allinone.db.DbHandler;
import apeha.allinone.item.Item;
import apeha.allinone.item.ItemBuilder;
import apeha.allinone.item.ItemTest;
import apeha.allinone.item.Property;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ItemScoreTest {
    @Test
    public void itemScoreTest() {
        ItemWithScore itemWithScore = new ItemWithScore(
                ItemBuilder.createItem(ItemTest.SHOOTING));
        assertEquals(30, itemWithScore.getGoodStats());
        assertEquals(0, itemWithScore.getBadStats());

        itemWithScore = new ItemWithScore(
                ItemBuilder.createItem(ItemTest.SHIELD));
        assertEquals(66, itemWithScore.getGoodStats());
        assertEquals(0, itemWithScore.getBadStats());
    }

    @Test
    public void item1ScoreTest() {
        String name = "Кольцо Злости";
        Item item = DbHandler.getItemByName(name);
        ItemWithScore iws = new ItemWithScore(item);
        assertEquals(11, iws.getGoodStats());
        assertEquals(0, iws.getBadStats());
        iws.computeScore(Property.POWER);
        assertEquals(10, iws.getGoodStats());
        assertEquals(1, iws.getBadStats());
    }

    @Test
    public void item2ScoreTest() {
        String name = "Кольцо Баланса";
        Item item = DbHandler.getItemByName(name);
        ItemWithScore iws = new ItemWithScore(item);
        assertEquals(28, iws.getGoodStats());
        assertEquals(0, iws.getBadStats());

        iws.computeScore(Property.GUARD_OF_CRITICAL_STRIKE_ENEMY);
        assertEquals(7, iws.getGoodStats());
        assertEquals(21, iws.getBadStats());

        iws.computeScore(Property.GUARD_OF_FORTUNE_ENEMY);
        assertEquals(7, iws.getGoodStats());
        assertEquals(21, iws.getBadStats());

        iws.computeScore(Property.GUARD_OF_RESPONSE_ENEMY);
        assertEquals(7, iws.getGoodStats());
        assertEquals(21, iws.getBadStats());

        iws.computeScore(Property.GUARD_OF_DODGE_ENEMY);
        assertEquals(7, iws.getGoodStats());
        assertEquals(21, iws.getBadStats());
    }

    @Test
    public void item3ScoreTest() {
        String name = "Меч Колдуна";
        Item item = DbHandler.getItemByName(name);
        ItemWithScore iws = new ItemWithScore(item);
        assertEquals(28, iws.getGoodStats());
        assertEquals(0, iws.getBadStats());

        iws.computeScore(Property.POWER);
        assertEquals(20, iws.getGoodStats());
        assertEquals(8, iws.getBadStats());

        iws.computeScore(Property.POWER, Property.AGILITY,
                Property.CONSTITUTION);
        assertEquals(14, iws.getGoodStats());
        assertEquals(14, iws.getBadStats());

        iws.computeScore(Property.DAMAGE);
        assertEquals(12, iws.getGoodStats());
        assertEquals(16, iws.getBadStats());
    }

    @Test
    public void searchTypesTest() {
        String[] expected = {"Строгий", "Оптимальный", "Мягкий"};
        assertTrue(Arrays.equals(expected, SearchType.getTypes()));
    }

    @Test
    public void searchForItemTest1() {
        String maxPrice = "1500.00";
        String category = Category.RINGS.getType();
        int maxLevel = 7;
        int limit = 100;
        SearchType searchType = SearchType.STRONG;
        List<String> params = Lists.newArrayList("Сила", "Сложение");
        List<ItemWithScore> found = ItemSearch.findItems(maxPrice, maxLevel,
                limit, category, searchType, params);
        // Iterator<ItemWithScore> iterator = found.iterator();
        // while(iterator.hasNext())
        // System.out.println(iterator.next().getName());
        assertEquals(7, found.size());
    }

    @Test
    public void searchForItemTest2() {
        String maxPrice = "1500.00";
        String category = Category.ALL.getType();
        int maxLevel = 7;
        int limit = 100;
        SearchType searchType = SearchType.STRONG;
        List<String> params = Lists.newArrayList("Ловкость", "Реакция",
                "Злость", "Удача");
        List<ItemWithScore> found = ItemSearch.findItems(maxPrice, maxLevel,
                limit, category, searchType, params);
        // Iterator<ItemWithScore> iterator = found.iterator();
        // while(iterator.hasNext()) {
        // ItemWithScore next = iterator.next();
        // System.out.println(next.getName() + ", " + next.getScore());
        // }
        assertEquals(12, found.size());
    }

    @Test
    public void foundInDescOrderTypeStrong() {
        String maxPrice = "1900.00";
        int maxLevel = 13;
        int limit = 10;
        SearchType searchType = SearchType.STRONG;
        List<String> params = Lists.newArrayList("Ловкость", "Реакция");

        List<String> categories = Lists.newArrayList(
                Category.AMULETS.getType(), Category.ARMOURS.getType(),
                Category.ARMS.getType(), Category.BELTS.getType(),
                Category.BOOTS.getType(), Category.GLOVES.getType(),
                Category.HELMETS.getType(), Category.RINGS.getType(),
                Category.SHIELDS.getType(), Category.SHOOTING_ARMS.getType(),
                Category.VAMBRACES.getType());
        for (String category : categories) {
            List<ItemWithScore> found = ItemSearch.findItems(maxPrice,
                    maxLevel, limit, category, searchType, params);
            int previous = 9999;
            for (ItemWithScore iws : found) {
                assertTrue(previous >= iws.getGoodStats());
                if (previous > iws.getGoodStats())
                    previous = iws.getGoodStats();
            }
        }
    }

    @Test
    public void foundInDescOrderTypeWeak() {
        String maxPrice = "1900.00";
        int maxLevel = 13;
        int limit = 10;
        SearchType searchType = SearchType.WEAK;
        List<String> params = Lists.newArrayList("Ловкость", "Реакция");

        List<String> categories = Lists.newArrayList(
                Category.AMULETS.getType(), Category.ARMOURS.getType(),
                Category.ARMS.getType(), Category.BELTS.getType(),
                Category.BOOTS.getType(), Category.GLOVES.getType(),
                Category.HELMETS.getType(), Category.RINGS.getType(),
                Category.SHIELDS.getType(), Category.SHOOTING_ARMS.getType(),
                Category.VAMBRACES.getType());
        for (String category : categories) {
            List<ItemWithScore> found = ItemSearch.findItems(maxPrice,
                    maxLevel, limit, category, searchType, params);
            int previous = 9999;
            for (ItemWithScore iws : found) {
                int count = iws.getGoodStats();
                assertTrue(previous >= count);
                if (previous > count)
                    previous = count;
            }
        }
    }

    @Test
    public void foundInDescOrderTypeOptimal() {
        String maxPrice = "1900.00";
        int maxLevel = 13;
        int limit = 10;
        SearchType searchType = SearchType.OPTIMAL;
        List<String> params = Lists.newArrayList("Ловкость", "Реакция");

        List<String> categories = Lists.newArrayList(
                Category.AMULETS.getType(), Category.ARMOURS.getType(),
                Category.ARMS.getType(), Category.BELTS.getType(),
                Category.BOOTS.getType(), Category.GLOVES.getType(),
                Category.HELMETS.getType(), Category.RINGS.getType(),
                Category.SHIELDS.getType(), Category.SHOOTING_ARMS.getType(),
                Category.VAMBRACES.getType());
        for (String category : categories) {
            List<ItemWithScore> found = ItemSearch.findItems(maxPrice,
                    maxLevel, limit, category, searchType, params);
            int previous = 9999;
            for (ItemWithScore iws : found) {
                int count = iws.getGoodStats() - iws.getBadStats();
                assertTrue(previous >= count);
                if (previous > count)
                    previous = count;
            }
        }
    }

    @Test
    public void searchForAll() {
        String maxPrice = "500.00";
        String category = Category.RINGS.getType();
        int maxLevel = 13;
        int limit = 700;
        SearchType searchType = SearchType.OPTIMAL;
        List<String> params = Lists.newArrayList("Сложение", "Ловкость",
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE.getName());
        List<ItemWithScore> found = ItemSearch.findItems(maxPrice, maxLevel,
                limit, category, searchType, params);
        for (ItemWithScore item : found) {
            System.out.print(item.getName());
            int diff = item.getGoodStats() - item.getBadStats();
            System.out.println("\t" + diff);
        }
        // TODO
    }

    @Test
    public void searchForItemTest3() {
        String maxPrice = "1800.00";
        String category = Category.BOOTS.getType();
        int maxLevel = 13;
        int limit = 700;
        SearchType searchType = SearchType.OPTIMAL;
        // String[] params = {"Реакция", "Ловкость",
        // Property.GUARD_OF_FORTUNE.getName() ,
        // Property.GUARD_OF_CRITICAL_STRIKE.getName() ,
        // Property.GUARD_OF_DODGE_ENEMY.getName() };
        List<String> params = Lists.newArrayList("Сложение", "Реакция",
                Property.GUARD_OF_RESPONSE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName());
        // String[] params = {"Ловкость", "Сила",
        // Property.GUARD_OF_DODGE_ENEMY.getName()};
        // String[] params = { Property.CONSTITUTION.getName(),
        // Property.GUARD_OF_FORTUNE.getName(),
        // Property.GUARD_OF_CRITICAL_STRIKE_ENEMY.getName(),
        // Property.SPITE.getName(), Property.MASTERY_OF_WEAPON.getName() };
        List<ItemWithScore> found = ItemSearch.findItems(maxPrice, maxLevel,
                limit, category, searchType, params);

        for (ItemWithScore item : found) {
            int goodbad = item.getGoodStats() - item.getBadStats();
            Map<Property, String> props = item.getPropertiesAndValues();
            System.out.println(String.format("%s\t%d\t%s\t%s", item.getName(),
                    goodbad, props.get(Property.PRICE_IN_SHOP),
                    props.get(Property.DURABILITY)));
        }
        // assertEquals(12, found.size());
        // TODO
    }

    @Test
    public void containsShopPrice() {
        String maxPrice = "1800.00";
        String category = Category.BOOTS.getType();
        int maxLevel = 13;
        int limit = 700;
        SearchType searchType = SearchType.OPTIMAL;
        List<String> params = Lists.newArrayList("Сложение", "Реакция",
                Property.GUARD_OF_RESPONSE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName());
        List<ItemWithScore> found = ItemSearch.findItems(maxPrice, maxLevel,
                limit, category, searchType, params);

        ItemWithScore iws = found.get(0);
        Property priceInShop = Property.PRICE_IN_SHOP;
        assertTrue(iws.getPropertiesAndValues().keySet().contains(priceInShop));
        assertTrue(iws.toString().contains(priceInShop.getName()));
        assertTrue(iws.getProperties().contains(priceInShop.getName()));
    }
}
