package apeha.allinone.db;

import apeha.allinone.common.Category;
import apeha.allinone.common.Utils;
import apeha.allinone.item.Item;
import apeha.allinone.items.TextFilesHandler;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class DbTest {

    @Test
    public void sortedItemsFromDb() {
        List<Item> items = DbHandler.getItems();
        Collections.sort(items);
        assertEquals("Алебарда сторожевая", items.get(0).getName());
        assertEquals("Амулет Архангела", items.get(1).getName());
        assertEquals("Амулет Безразличия", items.get(2).getName());
    }

    @Test
    public void sortedCategoriesAndFirstItemFromDb() {
        Map<Category, List<Item>> categoryAndItems = DbHandler.getHandler().getCategoryAndItems();
        Map<Category, List<Item>> sorted = Utils.getSortedCategoryAndItem(categoryAndItems);
        Set<Category> categories = sorted.keySet();

        Map<Category, String> expectedCatAndFirstItem = Maps.newLinkedHashMap();
        expectedCatAndFirstItem.put(Category.AMULETS, "Амулет Архангела");
        expectedCatAndFirstItem.put(Category.RINGS, "Глаз дракона");
        expectedCatAndFirstItem.put(Category.ARMOURS, "Бригантина");
        expectedCatAndFirstItem.put(Category.VAMBRACES, "Наручи Безразличия");
        expectedCatAndFirstItem.put(Category.ARMS, "Алебарда сторожевая");
        expectedCatAndFirstItem.put(Category.GLOVES, "Золотые перчатки");
        expectedCatAndFirstItem.put(Category.BOOTS, "Золотые сапоги");
        expectedCatAndFirstItem.put(Category.BELTS, "Золотой пояс");
        expectedCatAndFirstItem.put(Category.SHOOTING_ARMS, "Арбалет лёгкий");
        expectedCatAndFirstItem.put(Category.HELMETS, "Золотой шлем");
        expectedCatAndFirstItem.put(Category.SHIELDS, "Башенный щит");

        assertArrayEquals(
                expectedCatAndFirstItem.keySet().toArray(new Category[expectedCatAndFirstItem.size()]),
                categories.toArray(new Category[sorted.size()])
        );

        for (Category cat : sorted.keySet()) {
            List<Item> items = sorted.get(cat);
            assertEquals(expectedCatAndFirstItem.get(cat), items.get(0).getName());
        }
    }

    @Test
    public void imageToString() throws IOException {
        TextFilesHandler.getInstance().downloadAllImages();
        File fileImage = new File("/tmp/img/Сякены.gif");
        byte[] bread = new byte[(int) fileImage.length()];
        new FileInputStream(fileImage).read(bread);
        String base64String = Base64.encodeBase64URLSafeString(bread);
        byte[] decbytes = Base64.decodeBase64(base64String);
        // System.out.println("encoded bytes: " + Arrays.toString(bread));
        // System.out.println(base64String.length());
        // System.out.println("decoded string: " + base64String);
        // System.out.println("decoded bytes: " + Arrays.toString(decbytes));
        assertTrue(Arrays.equals(bread, decbytes));
    }

    @Test
    public void imagesContainCorrectNumberOfFrame() {
        Map<String, Integer> itemsWithMultipleFrames = new HashMap<>();
        itemsWithMultipleFrames.put("Амулет невидимки", 5);
        itemsWithMultipleFrames.put("Амулет невидимки", 5);
        itemsWithMultipleFrames.put("Амулет странника", 3);
        itemsWithMultipleFrames.put("Белый крест", 4);
        itemsWithMultipleFrames.put("Кастет Удивления", 2);
        itemsWithMultipleFrames.put("Кольцо Демона", 9);
        itemsWithMultipleFrames.put("Меч ведьмы", 5);
        itemsWithMultipleFrames.put("Меч волшебника", 4);
        itemsWithMultipleFrames.put("Молот Злости", 2);
        itemsWithMultipleFrames.put("Молот удачи", 4);
        itemsWithMultipleFrames.put("Пламя ярости", 2);
        itemsWithMultipleFrames.put("Стальные когти", 3);
        itemsWithMultipleFrames.put("Сюрикены", 19);
        itemsWithMultipleFrames.put("Сюрикены Ученика", 19);
        itemsWithMultipleFrames.put("Сякены", 12);
        itemsWithMultipleFrames.put("Сякены Бронзовые", 12);
        itemsWithMultipleFrames.put("Шлем демона", 2);
        itemsWithMultipleFrames.put("Щит демона", 2);
        itemsWithMultipleFrames.put("Щит солнца", 3);

        List<Item> items = DbHandler.getItems();
        String msg = "Incorrect number of frames for '%s'";
        ImageLoader loader = new ImageLoader();
        for (Item item : items) {
            byte[] decbytes = Base64.decodeBase64(item.getImageSrc());
            final ImageData[] imageData = loader.load(new ByteArrayInputStream(decbytes));
            String name = item.getName();
            String formattedMessage = String.format(msg, name);
            int frames = imageData.length;

            if (itemsWithMultipleFrames.keySet().contains(name)) {
                assertEquals(formattedMessage, (int) itemsWithMultipleFrames.get(name), frames);
            } else {
                assertEquals(formattedMessage, 1, frames);
            }
        }
    }
}
