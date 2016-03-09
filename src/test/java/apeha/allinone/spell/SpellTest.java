package apeha.allinone.spell;

import apeha.allinone.common.Utils;
import apeha.allinone.item.Item;
import apeha.allinone.item.ItemBuilder;
import apeha.allinone.item.Property;
import apeha.allinone.item.TextParser;
import apeha.allinone.item.spell.SpellableItem;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class SpellTest {
    private static DateTime HARDCODED_CALENDAR;

    private final String expectedComment = "#Коммент\n#id 23423432432423";
    private final String fileSpellWithComments = SpellTest.class.getClassLoader().getResource("spell/spellWithComments.xml").getFile();

    private final String itemFromBlacksmithModAndSpell = "[Шлем Ненависти (мод.) (закл.)]\n"
            + "	Шлем Ненависти (мод.) (закл.)\n"
            + "Стоимость удаления мода: 20 ст. 	\n"
            + "Апатит ограненный\n"
            + "Время жизни: бессрочно\n"
            + "Апатит ограненный\n"
            + "Время жизни: 14д 21ч 43мин\n"
            + "Требуемый Уровень: 5\n"
            + "Реакция: +21\n"
            + "Сложение: +4\n"
            + "Оберег удачи: +20%\n"
            + "Оберег крита: +20%\n"
            + "Броня головы: +35\n"
            + "Цена в магазине: 45.00 ст.";
    private final String itemFromDb = "Меч Ненависти (мод.) (закл.)\n"
            + "прочность: 100/100\n" + "Цена в магазине: 105.00\n"
            + "Требуемый Уровень: 6\n" + "Сила: +11\n" + "Злость: +20\n"
            + "Урон 26-34\n" + "Наложено заклятие еще: 22д 12ч 8мин";
    private final String itemFromBlacksmithWithMod = "[Шлем Ненависти (мод.)]\n"
            + "	Шлем Ненависти (мод.)\n"
            + "Стоимость удаления мода: 20 ст. 	\n"
            + "Апатит ограненный\n"
            + "Время жизни: бессрочно\n"
            + "Требуемый Уровень: 5\n"
            + "Реакция: +21\n"
            + "Сложение: +4\n"
            + "Оберег удачи: +20%\n"
            + "Оберег крита: +20%\n"
            + "Броня головы: +35\n"
            + "Цена в магазине: 45.00 ст.";
    private final String itemFromBlacksmithWithSpell = "[Шлем Ненависти (закл.)]\n"
            + "	Шлем Ненависти (закл.)\n"
            + "Стоимость удаления мода: 20 ст. 	\n"
            + "Время жизни: 22д 12ч 8мин\n"
            + "Требуемый Уровень: 5\n"
            + "Реакция: +21\n"
            + "Сложение: +4\n"
            + "Оберег удачи: +20%\n"
            + "Оберег крита: +20%\n"
            + "Броня головы: +35\n"
            + "Цена в магазине: 45.00 ст.";

    @BeforeClass
    public static void setup() {
        HARDCODED_CALENDAR = Utils.formatDateFrom("21.08.12 17:24");
    }

    @Test
    public void spellItemCreateWarriorHouse() {
        Item item = ItemBuilder.createItem(itemFromDb);
        assertEquals(itemFromDb, item.toString());
    }

    @Test
    public void spellItemCreateFromBlacksmith() {
        String expected = "Шлем Ненависти (мод.) (закл.)\n"
                + "Цена в магазине: 45.00\n" + "Требуемый Уровень: 5\n"
                + "Реакция: +21\n" + "Сложение: +4\n" + "Оберег удачи: +20%\n"
                + "Оберег крита: +20%\n" + "Броня головы: +35\n"
                + "Наложено заклятие еще: 14д 21ч 43мин";
        Item item = ItemBuilder.createItem(itemFromBlacksmithModAndSpell);
        assertEquals(expected, item.toString());
    }

    @Test
    public void spellItemCreateFromBlacksmithModOnly() {
        String expected = "Шлем Ненависти (мод.)\n"
                + "Цена в магазине: 45.00\n" + "Требуемый Уровень: 5\n"
                + "Реакция: +21\n" + "Сложение: +4\n" + "Оберег удачи: +20%\n"
                + "Оберег крита: +20%\n" + "Броня головы: +35";
        Item item = ItemBuilder.createItem(itemFromBlacksmithWithMod);
        assertEquals(expected, item.toString());
    }

    @Test
    public void getDateFromLineTest() {
        String[] data = {"Наложено заклятие еще: 19д 16ч 19мин",
                "Наложено заклятие еще: 19д 0ч 0мин",
                "Наложено заклятие еще: 19д 16ч", "Наложено заклятие еще: 19д",
                "Наложено заклятие еще: 16ч 19мин",
                "Наложено заклятие еще: 19мин", "Время жизни: 14д 21ч 43мин",
                "Время жизни: бессрочно",};

        for (String text : data) {
            DateTime actual = Utils.getCalendarFrom(text);

            int[] dhm = this.getDaysHoursMins(text);
            int d = dhm[0];
            int h = dhm[1];
            int m = dhm[2];

            DateTime expected = DateTime.now().plusMinutes(
                    d * 24 * 60 + h * 60 + m);

            // System.out.println(text);
            // System.out.println(Arrays.toString(dhm));
            // System.out.println(actual);
            // System.out.println(expected);
            if (text.contains("бессрочно")) {
                assertNull(actual);
            } else {
                long diff = Math.abs(actual.getMillis() - expected.getMillis());
                // System.out.println(diff);
                assertTrue(diff <= 1 || Math.abs(diff - 60 * 60 * 1000) <= 1);
            }
        }
    }

    @Test
    public void createSpellableItemUsingItemBuilder() {
        SpellableItem item = ItemBuilder
                .createSpellableItem(itemFromBlacksmithModAndSpell);
        assertNotNull(item.getDate());
    }

    @Test
    public void xmlRead() throws IOException {
        String file = SpellTest.class.getClassLoader().getResource("spell/spell.xml").getFile();
        XMLIO xmlio = new XMLIO();
        List<SpellableItem> items = xmlio.getItemsFromXML(new FileReader(file));
        assertEquals(21, items.size());

        List<String> dates = getDates(file);
        assertEquals(items.size(), dates.size());
        for (SpellableItem item : items) {
            int index = items.indexOf(item);

            assertNotNull(item.getDate());
            assertNull(item.getComment());
            assertEquals(dates.get(index),
                    Utils.dateFormat.print(item.getDate()));
        }
    }

    private List<String> getDates(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        List<String> dates = Lists.newArrayList();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(XMLIO.XMLTags.date)) {
                dates.add(line.substring(line.indexOf(">") + 1,
                        line.lastIndexOf("<")));
            }
        }
        reader.close();

        return dates;
    }

    @Test
    public void xmlWrite() throws IOException {
        String file = SpellTest.class.getClassLoader().getResource("spell/spell.xml").getFile();
        XMLIO xmlio = new XMLIO();
        List<SpellableItem> read = xmlio.getItemsFromXML(new FileReader(file));
        // System.out.println(xmlio.getXMLfromListAsString(items));
        String saveFile = "/tmp/spell.xml";
        xmlio.saveItemsTo(read, new FileWriter(new File(saveFile)));
        List<SpellableItem> wrote = xmlio.getItemsFromXML(new FileReader(
                saveFile));
        // System.out.println(itemsRead.toString());
        assertEquals(read.toString(), wrote.toString());
        List<String> dates = getDates(file);
        for (int i = 0; i < read.size(); i++) {

            assertNotNull(wrote.get(i).getDate());
            assertNull(wrote.get(i).getComment());
            assertEquals(dates.get(i),
                    Utils.dateFormat.print(wrote.get(i).getDate()));

            assertNotNull(read.get(i).getDate());
            assertNull(read.get(i).getComment());
            assertEquals(dates.get(i),
                    Utils.dateFormat.print(read.get(i).getDate()));

            assertEquals(read.get(i), wrote.get(i));
        }
    }

    // TODO try to add from db and from blacksmith items with Mod only and with
    // Spell only from file!!

    @Test
    public void isAliveTest() {
        SpellableItem item = ItemBuilder.createSpellableItem(itemFromDb);
        assertTrue(item.isAlive());
        item.setDate(HARDCODED_CALENDAR);
        assertFalse(item.isAlive());
    }

    @Test
    public void isSpellableTest() throws ParseException {
        SpellableItem item = ItemBuilder.createSpellableItem(itemFromDb);
        assertFalse(item.isSpellable());

        DateTime instance = DateTime.now().plusDays(4);
        item.setDate(instance);

        assertTrue(item.isSpellable());
    }

    @Test
    public void lessThen24HTest() {
        SpellableItem item = ItemBuilder.createSpellableItem(itemFromDb);
        assertFalse(item.isLessThen24hAlive());

        DateTime instance = DateTime.now().plusHours(16);
        item.setDate(instance);

        assertTrue(item.isLessThen24hAlive());
    }

    @Test
    public void getSpellStartDateTest() {
        SpellableItem item = ItemBuilder.createSpellableItem(itemFromDb);
        item.setDate(DateTime.now());

        DateTime instance = DateTime.now().minusDays(5);

        assertEquals(instance, item.getSpellStartCalendar());
    }

    @Test
    public void createItemWithModOnly() {
        SpellableItem item = ItemBuilder
                .createSpellableItem(itemFromBlacksmithWithMod);
        assertNull(item);
    }

    @Test
    public void createItemWithSpellOnly() {
        SpellableItem item = ItemBuilder
                .createSpellableItem(itemFromBlacksmithWithSpell);
        assertNotNull(item);
        assertNotNull(item.getDate());
    }

    @Test
    public void spellTest() {
        SpellableItem item = ItemBuilder.createSpellableItem(itemFromBlacksmithModAndSpell);
        int[] spelltimes = {30, 45, 60, 75, 90};
        DateTime instance;
        for (int time : spelltimes) {
            instance = DateTime.now();
            item.spell(time);

            int diffDays = Days.daysBetween(instance, item.getDate()).getDays();
            assertEquals(time, diffDays);

            int diffMinutes = Minutes.minutesBetween(instance, item.getDate())
                    .getMinutes();
            // assertEquals(time * 24 * 60, diffMinutes);
            assertEquals((double) time * 24 * 60, (double) diffMinutes, 60.00);

//            System.out.println(item.getTimeLeft());
            String timeLeft = item.getTimeLeft();
            String timeLeftOffset0 = getTimeLeft(instance, time, 0);
            String timeLeftOffset1 = getTimeLeft(instance, time, 1);
            boolean isCorrect = timeLeftOffset0.equals(timeLeft) ||
                    timeLeftOffset1.equals(timeLeft);
//            if (!isCorrect) {
//                System.out.println(item.getTimeLeft());
//            }
//          NOTE: Could fail because of time shift
            assertTrue(
                    String.format("Date is not correct, possible error - delta is more than 2 sec\n" +
                                    "Item time left: %s, offset 0: %s, offset1: %s",
                            timeLeft, timeLeftOffset0, timeLeftOffset1),
                    isCorrect
            );
        }
    }

    private String getTimeLeft(DateTime date, int time, int deltaSec) {
        long ms = date.getMillis();
        DateTime dateAfter = date.plusDays(time);
        ms = dateAfter.getMillis() - ms;
        ms = ms - deltaSec * 1000;
        int days = (int) (ms / (1000 * 60 * 60 * 24));
        int hours = (int) ((ms / (1000 * 60 * 60)) % 24);
        int minutes = (int) ((ms / (1000 * 60)) % 60);
        return String.format("%dд %dч %dмин", days, hours, minutes);
    }

    private int[] getDaysHoursMins(String text) {
        int days;
        int hours;
        int mins;
        Pattern pNumber = Pattern.compile("[0-9]+");
        Pattern pattern = Pattern.compile("[0-9]+\\д");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            matcher = pNumber.matcher(matcher.group());
            if (matcher.find()) {
                days = Integer.parseInt(matcher.group());
            } else {
                days = 0;
            }
        } else {
            days = 0;
        }
        pattern = Pattern.compile("[0-9]+\\ч");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            matcher = pNumber.matcher(matcher.group());
            if (matcher.find()) {
                hours = Integer.parseInt(matcher.group());
            } else {
                hours = 0;
            }
        } else {
            hours = 0;
        }
        pattern = Pattern.compile("[0-9]+\\мин");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            matcher = pNumber.matcher(matcher.group());
            if (matcher.find()) {
                mins = Integer.parseInt(matcher.group());
            } else {
                mins = 0;
            }
        } else {
            mins = 0;
        }

        return new int[]{days, hours, mins};
    }

    @Test
    public void sharpenItemTest() {
        String text = "Кольцо Ненависти (мод.) (закл.) (реак. +1)\n"
                + "прочность: 100/100\n" + "Требуемый Уровень: 5\n"
                + "Реакция: +29\n" + "Сложение: +6\n" + "Оберег удачи: +30%\n"
                + "Оберег крита: +30%\n" + "Цена в магазине: 45.00 ст.\n"
                + "количество: 1\n" + "Наложено заклятие еще: 2д 14ч 50мин\n";
        SpellableItem item = ItemBuilder.createSpellableItem(text);
        assertNotNull(item);
        assertEquals("Кольцо Ненависти (мод.) (закл.) (реак. +1)",
                item.getName());
    }

    @Test
    public void commentedItemTest() {
        String text = "Кольцо Ненависти (мод.) (закл.) (реак. +1)\n"
                + "прочность: 100/100\n" + "Требуемый Уровень: 5\n"
                + "Реакция: +29\n" + "Сложение: +6\n" + "Оберег удачи: +30%\n"
                + "Оберег крита: +30%\n" + "Цена в магазине: 45.00 ст.\n"
                + "количество: 1\n" + "Наложено заклятие еще: 2д 14ч 50мин\n"
                + "Какой то комментарий";
        SpellableItem item = ItemBuilder.createSpellableItem(text);
        assertNotNull(item);
        assertEquals("Какой то комментарий", item.getName());
        System.out.println(item.toString());
    }

    @Test
    public void fromDb() throws FileNotFoundException {
        String file = SpellTest.class.getClassLoader().getResource("spell/fromDb").getFile();
        List<Item> items = TextParser.getItems(new FileReader(file));
        for (Item item : items)
            assertTrue(item.getPropertiesAndValues().containsKey(
                    Property.SPELLED));
    }

    @Test
    public void inputItem() throws FileNotFoundException {
        String file = SpellTest.class.getClassLoader().getResource("spell/inputItem").getFile();
        List<Item> items = TextParser.getItems(new FileReader(file));
        for (Item item : items)
            assertTrue(item.getPropertiesAndValues().containsKey(
                    Property.SPELLED));
    }

    @Test
    public void latinNames() throws FileNotFoundException {
        String file = SpellTest.class.getClassLoader().getResource("spell/latinNames").getFile();
        List<Item> items = TextParser.getItems(new FileReader(file));
        for (Item item : items) {
            assertTrue(item.getName().contains("moij kompl"));
            assertTrue(item.getPropertiesAndValues().containsKey(
                    Property.SPELLED));
        }
    }

    @Test
    public void multipleItem() throws FileNotFoundException {
        String file = SpellTest.class.getClassLoader().getResource("spell/multipleItem").getFile();
        List<Item> items = TextParser.getItems(new FileReader(file));
        for (Item item : items) {
            if (item.getName().contains("Кольцо Молнии"))
                assertFalse(item.getPropertiesAndValues().containsKey(
                        Property.SPELLED));
            else
                assertTrue(item.getPropertiesAndValues().containsKey(
                        Property.SPELLED));
        }
    }

    @Test
    public void newMultipleItem() throws FileNotFoundException {
        String file = SpellTest.class.getClassLoader().getResource("spell/newMultiItems").getFile();
        List<Item> items = TextParser.getItems(new FileReader(file));
        for (Item item : items) {
            System.out.println(item.getName());
            if (item.getName().contains("Меч Стихий"))
                assertFalse(item.getPropertiesAndValues().containsKey(
                        Property.SPELLED));
            else
                assertTrue(item.getPropertiesAndValues().containsKey(
                        Property.SPELLED));
        }
    }

    @Test
    public void parseXMLwithComments() throws IOException {
        XMLIO xmlio = new XMLIO();
        List<SpellableItem> items = xmlio.getItemsFromXML(new FileReader(
                fileSpellWithComments));

        List<String> dates = getDates(fileSpellWithComments);
        for (SpellableItem item : items) {
            String actualComment = item.getComment();

            // System.out.println(actualComment);
            assertNotNull(actualComment);
            assertEquals(expectedComment, actualComment);

            assertNotNull(item.getDate());
            assertEquals(dates.get(items.indexOf(item)),
                    Utils.dateFormat.print(item.getDate()));
        }
    }

    @Test
    public void writeXMLwithComments() throws IOException {
        XMLIO xmlio = new XMLIO();
        List<SpellableItem> items = xmlio.getItemsFromXML(new FileReader(
                fileSpellWithComments));
        // System.out.println(xmlio.getXMLfromListAsString(items));

        String saveFile = "/tmp/spellWithComments.xml";
        xmlio.saveItemsTo(items, new FileWriter(new File(saveFile)));
        List<SpellableItem> itemsRead = xmlio.getItemsFromXML(new FileReader(
                saveFile));
        // System.out.println(itemsRead.toString());

        List<String> dates = getDates(fileSpellWithComments);
        for (int i = 0; i < items.size(); i++) {
            SpellableItem read = itemsRead.get(i);
            SpellableItem wrote = items.get(i);

            assertEquals(wrote, read);
            assertEquals(wrote.getDate(), read.getDate());
            assertEquals(wrote.getComment(), read.getComment());

            assertNotNull(wrote.getDate());
            assertNotNull(wrote.getComment());
            assertEquals(dates.get(i), Utils.dateFormat.print(wrote.getDate()));

            assertNotNull(read.getDate());
            assertNotNull(read.getComment());
            assertEquals(dates.get(i), Utils.dateFormat.print(read.getDate()));
        }
        assertEquals(items.toString(), itemsRead.toString());
    }

    @Test
    public void readSpellHardcoded() throws FileNotFoundException {
        XMLIO xmlio = new XMLIO();
        List<SpellableItem> items = xmlio.getItemsFromXML(new FileReader(SpellTest.class.getClassLoader().getResource("spell/spellHardcoded.xml").getFile()));

        SpellableItem item1 = items.get(0);
        assertEquals("23.09.12 22:09", Utils.dateFormat.print(item1.getDate()));

        SpellableItem item2 = items.get(1);
        assertEquals("07.01.14 18:13", Utils.dateFormat.print(item2.getDate()));
    }

    @Test
    public void getTimeLeftTest() {
        SpellableItem item = ItemBuilder
                .createSpellableItem(itemFromBlacksmithModAndSpell);
        assertTrue(item.getTimeLeft().matches("14д 2[01]ч 4[32]мин"));

        item = ItemBuilder.createSpellableItem(itemFromBlacksmithWithSpell);
        assertTrue(item.getTimeLeft().matches("22д 1[12]ч [78]мин"));

        item = ItemBuilder.createSpellableItem(itemFromDb);
        assertTrue(item.getTimeLeft().matches("22д 1[12]ч [78]мин"));
    }
}