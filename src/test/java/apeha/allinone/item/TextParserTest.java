package apeha.allinone.item;

import apeha.allinone.db.DbHandler;
import apeha.allinone.market.MarketTest;
import apeha.allinone.spell.SpellTest;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TextParserTest {
    private File fileMarket = new File(MarketTest.class.getClassLoader().getResource("market/allItemsInMarket").getFile());
    private File fileDb = new File(SpellTest.class.getClassLoader().getResource("spell/fromDb").getFile());
    private File fileLatinNames = new File(SpellTest.class.getClassLoader().getResource("spell/latinNames").getFile());
    private File fileMultiItem = new File(SpellTest.class.getClassLoader().getResource("spell/multipleItem").getFile());
    private List<Item> origItems = DbHandler.getItems();

    @Test
    public void nickNameTest() throws Exception {
        File file4 = new File(TextParserTest.class.getClassLoader().getResource("item/names").getFile());
        LineNumberReader reader = new LineNumberReader(new FileReader(file4));
        String line;
        while ((line = reader.readLine()) != null) {
            // System.out.println(line.trim()+"\t"+TextParser.getName(line));
            assertEquals(line.trim(), TextParser.getName(line));
        }
        // TODO add to names files all clan members
        reader.close();
    }

    @Test
    public void itemNameTest() {
        String name = "Меч Ненависти (мод.) (закл.)";
        assertEquals(name, TextParser.getName(name));
        name = "Амулет Невиновности (мод.)";
        assertEquals(name, TextParser.getName(name));
        name = "Амулет Невиновности (закл.)";
        assertEquals(name, TextParser.getName(name));
        name = "Амулет Невиновности (мод.) (закл.)";
        assertEquals(name, TextParser.getName(name));
        name = "Амулет Невиновности";
        assertEquals(name, TextParser.getName(name));
        name = "tehno";
        assertEquals(name, TextParser.getName(name));
        name = "666dub666";
        assertEquals(name, TextParser.getName(name));
        name = "tehno (мод.)";
        assertEquals(name, TextParser.getName(name));
        name = "tehno (закл.)";
        assertEquals(name, TextParser.getName(name));
        name = "tehno (мод.) (закл.)";
        assertEquals(name, TextParser.getName(name));
        name = "[Перчатки Безразличия (мод.)]";
        assertEquals(null, TextParser.getName(name));
        name = "Часть именного комлекта moij kompl (мод.) (закл.)";
        assertEquals(name, TextParser.getName(name));
    }

    @Test
    public void sharpenItemsTest() {
        String name = "Avicenna (закл.) (заточ. +4)";
        assertEquals(name, TextParser.getName(name));
        name = "Avicenna (заточ. +4)";
        assertEquals(name, TextParser.getName(name));
        name = "Амулет Невиновности (заточ. +4)";
        assertEquals(name, TextParser.getName(name));
        name = "Кольцо Ненависти (мод.) (закл.) (реак. +1)";
        assertEquals(name, TextParser.getName(name));
        name = "Меч Стихий (мод.) (заточ. +1)";
        assertEquals(name, TextParser.getName(name));
        name = "Кольцо Ненависти (мод.) (закл.) (бронь +1)";
        assertEquals(name, TextParser.getName(name));
        name = "Кольцо Ненависти (мод.) (закл.) (сила +1)";
        assertEquals(name, TextParser.getName(name));
        name = "Кольцо Ненависти (мод.) (закл.) (ловк. +1)";
        assertEquals(name, TextParser.getName(name));
        name = "Кольцо Ненависти (мод.) (закл.) (реак. +1)";
        assertEquals(name, TextParser.getName(name));
        name = "Кольцо Ненависти (мод.) (закл.) (удача +1)";
        assertEquals(name, TextParser.getName(name));
        name = "Кольцо Ненависти (мод.) (закл.) (злость +1)";
        assertEquals(name, TextParser.getName(name));
        name = "Кольцо Ненависти (мод.) (закл.) (слож. +1)";
        assertEquals(name, TextParser.getName(name));
        name = "Кольцо Ненависти (мод.) (слож. +1)";
        assertEquals(name, TextParser.getName(name));
        name = "Кольцо Ненависти (закл.) (слож. +1)";
        assertEquals(name, TextParser.getName(name));
        name = "Кольцо Ненависти (слож. +1)";
        assertEquals(name, TextParser.getName(name));
    }

    @Test
    public void getListOfItemStringFromText() throws Exception {
        List<String> list = TextParser.getListOfItemStrings(new FileReader(
                fileMarket));
        assertEquals(84, list.size());
        list = TextParser.getListOfItemStrings(new FileReader(fileDb));
        assertEquals(6, list.size());
        list = TextParser.getListOfItemStrings(new FileReader(fileLatinNames));
        assertEquals(2, list.size());
        list = TextParser.getListOfItemStrings(new FileReader(fileMultiItem));
        assertEquals(33, list.size());
        // TODO get from kunzica when some items are damadge
    }

    @Test
    public void getItemsFromFiles() throws Exception {
        List<Item> items = TextParser.getItems(new FileReader(fileMarket));
        assertEquals(84, items.size());
        compareItemNames(items);
        items = TextParser.getItems(new FileReader(fileDb));
        assertEquals(6, items.size());
        compareItemNames(items);
        items = TextParser.getItems(new FileReader(fileLatinNames));
        assertEquals(2, items.size());
        // compareItemNames(items);
        items = TextParser.getItems(new FileReader(fileMultiItem));
        assertEquals(33, items.size());
        compareItemNames(items);
    }

    private void compareItemNames(List<Item> list) {
        for (int i = 0; i < list.size(); i++) {
            boolean isCorrect = false;
            // String dbName = "";
            Item item = list.get(i);
            for (int j = 0; j < origItems.size(); j++) {
                Item dbItem = origItems.get(j);
                if (item.getName().contains(dbItem.getName())) {
                    isCorrect = true;
                    // dbName = dbItem.getName();
                    break;
                }
            }
            // if (isCorrect)
            // System.out.println(String.format("found in DB: %s, db name: %s",
            // item.getName(), dbName));
            assertTrue("Not found: " + item.getName(), isCorrect);
        }
    }
}
