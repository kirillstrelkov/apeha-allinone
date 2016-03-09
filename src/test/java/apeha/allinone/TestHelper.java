package apeha.allinone;

import java.io.File;

public class TestHelper {
    public static final File TEMP_FILE_DIR = new File(System.getProperty("java.io.tmpdir"));

    public static String joinPaths(String... paths) {
        File fullPath = null;
        for (String path : paths) {
            if (fullPath == null) {
                fullPath = new File(path);
            } else {
                fullPath = new File(fullPath, path);
            }
        }
        return fullPath.getPath();
    }
}
