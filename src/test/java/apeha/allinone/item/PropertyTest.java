package apeha.allinone.item;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class PropertyTest {
    // TODO extract to separate enum Property

    private static String[] MOST_PROPERTIES = new String[]{
            "Броня головы: +10", "Броня корпуса: +10", "Броня ног: +10",
            "Броня левой руки: +10", "Броня правой руки: +10",
            "Влияние силы: 50%", "Зарядов 75", "Сила: +7", "Ловкость: +7",
            "Реакция: +7", "Злость: +7", "Удача: +7", "Сложение: +7",
            "Ловкость: -1", "Реакция: -1", "Злость: -1",
            "Мастерство кулачного боя: +20%", "Мастерство защиты: +20%",
            "Мастерство владения оружием: +20%", "Меткость: +20%",
            "Оберег удачи: +70%", "Оберег ответа: +70%", "Оберег крита: +70%",
            "Оберег уворота: +70%", "Оберег удачи противника: -13%",
            "Оберег ответа противника: -13%", "Оберег крита противника: -13%",
            "Оберег уворота противника: -13%", "Радиус поражения: 6",
            "Точность: 80%", "Требуемый Уровень: 8", "Требуемый Уровень: 5-7",
            "Урон 4-20", "Требуемая Раса: Эльф", "Требуемая Раса: Хоббит",
            "Требуемая Раса: Человек", "Требуемая Раса: Гном",
            "Требуемая Раса: Дракон", "Требуемая Раса: Орк",
            "Цена в магазине: 32.00 ст.", "прочность: 50", "Двуручное."};

    @Before
    public void before() {
        // System.out.println("======================================================");
    }

    @Test
    public void propertyNameTest() {
        for (String property : MOST_PROPERTIES) {
            String actual = Property.getPropertyFrom(property).getName();
            Matcher matcher = Pattern.compile("[а-яА-Я ]+").matcher(property);
            if (matcher.find()) {
                String expected = matcher.group().trim();
                // System.out.println(String.format("expected '%s' actual '%s'",
                // expected, actual));
                assertEquals(expected, actual);
            } else
                fail("Pattern not found in: " + property);
        }
    }

    @Test
    public void propertyValueTest() {
        for (String property : MOST_PROPERTIES) {
            String actual = Property.getPropertyValueFrom(property);
            String expected = property
                    .replaceAll(Property.getPropertyFrom(property).getName(),
                            "").replaceAll("(: )|(\\.$)", "").trim();
            Matcher matcher = Pattern.compile("\\d+\\.\\d+").matcher(expected);
            if (matcher.find())
                expected = matcher.group();
            if (expected.contains("много"))
                expected = "Неизвестная раса";
            // System.out.println(String.format("expected '%s' actual '%s'",
            // expected, actual));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void twoHandedArm() {
        String line = "Двуручное.";
        String name = Property.getPropertyFrom(line).getName();
        String value = Property.getPropertyValueFrom(line).toString();
        assertEquals("Двуручное", name);
        assertEquals("", value);
    }

    @Test
    public void textContainsProperty() {
        for (String text : MOST_PROPERTIES) {
            Property propertyFrom = Property.getPropertyFrom(text);
            // System.out.println(String.format("'%s' contains '%s'", text,
            // propertyFrom));
            assertNotNull(propertyFrom);
        }
    }

    @Test
    public void testNameIsNotSetIfPropertyUse() {
        for (String text : MOST_PROPERTIES) {
            String name = TextParser.getName(text);
            assertEquals(null, name);
        }
        String name = "	Шлем силы древних	";
        assertEquals(name.trim(), TextParser.getName(name));
        name = "[Шлем силы древних]";
        assertEquals(null, TextParser.getName(name));
    }

    @Test
    public void durabilityTest() {
        String name = "прочность: 100 / 100";
        assertEquals("прочность: 100/100", Property.formatProperty(name));
        name = "прочность: 100/100";
        assertEquals(name, Property.formatProperty(name));
        name = "прочность: 100";
        assertEquals(name, Property.formatProperty(name));
    }

    @Test
    public void testFormatProperty() {
        for (String line : MOST_PROPERTIES) {
            String name = Property.getPropertyNameFrom(line);
            String value = Property.getPropertyValueFrom(line);
            // System.out.println(name + " " + value);
            assertEquals(line.replaceAll("( ст.)|(\\.$)", ""),
                    Property.formatProperty(line));
            assertEquals(Property.formatProperty(name + value),
                    Property.formatProperty(line));
        }
    }

    @Test
    public void priceInMarketTest() {
        String price = "Цена: 900.00 ст. + 0.00";
        assertEquals(price, Property.formatProperty(price));
        price = "Цена: 0.85 ст. + 1.78";
        assertEquals(price, Property.formatProperty(price));
        price = "Цена: 0.85 ст.";
        assertEquals(price, Property.formatProperty(price));
        price = "Цена: 0.85";
        assertEquals(price, Property.formatProperty(price));
        price = "Цена: 0.85 ст. + 1.78 ст.";
        assertEquals(price, Property.formatProperty(price));
        price = "Цена:0.85ст.+1.78ст.";
        assertEquals("Цена: 0.85ст.+1.78ст.", Property.formatProperty(price));
        price = "Цена:  0.85  ст.  +  1.78  ст.  ";
        assertEquals("Цена: 0.85  ст.  +  1.78  ст.",
                Property.formatProperty(price));
    }

    @Test
    public void spellStringTest() {
        String text = "Наложено заклятие еще: 19д 16ч 19мин";
        assertEquals(text, Property.formatProperty(text));
        text = "Наложено заклятие еще: 19д 0ч 0мин";
        assertEquals(text, Property.formatProperty(text));
        text = "Наложено заклятие еще: 19д 16ч";
        assertEquals(text, Property.formatProperty(text));
        text = "Наложено заклятие еще: 19д";
        assertEquals(text, Property.formatProperty(text));
        text = "Наложено заклятие еще: 16ч 19мин";
        assertEquals(text, Property.formatProperty(text));
        text = "Наложено заклятие еще: 19мин";
        assertEquals(text, Property.formatProperty(text));
    }

    @Test
    public void lifeTimeTest() {
        String lifeTime = "Время жизни: 14д 21ч 43мин";
        // System.out.println(Property.formatProperty(lifeTime));
        assertEquals("Наложено заклятие еще: 14д 21ч 43мин",
                Property.formatProperty(lifeTime));
        lifeTime = "Время жизни: бессрочно";
        // System.out.println(Property.formatProperty(lifeTime));
        assertEquals(null, Property.formatProperty(lifeTime));
    }

    @Test
    public void searchedInMarketItemTest() {
        String itemString = " [Кольцо Поклонения (мод.)]\n"
                + "	Кольцо Поклонения (мод.) 	Лавка: [El] 666dub666 12 [i]\n"
                + "Цена: 105.00 + 5.00\n" + "Количество: 1\n"
                + "Требуемый Уровень: 6\n" + "Реакция: +12\n"
                + "Сложение: +8\n" + "Оберег удачи: +35%\n"
                + "Оберег крита: +35%\n" + "Оберег ответа противника: -35%";
        itemString = itemString.replaceAll("Лавка", "\nЛавка");
        Item item = ItemBuilder.createItem(itemString);
        assertEquals("[El] 666dub666 12 [i]",
                item.getPropertiesAndValues().get(Property.SHOP));
    }

    @Test
    public void getScoreTest() {
        String[] lines = {"Сила: +30", "Ловкость: +28", "Реакция: +41",
                "Злость: 0", "Удача: +13", "Сложение: +64",
                "Мастерство кулачного боя: +20%", "Мастерство защиты: +85%",
                "Мастерство владения оружием: 0%", "Меткость: 0%",
                "Оберег удачи: +160%", "Оберег ответа: +90%",
                "Оберег крита: +200%", "Оберег уворота: +90%",
                "Оберег удачи противника: 0%",
                "Оберег ответа противника: -150%",
                "Оберег крита противника: 0%",
                "Оберег уворота противника: -150%"};
        int[] expected = {30, 28, 41, 0, 13, 64, 4, 17, 0, 0, 32, 18, 40, 18,
                0, 30, 0, 30};

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            // System.out.println(line + "\t" + i + " "
//                    + Property.getScoreFrom(line) + " " + expected[i]);
            assertEquals(Property.getScoreFrom(line), expected[i]);
        }
    }

    @Test
    public void getScoreDamageTest() {
        String[] lines = {"Урон 4-20", "Урон 1-3", "Урон 96-104"};
        int[] expected = {6, 1, 50};

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            // System.out.println(i + " " + Property.getScoreFrom(line) + " "
//                    + expected[i]);
            assertEquals(Property.getScoreFrom(line), expected[i]);
        }
    }

    @Test
    public void getIntegerFromProperties() {
        int[] expected = {2, 2, 2, 2, 2, -999, -999, 7, 7, 7, 7, 7, 7, -1, -1,
                -1, 4, 4, 4, 4, 14, 14, 14, 14, 2, 2, 2, 2, -999, -999, -999,
                -999, 6, -999, -999, -999, -999, -999, -999, -999, -999, -999};
        for (int i = 0; i < MOST_PROPERTIES.length; i++) {
            String property = MOST_PROPERTIES[i];
            int actual = Property.getScoreFrom(property);
            // System.out.println(i + "\t" + property + "\t" + actual);
            assertEquals(actual, expected[i]);
        }
    }

    @Test
    public void formatPropertyByInteger() {
        for (String prop : MOST_PROPERTIES) {
            Property property = Property.getPropertyFrom(prop);
            int score = Property.getInteger(property, prop);
            String formatted = Property.formatPropertyByValue(property, score);
            if (Property.STATS.contains(property)
                    || Property.GUARDS_AND_MASTERIES.contains(property)
                    || Property.ARMOR.contains(property))
                assertEquals(prop, formatted);
        }
    }
}
