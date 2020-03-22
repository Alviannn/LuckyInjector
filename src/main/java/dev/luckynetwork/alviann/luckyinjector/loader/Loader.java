package dev.luckynetwork.alviann.luckyinjector.loader;

import com.github.alviannn.lib.dependencyhelper.DependencyHelper;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Loader {

    /**
     * injects the SQL Helper to server
     *
     * @param clazz the main class
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void startInjecting(Class<?> clazz) throws Exception {
        DependencyHelper helper = new DependencyHelper(clazz.getClassLoader());
        File dataFolder = new File("LuckyInjector");

        if (!dataFolder.exists())
            dataFolder.mkdir();

        Path dirPath = dataFolder.toPath();
        Map<String, String> downloadMap = new HashMap<>();

        downloadMap.put("SQLHelper-2.5.jar", "https://github.com/Alviannn/SQLHelper/releases/download/2.5/SQLHelper-2.5.jar");

        helper.download(downloadMap, dirPath);
        helper.loadDir(dirPath);
    }

}
