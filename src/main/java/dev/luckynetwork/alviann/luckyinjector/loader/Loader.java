package dev.luckynetwork.alviann.luckyinjector.loader;

import com.github.alviannn.lib.dependencyhelper.DependencyHelper;
import com.github.alviannn.sqlhelper.utils.Closer;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Loader {

    public static final File DATA_FOLDER = new File("plugins", "LuckyInjector");
    public static final File CONFIG_FILE = new File(DATA_FOLDER, "config.yml");

    /**
     * injects the SQL Helper to server
     *
     * @param clazz the main class
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void startInjecting(Class<?> clazz) throws Exception {
        DependencyHelper helper = new DependencyHelper(clazz.getClassLoader());

        if (!DATA_FOLDER.getParentFile().exists())
            DATA_FOLDER.getParentFile().mkdir();
        if (!DATA_FOLDER.exists())
            DATA_FOLDER.mkdir();

        Path dirPath = DATA_FOLDER.toPath();
        Map<String, String> downloadMap = new HashMap<>();

        downloadMap.put("SQLHelper-2.5.jar", "https://github.com/Alviannn/SQLHelper/releases/download/2.5/SQLHelper-2.5.jar");

        helper.download(downloadMap, dirPath);
        helper.loadDir(dirPath);
    }

    /**
     * initializes the config file to be used by the injector
     *
     * @param clazz the main class
     */
    @SneakyThrows
    public static void initConfig(Class<?> clazz) {
        if (CONFIG_FILE.exists())
            return;

        try (Closer closer = new Closer()) {
            InputStream stream = clazz.getClassLoader().getResourceAsStream("config.yml");

            if (stream == null)
                throw new NullPointerException("Cannot find config.yml!");

            closer.add(stream);
            Files.copy(stream, CONFIG_FILE.toPath());
        }
    }

}
