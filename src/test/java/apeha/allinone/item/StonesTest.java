package apeha.allinone.item;

import apeha.allinone.item.stone.Facet;
import apeha.allinone.item.stone.Stone;
import apeha.allinone.market.MarketTest;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StonesTest {

    @Test
    public void alexandriteTest() {
        assertStoneNameAndProperty(Stone.ALEXANDRITE, "Александрит",
                Property.POWER);
    }

    @Test
    public void diamondTest() {
        assertStoneNameAndProperty(Stone.DIAMOND, "Алмаз", Property.AGILITY);
    }

    @Test
    public void amazonTest() {
        assertStoneNameAndProperty(Stone.AMAZON, "Амазонит", Property.FOTUNE);
    }

    @Test
    public void apatiteTest() {
        assertStoneNameAndProperty(Stone.APATITE, "Апатит", Property.REACTION);
    }

    @Test
    public void calaiteTest() {
        assertStoneNameAndProperty(Stone.CALAITE, "Бирюза",
                Property.CONSTITUTION);
    }

    @Test
    public void jasperTest() {
        assertStoneNameAndProperty(Stone.JASPER, "Яшма", Property.SPITE);
    }

    @Test
    public void lazuriteTest() {
        assertStoneNameAndProperty(Stone.LAZURITE, "Лазурит",
                Property.MARKSMANSHIP);
    }

    @Test
    public void malachiteTest() {
        assertStoneNameAndProperty(Stone.MALACHITE, "Малахит",
                Property.MASTERY_OF_WEAPON);
    }

    @Test
    public void obsidianTest() {
        assertStoneNameAndProperty(Stone.OBSIDIAN, "Обсидиан",
                Property.MASTERY_OF_DEFENCE);
    }

    @Test
    public void onyxTest() {
        assertStoneNameAndProperty(Stone.ONYX, "Оникс",
                Property.MASTERY_OF_FISTICUFFS);
    }

    @Test
    public void opalTest() {
        assertStoneNameAndProperty(Stone.OPAL, "Опал",
                Property.GUARD_OF_FORTUNE);
    }

    @Test
    public void pyriteTest() {
        assertStoneNameAndProperty(Stone.PYRITE, "Пирит",
                Property.GUARD_OF_DODGE);
    }

    @Test
    public void rubyTest() {
        assertStoneNameAndProperty(Stone.RUBY, "Рубин",
                Property.GUARD_OF_RESPONSE);
    }

    @Test
    public void sapphireTest() {
        assertStoneNameAndProperty(Stone.SAPPHIRE, "Сапфир",
                Property.GUARD_OF_CRITICAL_STRIKE);
    }

    @Test
    public void seleniteTest() {
        assertStoneNameAndProperty(Stone.SELENITE, "Селенит",
                Property.GUARD_OF_DODGE_ENEMY);
    }

    @Test
    public void chrysoliteTest() {
        assertStoneNameAndProperty(Stone.CHRYSOLITE, "Хризолит",
                Property.GUARD_OF_RESPONSE_ENEMY);
    }

    @Test
    public void fluoriteTest() {
        assertStoneNameAndProperty(Stone.FLUORITE, "Флуорит",
                Property.GUARD_OF_CRITICAL_STRIKE_ENEMY);
    }

    @Test
    public void emeraldTest() {
        assertStoneNameAndProperty(Stone.EMERALD, "Изумруд",
                Property.GUARD_OF_FORTUNE_ENEMY);
    }

    private void assertStoneNameAndProperty(Stone stone, String name,
                                            Property property) {
        assertEquals(name, stone.getName());
        assertEquals(property, stone.getProperty());
    }

    @Test
    public void facetTest() {
        int[] iFacets = {1, 3, 5, 7, 9, 11, 15, 19, 23};
        String[] sFacets1 = {"+1", "+3", "+5", "+7", "+9", "+11", "+15",
                "+19", "+23"};
        String[] sFacets2 = {"+5%", "+15%", "+25%", "+35%", "+45%", "+55%",
                "+75%", "+95%", "+115%"};
        String[] sFacets3 = {"-5%", "-15%", "-25%", "-35%", "-45%", "-55%",
                "-75%", "-95%", "-115%"};
        for (int i = 0; i < iFacets.length; i++) {
            assertEquals(iFacets[i], Facet.getFacet(sFacets1[i])[0].getValue());
            assertEquals(iFacets[i], Facet.getFacet(sFacets2[i])[0].getValue());
            assertEquals(iFacets[i], Facet.getFacet(sFacets3[i])[0].getValue());
        }
    }

    @Test
    public void itemMod1() {
        String item = new String("[Щит Древних]\n" + " Щит Древних\n"
                + "прочность: 250 	Количество: много\n"
                + "Цена в магазине: 1650.00 ст.\n" + "Требуемый Уровень: 7\n"
                + "Сложение: +10\n" + "Оберег удачи: +195%\n"
                + "Оберег ответа: +60%\n" + "Оберег крита: +40%\n"
                + "Оберег уворота: +45%\n" + "Броня ног: +80\n"
                + "Броня левой руки: +80\n" + "Броня правой руки: +80\n"
                + "Оберег удачи противника: -80%\n"
                + "Оберег уворота противника: -20%\n" + "Урон 0-0");
        ModItem modItem = ItemBuilder.createModItem(item);
        String[] expected = {"Оберег удачи: +115%",
                "Оберег удачи противника: -45%"};
        String[] actual = {modItem.getMod1().toString(),
                modItem.getMod2().toString()};
        assertEquals(Arrays.asList(expected).toString(), Arrays.asList(actual)
                .toString());
    }

    @Test
    public void itemMod2() {
        String item = new String("[Щит Древних]\n" + " Щит Древних\n"
                + "прочность: 250 	Количество: много\n"
                + "Цена в магазине: 1650.00 ст.\n" + "Требуемый Уровень: 7\n"
                + "Сложение: +10\n" + "Оберег удачи: +230%\n"
                + "Оберег ответа: +60%\n" + "Оберег крита: +40%\n"
                + "Оберег уворота: +45%\n" + "Броня ног: +80\n"
                + "Броня левой руки: +80\n" + "Броня правой руки: +80\n"
                + "Оберег удачи противника: -35%\n"
                + "Оберег уворота противника: -20%\n" + "Урон 0-0");
        ModItem modItem = ItemBuilder.createModItem(item);
        String[] expected = {"Оберег удачи: +75%", "Оберег удачи: +75%"};
        String[] actual = {modItem.getMod1().toString(),
                modItem.getMod2().toString()};
        assertEquals(Arrays.asList(expected).toString(), Arrays.asList(actual)
                .toString());
    }

    @Test
    public void itemMod3() {
        String item = new String(" [Небесный щит (мод.) (закл.)]\n"
                + "	Небесный щит (мод.) (закл.)\n"
                + "прочность: 100 / 100 	Количество: 1\n"
                + "Цена в магазине: 114.21 ст.\n" + "Требуемый Уровень: 8\n"
                + "Сила: -10\n" + "Ловкость: -10\n" + "Удача: +10\n"
                + "Сложение: +27\n" + "Оберег удачи: +15%\n"
                + "Броня корпуса: +25\n" + "Броня левой руки: +25\n"
                + "Броня правой руки: +25\n" + "Мастерство защиты: +10%\n"
                + "Урон 1-1");
        String[] expected = {"Сложение: +7", "Оберег удачи: +35%"};
        ModItem modItem = ItemBuilder.createModItem(item);
        String[] actual = {modItem.getMod1().toString(),
                modItem.getMod2().toString()};
        assertEquals(Arrays.asList(expected).toString(), Arrays.asList(actual)
                .toString());
    }

    @Test
    public void itemMod4() {
        String item = new String(" [Кольцо Злости (мод.) (закл.)]\n"
                + "	Кольцо Злости (мод.) (закл.)\n" + "прочность: 60\n"
                + "Цена в магазине: 8.55\n" + "Требуемый Уровень: 4\n"
                + "Сила: +10\n" + "Ловкость: +8\n" + "Реакция: -1\n"
                + "Злость: +22\n" + "Удача: +2\n" + "Сложение: +2\n"
                + "Броня ног: +2\n" + "");
        ModItem modItem = ItemBuilder.createModItem(item);
        String[] expected = {"Ловкость: +9", "Злость: +23"};
        String[] actual = {modItem.getMod1().toString(),
                modItem.getMod2().toString()};
        assertEquals(Arrays.asList(expected).toString(), Arrays.asList(actual)
                .toString());
    }

    @Test
    public void itemMod5() {
        String item = new String(" [Кольцо Злости (мод.) (закл.)]\n"
                + "	Кольцо Злости (мод.) (закл.)\n" + "прочность: 60\n"
                + "Цена в магазине: 8.55\n" + "Требуемый Уровень: 4\n"
                + "Сила: +10\n" + "Ловкость: -1\n" + "Реакция: -1\n"
                + "Злость: +9\n" + "Удача: +2\n" + "Сложение: +2\n"
                + "Броня ног: +2\n" + "");
        ModItem modItem = ItemBuilder.createModItem(item);
        String[] expected = {"Злость: +5", "Злость: +5"};
        String[] actual = {modItem.getMod1().toString(),
                modItem.getMod2().toString()};
        assertEquals(Arrays.asList(expected).toString(), Arrays.asList(actual)
                .toString());
    }

    @Ignore
    @Test
    public void allItemsInMarketTest() throws FileNotFoundException {
        String file = MarketTest.class.getClassLoader().getResource("market/allItemsInMarket").getFile();
        List<Item> items = TextParser.getItems(new FileReader(new File(file)));
        for (Item item : items) {
            // System.out.println(item.getName() + "\t" + Modification.getModifications(item));
        }
    }

}
