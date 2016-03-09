package apeha.allinone.fitting;

import apeha.allinone.common.Category;
import apeha.allinone.db.DbHandler;
import apeha.allinone.item.Item;
import apeha.allinone.item.ModItem;
import apeha.allinone.item.Property;
import apeha.allinone.search.SearchType;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SelectionTest {
    private static final String[][] EXAMPLE = {
            {"Шлем Стихий", "Шлем Юпитера", "Шлем Привидения",
                    "Шлем Разрушителя", "Шлем Отваги", "Шлем Архангела",
                    "Шлем Могущества", "Шлем Невиновности", "Шлем Поклонения",
                    "Шлем Силы Древних", "Шлем Террора", "Шлем Пламенный",
                    "Шлем Ненависти", "Шлем Безразличия", "Шлем демона",
                    "Шлем Упорства", "Шлем Мужества", "Мифриловый Шлем",
                    "Шлем Прозрения", "Шлем Боли", "Шлем Кровавый",
                    "Шлем Луны", "Небесный шлем", "Огненный шлем",
                    "Шлем походный", "Шлем Владыки", "Шлем платиновый",
                    "Шлем Белого рыцаря", "Шлем Солнца", "Шлем Проникновения"},
            {"Амулет Пламени", "Амулет Очарования", "Амулет Доблести",
                    "Амулет Невиновности", "Белый крест", "Амулет Архангела",
                    "Амулет Перевоплощения", "Амулет Невозмутимости",
                    "Амулет Призрака", "Амулет спасения", "Амулет защиты",
                    "Амулет Рассудительности", "Кулон \"виктория\"",
                    "Чёрный крест", "Колье Храбрости", "Амулет Достоинства",
                    "Призрачный Амулет"},
            {"Латы Прозрения", "Латы Юпитера", "Латы Владыки", "Латы Крови",
                    "Ледяные Латы", "Латы Поклонения", "Латы Ненависти",
                    "Кольчуга Призрака", "Платиновый панцирь", "Латы Измора",
                    "Броня крестоносца", "Броня уворота", "Латы Достоинства",
                    "Доспехи оруженосца", "Латы Террора", "Кираса Блестящая",
                    "Броня Белого рыцаря", "Панцирь Мужества", "Латы Стихий",
                    "Латы Чести", "Золотые латы", "Латы Подавления",
                    "Кольчуга лёгкая", "Жакет из кожи", "Панцирь простой",
                    "Кольчуга полная", "Броня Доблести", "Волчёнка"},
            {"Тесак Очарования", "Меч Стихий", "Меч Смелости",
                    "Меч Привидения", "Рапира Мужества", "Меч Архангела",
                    "Меч Поклонения", "Палица Изворотливости",
                    "Нож-Кастет Спасения", "Меч волшебника", "Лунный кинжал",
                    "Кинжал Самурая", "Секира Разрушителя", "Дубина Прозрения",
                    "Меч Ветра", "Кинжал Достоинства", "Меч Страха",
                    "Меч Чести", "Кастет Крови", "Меч Тора",
                    "Меч Невиновности", "Меч Терпимости", "Кнут боевой",
                    "Булава Торина", "Посох Шипованный", "Скипетр Королей",
                    "Топор Луны", "Несущий Смерть", "Меч Молнии",
                    "Меч Юпитера", "Сабля Перевоплощения", "Палаш Упорства",
                    "Всепроникающий Меч", "Меч королей",
                    "Меч правосудия Хоббитов", "Меч странника",
                    "Топор двусторонний", "Божественный меч", "Лазурный меч",
                    "Меч изумрудный", "Меч ювелирный", "Меч Призрака",
                    "Сатанистский меч", "Стальные когти", "Посох Зевса",
                    "Топор Владыки", "Мифриловый Меч", "Меч сглаза",
                    "Меч Чёрного рыцаря", "Топор Возмездия", "Посох шамана",
                    "Золотой топор", "Когти боевые", "Кинжал Успокоения",
                    "Топор Спокойствия", "Кинжал Боли", "Алебарда сторожевая",
                    "Дубина ювелирная", "Булава Великана", "Скипетр обычный",
                    "Меч гладиатора", "Меч короткий", "Меч Таинос",
                    "Топор убийцы драконов", "Стальной Молот",
                    "Серебряный топор", "Серп раскаяния",
                    "Кастет \"скорпион\"", "Молот малахитовый",
                    "Молот раскалённый", "Топор войны", "Булава малая",
                    "Кастет \"лезвие\""},
            {"Пояс Отваги", "Пояс Юпитера", "Пояс Стихий", "Пояс Террора",
                    "Пояс Подавления", "Пояс Привидения", "Пояс Ненависти",
                    "Пояс Прозрения", "Пояс отчаяния", "Пояс спасения",
                    "Пояс Владыки", "Золотой пояс", "Пояс Достоинства",
                    "Пояс Невозмутимости", "Пояс странника", "Лазурный пояс",
                    "Огненный пояс", "Пояс охотника"},
            {"Поножи Стихий", "Поножи Очарования", "Поножи Отваги",
                    "Поножи Привидения", "Поножи Мужества",
                    "Поножи Разрушителя", "Поножи Невиновности",
                    "Поножи Поклонения", "Поножи Измора", "Поножи Безразличия",
                    "Поножи Уюта", "Сапоги превосходства", "Поножи Упорства",
                    "Сапоги Белого рыцаря", "Поножи Чести", "Сапоги Гордости",
                    "Огненные сапоги", "Сапоги охотника", "Поножи кольчужные",
                    "Поножи платиновые", "Поножи медные", "Золотые сапоги"},
            {"Перчатки Очарования", "Перчатки Владыки", "Перчатки Мужества",
                    "Перчатки Прозрения", "Перчатки Отваги",
                    "Перчатки Властелина", "Перчатки Подавления",
                    "Перчатки Рассудка", "Платиновые наручи",
                    "Перчатки Мертвеца", "Перчатки Терпимости",
                    "Перчатки Чести", "Перчатки Невиновности",
                    "Перчатки Безразличия", "Перчатки Невозмутимости",
                    "Перчатки Чёрного рыцаря", "Перчатки Юпитера",
                    "Наручи медные", "Перчатки Белого рыцаря",
                    "Перчатки Упорства", "Перчатки рейнджера"},
            {"Наручи Стихий", "Наручи Юпитера", "Наручи Привидения",
                    "Наручи Самурая", "Наручи Подавления", "Наручи Рассудка",
                    "Наручи Отваги", "Наручи Терпимости", "Наручи Террора",
                    "Наручи Измора", "Наручи Упорства", "Наручи Доблести",
                    "Наручи Перевоплощения", "Наручи Ледяные"},
            {"Кольцо Прозрения", "Кольцо Юпитера", "Кольцо Владыки",
                    "Кольцо Мужества", "Перстень Разрушителя",
                    "Кольцо Невиновности", "Кольцо Мертвеца",
                    "Кольцо Поклонения", "Кольцо Ненависти",
                    "Кольцо Отражения", "Небесный перстень жизни",
                    "Кольцо Безразличия", "Кольцо Разрушителя",
                    "Огненный перстень", "Кольцо Движения",
                    "Кольцо Повелителя Морей", "Кольцо Демона",
                    "Кольцо магической защиты", "Лазурное кольцо",
                    "Кольцо Хранителя", "Кольцо Терпимости", "Кольцо воды",
                    "Серебряное кольцо", "Кольцо медное", "Кольцо защиты леса"}};
    private static final List<SearchedSet> SEARCHED_SETS;
    private static final long timeForSelection;

    static {
        SearchType searchType = SearchType.OPTIMAL;

        List<Category> categories = Lists.newArrayList(Category.ARMS,
                Category.AMULETS, Category.BELTS, Category.RINGS,
                Category.GLOVES, Category.VAMBRACES, Category.HELMETS,
                Category.ARMOURS, Category.BOOTS);

        String setPrice = "19000";
        String maxPrice = "3000.00";
        int maxLevel = 13;
        int limit = 100;
        List<String> params = Lists.newArrayList("Сложение", "Ловкость",
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE.getName());
        long time = System.currentTimeMillis();
        SEARCHED_SETS = Selection.selectSets(setPrice, maxPrice, maxLevel,
                limit, searchType, categories, params);
        timeForSelection = System.currentTimeMillis() - time;
    }

    @Test
    public void cartesianProductNamesFirstAndSecondScoreTest() {
        String price = "19000";
        int limit = 100;
        List<String> params = Lists.newArrayList("Сложение", "Ловкость",
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE.getName());

        List<String> set1 = Lists.newArrayList(EXAMPLE[0]);
        List<String> set2 = Lists.newArrayList(EXAMPLE[1]);
        List<List<String>> cartesianProduct = new Selection()
                .getCartesianProduct(set1, set2);
        for (int i = 2; i < EXAMPLE.length; i++) {
            List<String> setI = Lists.newArrayList(EXAMPLE[i]);
            List<List<String>> cartesianProduct2 = new Selection()
                    .getCartesianProduct(cartesianProduct, setI, limit, price,
                            params);
            int firstScore = new Selection().getSearchedSet(
                    cartesianProduct2.get(0), params).getScore();
            int secondScore = new Selection().getSearchedSet(
                    cartesianProduct2.get(1), params).getScore();
            assertTrue(firstScore >= secondScore);
            cartesianProduct = cartesianProduct2;
        }
    }

    @Test
    public void checkNotFullSet() {
        String price = "33000";
        int limit = 10;
        List<String> params = Lists.newArrayList("Сложение", "Ловкость",
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE.getName());

        List<Category> cats = Lists.newArrayList(Category.AMULETS,
                Category.HELMETS);
        List<SearchedSet> sets = Selection.selectSets(price, "1900.00", 13,
                limit, SearchType.OPTIMAL, cats, params);
        for (SearchedSet set : sets) {
            // System.out.println(set.toString());
            System.out.println(set.amulet.getName() + " "
                    + set.helmet.getName());
            assertTrue(Integer.parseInt(set.amulet.getPropertiesAndValues()
                    .get(Property.REQUIRED_LEVEL)) >= 7);
            assertTrue(Integer.parseInt(set.helmet.getPropertiesAndValues()
                    .get(Property.REQUIRED_LEVEL)) >= 7);
        }
    }

    @Test
    public void checkSetScores() {
        int previous = 99999;
        for (SearchedSet set : SEARCHED_SETS) {
            int current = set.getScore();
            assertTrue(current <= previous);
            if (current < previous)
                previous = current;
        }
    }

    @Test
    public void noDuplicateRingNameInText() {
        SearchedSet set = SEARCHED_SETS.get(0);
        final String formattedString = set.toString();
        final List<String> lines = Arrays.asList(formattedString.split("\n"));
        for (String line : lines) {
            // System.out.println(line);
            if (line.length() > 0)
                assertEquals(1, Collections.frequency(lines, line));
        }
        System.out.println(formattedString);
    }

    @Test
    public void checkSetsWithGuardOfEnemy() {
        String price = "33000";
        int limit = 13;
        List<String> params = Lists.newArrayList(
                Property.GUARD_OF_FORTUNE_ENEMY.getName(),
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_RESPONSE_ENEMY.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE_ENEMY.getName());

        List<Category> cats = Lists.newArrayList(Category.AMULETS,
                Category.BELTS);
        List<SearchedSet> sets = Selection.selectSets(price, "1900.00", 13,
                limit, SearchType.OPTIMAL, cats, params);
        for (SearchedSet set : sets) {
            assertTrue(set.score > 0);
        }
    }

    @Test
    public void checkSetWithoutSomeItem() {
        String price = "33000";
        int limit = 13;

        List<String> params = Lists.newArrayList(
                Property.GUARD_OF_FORTUNE_ENEMY.getName(),
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_RESPONSE_ENEMY.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE_ENEMY.getName());

        List<Category> cats = Lists.newArrayList(Selection.SORTED_CATEGORIES);
        List<SearchedSet> sets = Selection.selectSets(price, "1900.00", 13,
                limit, SearchType.OPTIMAL, cats, params);

        for (SearchedSet set : sets) {
            assertTrue(set.score > 0);
            System.out.println(set);
        }
        assertTrue(sets.size() > 0);
    }

    @Test
    public void twoDifferentArms() {
        String price = "3000";
        int limit = 10;

        List<String> params = Lists.newArrayList(Property.AGILITY.getName(),
                Property.CONSTITUTION.getName());

        List<Category> cats = Lists.newArrayList(Category.ARMS);
        List<SearchedSet> sets = Selection.selectSets(price, "1900.00", 13,
                limit, SearchType.OPTIMAL, cats, params);

        for (SearchedSet set : sets) {
            assertTrue(set.score > 0);
            System.out.println(set);
        }
        assertTrue(sets.size() > 0);
    }

    @Test
    public void differentRings() {
        String price = "1500";
        int limit = 20;

        List<String> params = Lists.newArrayList("Сложение", "Ловкость",
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE.getName());

        List<Category> cats = Lists.newArrayList(Category.RINGS);
        List<SearchedSet> sets = Selection.selectSets(price, "1300.00", 13,
                limit, SearchType.OPTIMAL, cats, params);

        for (SearchedSet set : sets) {
            assertTrue(set.score > 0);
            System.out.println(set);
        }
        assertTrue(sets.size() > 0);
    }

    @Test
    public void myVSauto() {
        // 8 колец мертвеца, 1440 соток 208 нужных статов
        final int my = 208;

        String price = "1500";
        int limit = 5;

        List<String> params = Lists.newArrayList("Сложение", "Ловкость",
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE.getName());

        List<Category> cats = Lists.newArrayList(Category.RINGS);
        List<SearchedSet> sets = Selection.selectSets(price, "360.00", 13,
                limit, SearchType.OPTIMAL, cats, params);

        for (SearchedSet set : sets) {
            assertTrue(set.score > 0);
            // System.out.println(set);
        }
        assertTrue(sets.size() > 0);
        assertTrue(sets.get(0).getScore() >= my);
    }

    @Test
    public void compareSearchSets2() {
        List<String> params = Lists.newArrayList("Сложение", "Ловкость",
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE.getName());

        Item item1 = DbHandler.getItemByName("Кольцо Мертвеца");
        Item item2 = DbHandler.getItemByName("Кольцо Мертвеца");
        Item item3 = DbHandler.getItemByName("Кольцо Невиновности");
        Item item4 = DbHandler.getItemByName("Кольцо Невиновности");
        Item item5 = DbHandler.getItemByName("Кольцо Невиновности");
        List<Item> items = Lists
                .newArrayList(item1, item2, item3, item4, item5);

        SearchedSet player1 = new SearchedSet();
        for (Item item : items) {
            ModItem modItem = new ModItem(item);
            player1.putOn(modItem);
        }
        player1.updateScore(params);

        Item item11 = DbHandler.getItemByName("Кольцо Мертвеца");
        Item item33 = DbHandler.getItemByName("Кольцо Невиновности");
        List<Item> items2 = Lists.newArrayList(item33, item33, item11, item11,
                item33);

        SearchedSet player2 = new SearchedSet();
        for (Item item : items2) {
            ModItem modItem = new ModItem(item);
            player2.putOn(modItem);
        }
        player2.updateScore(params);

        assertEquals(player1.toString(), player2.toString());
    }

    @Test
    public void compareSearchSets() {
        List<String> params = Lists.newArrayList("Сложение", "Ловкость",
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE.getName());

        Item item1 = DbHandler.getItemByName("Кольцо Мертвеца");
        Item item2 = DbHandler.getItemByName("Кольцо Мертвеца");
        Item item3 = DbHandler.getItemByName("Кольцо Невиновности");
        Item item4 = DbHandler.getItemByName("Кольцо Невиновности");
        Item item5 = DbHandler.getItemByName("Кольцо Ненависти");
        List<Item> items = Lists
                .newArrayList(item1, item2, item3, item4, item5);

        SearchedSet player1 = new SearchedSet();
        for (Item item : items) {
            ModItem modItem = new ModItem(item);
            player1.putOn(modItem);
        }

        item1 = DbHandler.getItemByName("Кольцо Мертвеца");
        items = Lists.newArrayList(item1, item1, item1, item1, item1);

        SearchedSet player2 = new SearchedSet();
        for (Item item : items) {
            ModItem modItem = new ModItem(item);
            player2.putOn(modItem);
        }

        item1 = DbHandler.getItemByName("Кольцо Невиновности");
        items = Lists.newArrayList(item1, item1, item1, item1, item1);

        SearchedSet player3 = new SearchedSet();
        for (Item item : items) {
            ModItem modItem = new ModItem(item);
            player3.putOn(modItem);
        }

        List<SearchedSet> sets = Lists.newLinkedList();
        player1.updateScore(params);
        player2.updateScore(params);
        player3.updateScore(params);

        // System.out.println(player1.getScore() + "\t" +
        // player1.getSetPrice());
        // System.out.println(player2.getScore() + "\t" +
        // player2.getSetPrice());
        // System.out.println(player3.getScore() + "\t" +
        // player3.getSetPrice());

        sets.add(player1);
        sets.add(player2);
        sets.add(player3);
        Collections.sort(sets);
        Collections.reverse(sets);

        // System.out.println(sets.get(0).getScore() + "\t" +
        // sets.get(0).getSetPrice());
        // System.out.println(sets.get(1).getScore() + "\t" +
        // sets.get(1).getSetPrice());
        // System.out.println(sets.get(2).getScore() + "\t" +
        // sets.get(2).getSetPrice());

        assertEquals(sets.get(0).getScore(), 150);
        assertEquals(sets.get(0).getPrice(), new BigDecimal("1800.00"));
        assertEquals(sets.get(1).getScore(), 130);
        assertEquals(sets.get(1).getPrice(), new BigDecimal("900.00"));
        assertEquals(sets.get(2).getScore(), 130);
        assertEquals(sets.get(2).getPrice(), new BigDecimal("1125.00"));
    }

    @Test
    public void arms2AndRings8() {
        List<String> params = Lists.newArrayList("Сложение", "Ловкость",
                Property.GUARD_OF_DODGE_ENEMY.getName(),
                Property.GUARD_OF_FORTUNE.getName(),
                Property.GUARD_OF_CRITICAL_STRIKE.getName());

        ModItem ring = new ModItem(DbHandler.getItemByName("Кольцо Мертвеца"));
        ModItem arm = new ModItem(DbHandler.getItemByName("Тесак Очарования"));
        List<ModItem> items = Lists.newArrayList(arm, arm, ring, ring, ring,
                ring, ring, ring, ring, ring);

        SearchedSet player1 = new SearchedSet();
        for (ModItem item : items) {
            player1.putOn(item);
        }
        player1.updateScore(params);
        assertEquals(player1.getScore(), 370);
    }

    @Test
    public void selectionPerformanceTest() {
        System.out.println(timeForSelection);
        assertTrue(timeForSelection < 119836);
    }
}
