package dev.luckynetwork.alviann.luckyinjector.loader;

import com.github.alviannn.lib.dependencyhelper.DependencyHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.luckynetwork.alviann.luckyinjector.closer.Closer;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

        File pluginFile = Loader.getCurrentPluginFile();
        if (pluginFile == null || !pluginFile.exists())
            throw new FileNotFoundException("Cannot find the current plugin jar file!");

        // stores the dependencies name and url
        Map<String, String> dependenciesMap = new HashMap<>();

        // fetch all dependencies entries
        try (Closer closer = new Closer()) {
            JarFile jarFile = closer.add(new JarFile(pluginFile));
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String fileName = entry.getName();

                if (entry.isDirectory())
                    continue;
                if (!fileName.contains("internal-depends") || !fileName.endsWith(".json"))
                    continue;

                try {
                    InputStream resource = closer.add(jarFile.getInputStream(entry));
                    InputStreamReader streamReader = closer.add(new InputStreamReader(resource));

                    JsonElement rawJson = JsonParser.parseReader(streamReader);
                    JsonArray dependencies = rawJson.getAsJsonArray();

                    if (dependencies.size() == 0)
                        continue;

                    for (JsonElement element : dependencies) {
                        JsonObject dependency = element.getAsJsonObject();

                        dependenciesMap.put(
                                dependency.get("name").getAsString(),
                                dependency.get("url").getAsString()
                        );
                    }
                } catch (Exception ignored) {
                }
            }
        }

        helper.download(dependenciesMap, dirPath);
        helper.load(dependenciesMap, dirPath);
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

    /**
     * gets the plugin jar file
     */
    @Nullable
    public static File getCurrentPluginFile() {
        try {
            return new File(Loader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (Exception ignored) {
        }

        return null;
    }

}
