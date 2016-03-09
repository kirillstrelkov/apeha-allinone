package apeha.allinone.db;

import apeha.allinone.common.Category;
import apeha.allinone.db.xml.XMLHandlerRW;
import apeha.allinone.item.Item;
import apeha.allinone.items.ItemsTesting;
import apeha.allinone.items.TextFilesHandler;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTest {
    private static final String APEHADB_XML = XMLHandler.class.getClassLoader().getResource(
            "apehadb.xml").getPath();
    private static final XMLHandlerRW XML_HANDLER_RW = new XMLHandlerRW(
            APEHADB_XML);
    private static final XMLHandler XML_HANDLER = new XMLHandler(APEHADB_XML);
    private static final TextFilesHandler TEXT_HANDLER = TextFilesHandler.getInstance();
    private static final List<String> ITEM_NAMES;
    private static final List<String> LOWERCASE_ITEM_NAMES;

    static {
        ITEM_NAMES = new LinkedList<String>();
        LOWERCASE_ITEM_NAMES = new LinkedList<String>();
        for (Item item : XML_HANDLER.getItems()) {
            ITEM_NAMES.add(item.getName());
            LOWERCASE_ITEM_NAMES.add(item.getName().toLowerCase());
        }
    }

    @Test
    public void singleItemInXMLTest() {
        String[] items = {"Золотой топор", "Кастет \"череп\"",
                "Кольцо Баланса", "Кольцо Движения", "Кольцо Злости",
                "Кольцо Жизни", "Кольцо Тора", "Копьё боевое", "Лук боевой",
                "Меч рыбьего страха", "Меч ювелирный",
                "Небесный перстень жизни", "Несущий Смерть",
                "Перстень Чёрного рыцаря", "Пика двуручная", "Посох шамана",
                "Рубашка защитная", "Топор дровосека", "Лук ивовый",
                "Небесный щит", "Огненная броня", "Панцирь Чёрного рыцаря",
                "Перчатки Невозмутимости", "Изумрудный Щит"};
        for (String name : items) {
            assertTrue(ITEM_NAMES.contains(name));
        }
    }

    @Test
    public void defaultXmlFileOpen() {
        new XMLHandler().getCategoryAndItems();
    }

    @Test
    public void xmlToFile() throws Exception {
        Files.copy(
                FileSystems.getDefault().getPath(XML_HANDLER_RW.dbStream),
                FileSystems.getDefault().getPath(
                        "/tmp/" + new Date().getTime() + "apehaold.db"));

        List<Item> itemsBefore = TEXT_HANDLER.getAllItems();
        XML_HANDLER_RW.saveTo();
        List<Item> itemsAfter = XML_HANDLER_RW.getItems();
        assertEquals(itemsBefore.size(), itemsAfter.size());
        Collections.sort(itemsBefore);
        Collections.sort(itemsAfter);
        assertEquals(itemsBefore.toString(), itemsAfter.toString());
    }

    @Test
    public void sizeCategories() {
        Set<Category> keySet = XML_HANDLER.getCategoryAndItems().keySet();
        assertEquals("Categories are incorrent", 11, keySet.size());
    }

    @Test
    public void getItemsFromXMl() {
        ItemHandler dbIterface = XML_HANDLER;
        assertEquals(ItemsTesting.SIZE_OF_ALL_ITEMS, dbIterface.getItems()
                .size());
    }

    @Test
    public void compareTextItemsAndXMLItems() {
        Set<String> setOfNamesKovcheg = TEXT_HANDLER.getSetOfNamesKovcheg();
        ItemHandler handler = XML_HANDLER_RW;
        Set<String> setOfNamesXml = TEXT_HANDLER.getSetOfNamesFrom(handler
                .getItems());
        assertEquals(setOfNamesKovcheg.toString(), setOfNamesXml.toString());
    }

    @Test
    public void uniqueNamesInXML() {
        int size1 = LOWERCASE_ITEM_NAMES.size();
        int size2 = new TreeSet<String>(LOWERCASE_ITEM_NAMES).size();
        assertEquals(size1, size2);
    }

    // uses lower case comparison
    @Test
    public void itemNameFromBOareInXML() {
        Set<String> names = TEXT_HANDLER.getSetOfNamesBO();
        for (String name : names) {
            // fixing incorrect names
            if (name.equals("Кастет череп"))
                name = "Кастет \"череп\"";
            else if (name.equals("Кольцо Приведения"))
                name = "Кольцо Привидения";
            else if (name.equals("Молот раскаленный"))
                name = "Молот раскалённый";
            else if (name.equals("Сабля ледянная"))
                name = "Сабля Ледяная";

            assertTrue(
                    String.format("Name '%s' wasn't found in database", name),
                    LOWERCASE_ITEM_NAMES.contains(name.toLowerCase()));
        }
    }

    @Test
    public void itemsFromMaltAreInXML() {
        Set<String> names = TEXT_HANDLER.getSetOfNamesMaltNew();
        for (String name : names) {
            assertTrue(
                    String.format("Name '%s' wasn't found in database", name),
                    ITEM_NAMES.contains(name));
        }
    }

    @Test
    public void itemsFromStalkerzAreInXML() {
        Set<String> names = TEXT_HANDLER.getSetOfNamesStalkerz();
        for (String name : names) {
            // skipping items
            if (name.equals("Броня Древних чужая")
                    || name.equals("Броня Силы Древних чужая")
                    || name.equals("Молот Древних чужой")
                    || name.equals("Шлем Древних чужой")
                    || name.equals("Шлем силы древних чужой")
                    || name.equals("Щит Древних чужой")
                    || name.equals("Щит Силы Древних чужой")
                    || name.equals("Меч Силы Древних чужой"))
                continue;
            assertTrue(
                    String.format("Name '%s' wasn't found in database", name),
                    ITEM_NAMES.contains(name));
        }
    }
}
