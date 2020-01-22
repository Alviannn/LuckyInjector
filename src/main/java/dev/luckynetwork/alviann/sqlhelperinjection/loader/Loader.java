package dev.luckynetwork.alviann.sqlhelperinjection.loader;

import com.github.alviannn.lib.dependencyhelper.DependencyHelper;

import java.io.File;

public class Loader {

    /**
     * injects the SQL Helper to server
     *
     * @param clazz      the main class
     * @param dataFolder the data folder
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void injectSQLHelper(Class<?> clazz, File dataFolder) throws Exception {
        DependencyHelper helper = new DependencyHelper(clazz.getClassLoader());
        File dir = new File(dataFolder, "libs");

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        if (!dir.exists()) {
            dir.mkdir();
        }

        helper.download("SQLHelper-2.4.jar", "https://github.com/Alviannn/SQLHelper/releases/download/2.4/SQLHelper-2.4.jar", dir.toPath());
        helper.loadDir(dir.toPath());
    }

}
