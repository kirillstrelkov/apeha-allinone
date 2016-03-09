package apeha.allinone.common;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AppPropertiesTest {
    public static final String COMMON_TESTEXPECTED_PROPERTIES = "common/testexpected.properties";
    private static String DEFAULT_FILE = AppProperties.DEFAULT_FILE;

    private void removePropFile() {
        new File(DEFAULT_FILE).delete();
    }

    @Test
    public void fileNotFoundExc() {
        AppProperties props = AppProperties.newAppProperties();
        assertEquals(props.properties.keySet().size(), 0);
    }

    @Test
    public void loadProperties() {
        this.copyPropFile();
        AppProperties props = AppProperties.newAppProperties();

        assertEquals(props.getDefaultFile(), "/tmp/apeha2.xml");
        assertEquals(props.getDefaultSpell(), "30");
        assertEquals(props.getLoadAtStart(), true);
        this.removePropFile();
    }

    @Test
    public void loadPropertiesFileNotExist() {
        AppProperties props = AppProperties.newAppProperties();

        assertNull(props.getDefaultFile());
        assertNull(props.getDefaultSpell());
        assertEquals(props.getLoadAtStart(), false);
    }

    @Test
    public void setTest() {
        AppProperties props = AppProperties.newAppProperties();

        String tmp = "/tmp/sbla";
        props.setDefaultFile(tmp);
        assertEquals(props.getDefaultFile(), tmp);

        tmp = "45";
        props.setDefaultSpell(tmp);
        assertEquals(props.getDefaultSpell(), tmp);

        props.setLoadAtStart(true);
        assertEquals(props.getLoadAtStart(), true);
    }

    @Test
    public void saveTest() throws IOException {
        Properties properties = new Properties();
        properties.setProperty(AppProperties.KEY_LOAD_AT_START, "true");
        properties.setProperty(AppProperties.KEY_DEFAULT_FILE,
                "/tmp/apeha2.xml");
        properties.setProperty(AppProperties.KEY_DEFAULT_SPELL, "30");

        File createTempFile = File.createTempFile(".apeha", ".properties");
        createTempFile.deleteOnExit();
        String path = createTempFile.toString();
        properties.store(new OutputStreamWriter(new FileOutputStream(
                createTempFile), "UTF-8"), null);
        System.out.println(path);
        System.out.println(properties);

        LineNumberReader reader = new LineNumberReader(new FileReader(
                createTempFile));
        LineNumberReader reader2 = new LineNumberReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(COMMON_TESTEXPECTED_PROPERTIES)));
        List<String> list1 = getList(reader);
        List<String> list2 = getList(reader2);
        assertEquals(list1.toString(), list2.toString());
    }

    private List<String> getList(LineNumberReader reader) {
        List<String> list = Lists.newLinkedList();
        String readLine;
        try {
            while ((readLine = reader.readLine()) != null) {
                if (readLine.startsWith("#"))
                    continue;
                list.add(readLine);
            }
        } catch (Exception e) {
        }
        return list;
    }

    private void copyPropFile() {
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("common/testexpected.properties");
            OutputStream out = new FileOutputStream(AppProperties.DEFAULT_FILE);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
