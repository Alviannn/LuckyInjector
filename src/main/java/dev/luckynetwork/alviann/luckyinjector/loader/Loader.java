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

        DATA_FOLDER.mkdirs();

        Path dirPath = DATA_FOLDER.toPath();
        Map<String, String> downloadMap = new HashMap<>();

        downloadMap.put("SQLHelper-2.5.4.jar", "https://github.com/Alviannn/SQLHelper/releases/download/2.5.4/SQLHelper-2.5.4.jar");

        helper.download(downloadMap, dirPath);
        helper.load(downloadMap, dirPath);
    }

    /**
     * initializes the config file to be used by the injector
     *
     * @param clazz the main class
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SneakyThrows
    public static void initConfig(Class<?> clazz) {
        DATA_FOLDER.mkdirs();
        if (CONFIG_FILE.exists())
            return;

        try (Closer closer = new Closer()) {
            InputStream stream = closer.add(clazz.getClassLoader().getResourceAsStream("config.yml"));

            if (stream == null)
                throw new NullPointerException("Cannot find config.yml file!");

            Files.copy(stream, CONFIG_FILE.toPath());
        }
    }

}
