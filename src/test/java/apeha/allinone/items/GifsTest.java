package apeha.allinone.items;

import apeha.allinone.item.Item;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class GifsTest {
    private TextFilesHandler handler = TextFilesHandler.getInstance();

    @Test
    public void downloadImageCheck() {
        handler.downloadAllImages(handler.getImageNameAndURL());
        File dir = handler.getImageDir();
        File[] listFiles = dir.listFiles();
        for (File f : listFiles) {
            assertTrue(f.length() > 1);
        }
    }

    @Test
    public void checkGifCount() {
        assertEquals(ItemsTesting.SIZE_OF_ALL_ITEMS, handler.getImageDir().list().length);
    }

    @Test
    public void itemImageSrcIsSet() {
        List<Item> allItems = handler.getAllItemWithImageSrc();
        Iterator<Item> iterator = allItems.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            String name = item.getName();
            String imageSrc = item.getImageSrc();
            assertNotNull("Image src is null:" + name, imageSrc);
            assertTrue("Image does not exist in filesystem",
                    new File(imageSrc).exists());
            System.out.println(item.getImageSrc());
        }
    }

    @Test
    public void print() {
        List<Item> allItems = handler.getAllItemWithImageSrc();
        Iterator<Item> iterator = allItems.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            String name = item.getName();
            System.out.println("\n" + name + "\n" + item);
        }
    }
}
