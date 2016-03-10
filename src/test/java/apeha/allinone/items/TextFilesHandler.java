package apeha.allinone.items;

import apeha.allinone.TestHelper;
import apeha.allinone.common.Category;
import apeha.allinone.item.Item;
import apeha.allinone.item.ItemBuilder;
import apeha.allinone.item.Property;
import apeha.allinone.item.TextParser;
import apeha.allinone.search.ItemWithScore;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;

public class TextFilesHandler {
    private static TextFilesHandler textFilesHandler;
    private File fileGifs;
    private File fileGifsLess;
    private File helmets;
    private File amulets;
    private File armours;
    private File arms;
    private File belts;
    private File boots;
    private File naru4i;
    private File gloves;
    private File rings;
    private File shields;
    private File shootingArms;
    private File maltordenItems;
    private File itemsBO;
    private File itemsMalt;
    private File itemsStalkerz;

    private TextFilesHandler() {
        ClassLoader classLoader = TextFilesHandler.class.getClassLoader();
        String mainPath = "items";

        fileGifs = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, "gifs")).getFile());
        fileGifsLess = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, "gifsLessInfo")).getFile());
        helmets = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, Category.HELMETS.name())).getFile());
        amulets = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, Category.AMULETS.name())).getFile());
        armours = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, Category.ARMOURS.name())).getFile());
        arms = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, Category.ARMS.name())).getFile());
        belts = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, Category.BELTS.name())).getFile());
        boots = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, Category.BOOTS.name())).getFile());
        naru4i = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, Category.VAMBRACES.name())).getFile());
        gloves = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, Category.GLOVES.name())).getFile());
        rings = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, Category.RINGS.name())).getFile());
        shields = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, Category.SHIELDS.name())).getFile());
        shootingArms = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, Category.SHOOTING_ARMS.name())).getFile());
        maltordenItems = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, "maltOrdenAllStuff")).getFile());
        itemsBO = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, "itemsBO")).getFile());
        itemsMalt = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, "itemsMalt")).getFile());
        itemsStalkerz = new File(classLoader.getResource(TestHelper.joinPaths(mainPath, "itemsStalkerz")).getFile());

        rewriteFormatData();
    }

    public static TextFilesHandler getInstance() {
        if (textFilesHandler == null) {
            textFilesHandler = new TextFilesHandler();
        }
        return textFilesHandler;
    }

    public static void printList(List<ItemWithScore> list) {
        for (ItemWithScore item : list) {
            System.out.println(item.getName() + "\n" + item.toString());
        }
    }

    public static TreeMap<String, String> getMapNameAndPathToRENAMEDFiles() {
        TreeMap<String, String> mapFormated = new TreeMap<String, String>();
        Iterator<String> it = TextFilesHandler.getInstance().getSetOfNamesKovcheg()
                .iterator();
        int i = 1;
        while (it.hasNext()) {
            String next = it.next();
            mapFormated.put(next, TestHelper.joinPaths("img", String.valueOf(i) + ".gif"));
            i++;
        }
        return mapFormated;
    }

    public Set<String> getSetOfNamesMaltNew() {
        return this.getSetOfFoundString(Arrays.asList(itemsMalt),
                "[А-Я]{1}[а-яёА-Я\"\\ \\-]+", "(:)|(Двуручное)");
    }

    public Set<String> getSetOfNamesStalkerz() {
        return this.getSetOfFoundString(Arrays.asList(itemsStalkerz),
                "[А-Я]{1}[а-яёА-Я\"\\ \\-]+", "(:)|(Двуручное)");
    }

    public Set<String> getSetOfNamesBO() {
        return this.getSetOfFoundString(Arrays.asList(itemsBO),
                "[А-Я]{1}[а-яёА-Я\"\\ \\-]+", "(:)|(Двуручное)");
    }

    public Set<String> getSetOfNamesMalt() {
        return this.getSetOfFoundString(Arrays.asList(maltordenItems),
                "[А-Я]{1}[а-яёА-Я\"\\ \\-]+", "(:)|(Двуручное)");
    }

    public Set<String> getSetOfNamesKovcheg() {
        return this.getSetOfFoundString(getFilesKovchegStore(),
                "[а-яёА-Я\"\\ \\-]+",
                "(Количество)|(\\[)|(:)|(Зарядов)|(Урон \\d+)|(Двуручное)");
    }

    public Set<String> getSetOfNamesFrom(List<Item> items) {
        Set<String> set = new TreeSet<String>();
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext())
            set.add(iterator.next().getName());
        return set;
    }

    /**
     * Search information by patter from list of files.
     *
     * @param list             - list of {@link File} where to search properties
     * @param pattern          - String pattern of what to search
     * @param notInlinePattern - String pattern if presents in the line - skip the line
     * @return
     * @throws Exception
     */
    private TreeSet<String> getSetOfFoundString(List<File> list,
                                                String pattern, String notInlinePattern) {
        TreeSet<String> set = new TreeSet<String>();
        Iterator<File> it = list.iterator();
        LineNumberReader reader;
        Pattern p;
        Matcher match;
        while (it.hasNext()) {
            try {
                reader = new LineNumberReader(new FileReader(it.next()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (notInlinePattern == null) {
                        p = Pattern.compile(pattern);
                        match = p.matcher(line);
                        if (match.find()) {
                            set.add(match.group());
                        }
                    } else {
                        p = Pattern.compile(notInlinePattern);
                        match = p.matcher(line);
                        if (!match.find()) {
                            p = Pattern.compile(pattern);
                            match = p.matcher(line);
                            if (match.find()) {
                                set.add(match.group());
                            }
                        }
                    }

                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return set;
    }

    public List<Item> getAllItems() {
        return getAllItems(getFilesKovchegStore());
    }

    public List<Item> getAllItems(List<File> listOfAllItemsKovchegStore) {
        List<Item> list = new ArrayList<Item>();
        Iterator<File> it = listOfAllItemsKovchegStore.iterator();
        LineNumberReader reader;
        String oneItem = "";
        while (it.hasNext()) {
            try {
                File file = it.next();
                reader = new LineNumberReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String name = TextParser.getName(line);
                    if (name != null) {
                        // System.out.println(oneItem + "\n ------");
                        Item item = ItemBuilder.createItem(oneItem);
                        if (item != null
                                && item.getPropertiesAndValues().size() > 0) {
                            list.add(item);
                            // System.out.println(item.getName());
                            oneItem = "";
                        }
                        oneItem = oneItem.concat(line) + "\n";
                    } else if (Property.getPropertyNameFrom(line) != null
                            || Property.getPropertyValueFrom(line) != null)
                        oneItem = oneItem.concat(line) + "\n";
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (oneItem.length() > 1) {
            Item item = ItemBuilder.createItem(oneItem);
            list.add(item);
            // System.out.println(item.getName() + " added.");
        }
        return list;
    }

    public List<File> getFilesKovchegStore() {
        List<File> allItemsFiles = new ArrayList<File>();
        allItemsFiles.add(helmets);
        allItemsFiles.add(amulets);
        allItemsFiles.add(armours);
        allItemsFiles.add(arms);
        allItemsFiles.add(belts);
        allItemsFiles.add(boots);
        allItemsFiles.add(naru4i);
        allItemsFiles.add(gloves);
        allItemsFiles.add(rings);
        allItemsFiles.add(shields);
        allItemsFiles.add(shootingArms);
        return allItemsFiles;
    }

    public Set<String> compareGetDifference(Set<String> set, Set<String> set2) {
        Set<String> diff;
        if (set.size() > set2.size()) {
            diff = new TreeSet<String>(set);
            diff.removeAll(set2);
        } else {
            diff = new TreeSet<String>(set2);
            diff.removeAll(set);
        }
        return diff;
    }

    public File getImageDir() {
        return new File(TestHelper.joinPaths(TestHelper.TEMP_FILE_DIR.toString(), "img"));
    }

    /*
     * WHOLE THIS STUFF RELATED TO fileGifs
     */
    public List<Item> getAllItemWithImageSrc() {
        List<Item> allItems = getAllItems();
        Iterator<Item> iterator = allItems.iterator();
        Map<String, String> nameAndFile = downloadImagesToImageDir();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            String name = item.getName();
            item.setImageSrc(nameAndFile.get(name));
            assertNotNull("Image src is null:" + name, item.getImageSrc());
            // System.out.println(item.getImageSrc());
        }
        return allItems;
    }

    /**
     * Most neede method for XMLHandler
     *
     * @return
     */
    public Map<Category, List<Item>> getCategoryAndItems() {
        Map<Category, List<Item>> map = new TreeMap<Category, List<Item>>();
        Iterator<File> iterator = getFilesKovchegStore().iterator();
        while (iterator.hasNext()) {
            File next = iterator.next();
            Category category = Category.getCategoryByName(next.getName());
            map.put(category, getAllItemWithImageSrc(next));
        }
        return map;
    }

    private String prepareImgName(String name) {
        return name.replaceAll("[\\ \"]+", "");
    }

    private List<Item> getAllItemWithImageSrc(File file) {
        List<Item> allItems = getAllItems(Arrays.asList(file));
        Iterator<Item> iterator = allItems.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            String itemName = item.getName();
            File imageDir = getImageDir();
            for (String imageName : imageDir.list()) {
                String name = imageName.replaceAll(imageDir.toString(), "")
                        .replaceAll("\\.gif", "");
                // System.out.println("Comparing " + prepareImgName(itemName) +
                // " " + name);
                if (prepareImgName(itemName).equals(name)) {
                    // System.out.println(itemName + ": " + imageDir.toString()
//                            + "/" + imageName);
                    File fileImage = new File(imageDir.toString() + "/"
                            + imageName);
                    byte[] bread = new byte[(int) fileImage.length()];
                    try {
                        new FileInputStream(fileImage).read(bread);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String base64String = Base64.encodeBase64URLSafeString(bread);
                    item.setImageSrc(base64String);
                }
            }
            assertNotNull("Image src is null:" + itemName, item.getImageSrc());
        }
        return allItems;
    }

    public void downloadAllImages() {
        downloadAllImages(getImageNameAndURL());
    }

    public void downloadAllImages(Map<String, URL> imageNameAndURL) {
        Map<String, String> nameAndFileName = downloadImagesToImageDir(imageNameAndURL);
        List<String> incorrectFiles = new ArrayList<String>();
        List<String> incorrectNames = new ArrayList<String>();

        File dir = getImageDir();
        File[] listFiles = dir.listFiles();
        for (File f : listFiles) {
            if (f.length() < 1) {
                // System.out.println(f.toString() + " size: " + f.length());
                f.delete();
                incorrectFiles.add(f.toString());
            }
        }

        for (String name : nameAndFileName.keySet()) {
            for (String file : incorrectFiles) {
                if (file.contains(nameAndFileName.get(name))) {
                    incorrectNames.add(name);
                }
            }
        }
//		System.out.println(incorrectFiles.toString());
//		System.out.println(incorrectNames.toString());

        Map<String, URL> incorrectMap = new TreeMap<String, URL>();
        for (String name : imageNameAndURL.keySet()) {
            if (incorrectNames.contains(name)) {
                incorrectMap.put(name, imageNameAndURL.get(name));
            }
        }
//		System.out.println(incorrectMap.size());
        assert incorrectMap.size() == 0;
//		if (incorrectMap.size() > 0) {
//            downloadImagesToImageDir(incorrectMap);
//		}
    }

    private Map<String, String> downloadImagesToImageDir(Map<String, URL> imageNameAndURL) {
        Map<String, String> nameAndFile = new TreeMap<String, String>();
        File dir = getImageDir();
//        System.out.println("Download folder: " + dir);
        if (!dir.exists()) {
            assert dir.mkdir();
        }
        for (String name : imageNameAndURL.keySet()) {
            URL url = imageNameAndURL.get(name);
            String file = prepareImgName(name + ".gif");
            String pathToSave = TestHelper.joinPaths(dir.getAbsolutePath(), file);
            // System.out.println("Saving: " + pathToSave);
            nameAndFile.put(name, pathToSave);
            // System.out.println(name + "\t" + pathToSave);
            File save = new File(pathToSave);
//            System.out.print(Strings.padEnd(String.format("Downloading: %s", name), 50, '.'));
            if (save.exists() && save.length() > 0L) {
//                System.out.println("SKIPPED");
            } else {
                int tryoutTimes = 10;
                for (int i = 0; i < tryoutTimes; i++) {
                    try {
                        if (save.exists()) {
                            assert save.delete();
                        }
                        assert save.createNewFile();
                        FileOutputStream foStream = new FileOutputStream(save);
//                        boolean isRead = false;
                        InputStream openStream = null;
                        try {
//                            while (!isRead) {
                            openStream = url.openStream();
                            byte[] b = new byte[2048];
                            int length;
                            while ((length = openStream.read(b)) != -1) {
                                foStream.write(b, 0, length);
                            }
                            try {
                                foStream.flush();
                                foStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.exit(-1);
                            }
//                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (openStream != null) {
                                openStream.close();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (save.length() > 0) {
//                        System.out.println("OK");
                        break;
                    } else {
//                        System.out.println(".");
                    }

                }
            }
        }
        return nameAndFile;
    }

    private Map<String, String> downloadImagesToImageDir() {
        return downloadImagesToImageDir(getImageNameAndURL());
    }

    public Map<String, URL> getImageNameAndURL() {
        Map<String, URL> nameAndURL = new TreeMap<String, URL>();
        for (String line : getLines()) {
            Matcher mHttp = Pattern.compile("http[a-zA-Z\\:\\.\\d\\_\\-\\/]+")
                    .matcher(line);

            if (mHttp.find() && !line.contains("Кристалл")) {
                URL url = null;
                try {
                    url = new URL(mHttp.group());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String name = line.substring(line.lastIndexOf("title=\""));
                name = name.substring(0, name.indexOf("\">")).replaceAll(
                        "title=\"", "");
                name = name.replaceAll("", "");
                String group = name.trim();
                nameAndURL.put(group, url);
            } else {
                // System.out.println("Skiped: " + line);
            }
        }
        return nameAndURL;
    }

    private List<String> getLines() {
        List<String> lines = new LinkedList<String>();
        LineNumberReader reader;
        try {
            reader = new LineNumberReader(new FileReader(fileGifsLess));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * minimizes fileGifs context
     */
    private void rewriteFormatData() {
        // System.out.println(fileGifs.exists());
        LineNumberReader reader;
        try {
            reader = new LineNumberReader(new FileReader(fileGifs));
            String line;
            String out = "";
//			 int i = 1;
            while ((line = reader.readLine()) != null) {
                Matcher match = Pattern.compile("<tr class=item><td").matcher(
                        line);
                if (match.find()) {
                    String replaceAll = line.replaceAll(" \\(закл\\.\\)", "")
                            .replaceAll(" \\(мод\\.\\)", "");
                    out = out.concat(replaceAll + "\n");
//					 i++;
//					 System.out.println(replaceAll);
                }

            }
            reader.close();
            FileWriter writer = new FileWriter(fileGifsLess);
            writer.write(out);
            // System.out.println(fileGifsLess);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
