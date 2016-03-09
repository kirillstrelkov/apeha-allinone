package apeha.allinone.market;

import apeha.allinone.item.*;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MarketTest {
    @Test
    public void loadItemsTest() throws FileNotFoundException {
        String file = MarketTest.class.getClassLoader().getResource("market/allItemsInMarket").getFile();
        List<Item> items = TextParser.getItems(new FileReader(file));
        assertEquals(84, items.size());
    }

    @Test
    public void priceCompareTest() {
        String orig = "Цена: 900.00 ст. + 0.00";
        String text = "Цена: 900.00 ст. + 0.00";
        assertEquals(0,
                MarketPrice.format(orig).compareTo(MarketPrice.format(text)));
        text = "Цена: 900.00 ст.";
        assertEquals(0,
                MarketPrice.format(orig).compareTo(MarketPrice.format(text)));
        text = "Цена: 900.00 ст. + 5.40";
        assertEquals(-1,
                MarketPrice.format(orig).compareTo(MarketPrice.format(text)));
        text = "Цена: 900.00 ст. + 0.40";
        assertEquals(-1,
                MarketPrice.format(orig).compareTo(MarketPrice.format(text)));
        text = "Цена: 950.00 ст. + 0.00";
        assertEquals(-1,
                MarketPrice.format(orig).compareTo(MarketPrice.format(text)));
        text = "Цена: 800.00 ст. + 0.00";
        assertEquals(1,
                MarketPrice.format(orig).compareTo(MarketPrice.format(text)));
        text = "Цена: 800.00 ст. + 50.00";
        assertEquals(-1,
                MarketPrice.format(orig).compareTo(MarketPrice.format(text)));

        orig = "Цена: 105.00 ст. + 5.00";
        text = "Цена: 100.00 ст. + 6.00";
        assertEquals(0,
                MarketPrice.format(orig).compareTo(MarketPrice.format(text)));
    }

    @Test
    public void priceTest() {
        String text = "Цена: 900.00 ст. + 0.00";
        MarketPrice price = MarketPrice.format(text);
        assertEquals("900.00", price.getMainPrice());
        assertEquals("0.00", price.getBluePrice());
        assertEquals(
                text,
                Property.formatProperty(Property.PRICE_IN_MARKET,
                        price.toString()));
        text = "Цена: 0.85 ст. + 1.78";
        price = MarketPrice.format(text);
        assertEquals("0.85", price.getMainPrice());
        assertEquals("1.78", price.getBluePrice());
        assertEquals(
                text,
                Property.formatProperty(Property.PRICE_IN_MARKET,
                        price.toString()));
        text = "Цена: 0.85 ст.";
        price = MarketPrice.format(text);
        assertEquals("0.85", price.getMainPrice());
        assertEquals(null, price.getBluePrice());
        assertEquals(
                text,
                Property.formatProperty(Property.PRICE_IN_MARKET,
                        price.toString()));
        text = "Цена: 0.85";
        price = MarketPrice.format(text);
        assertEquals("0.85", price.getMainPrice());
        assertEquals(null, price.getBluePrice());
        assertEquals(
                "Цена: 0.85 ст.",
                Property.formatProperty(Property.PRICE_IN_MARKET,
                        price.toString()));
        text = "Цена: 0.85 ст. + 1.78 ст.";
        price = MarketPrice.format(text);
        assertEquals("0.85", price.getMainPrice());
        assertEquals("1.78", price.getBluePrice());
        assertEquals(
                "Цена: 0.85 ст. + 1.78",
                Property.formatProperty(Property.PRICE_IN_MARKET,
                        price.toString()));
        text = "Цена:0.85ст.+1.78ст.";
        price = MarketPrice.format(text);
        assertEquals("0.85", price.getMainPrice());
        assertEquals("1.78", price.getBluePrice());
        assertEquals(
                "Цена: 0.85 ст. + 1.78",
                Property.formatProperty(Property.PRICE_IN_MARKET,
                        price.toString()));
        text = "Цена:  0.85  ст.  +  1.78  ст.  ";
        price = MarketPrice.format(text);
        assertEquals("0.85", price.getMainPrice());
        assertEquals("1.78", price.getBluePrice());
        assertEquals(
                "Цена: 0.85 ст. + 1.78",
                Property.formatProperty(Property.PRICE_IN_MARKET,
                        price.toString()));
    }

    @Test
    public void marketSearchItemCreationTest() throws Exception {
        String file = MarketTest.class.getClassLoader().getResource("market/foundItems").getFile();
        List<Item> items = TextParser.getItems(new FileReader(file));
        System.out.println(items);
        assertEquals(20, items.size());
    }

    @Test
    public void marketPriceTest() {
        String text = "Кольцо Поклонения (мод.) \n"
                + "Лавка: [Hm] Swisstex 13 [i]\n" + "Цена: 103.90 + 2.50\n"
                + "Количество: 1\n" + "Требуемый Уровень: 6\n"
                + "Реакция: +12\n" + "Сложение: +8\n" + "Оберег удачи: +35%\n"
                + "Оберег крита: +35%\n" + "Оберег ответа противника: -35%";
        ModItem createModItem = ItemBuilder.createModItem(text);
        String string = createModItem.getPropertiesAndValues().get(
                Property.PRICE_IN_MARKET);
        String formatProperty = Property.formatProperty(
                Property.PRICE_IN_MARKET, string);
        assertEquals("103.90 ст. + 2.50", MarketPrice.format(formatProperty)
                .toString());
    }
}
