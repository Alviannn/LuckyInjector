package dev.luckynetwork.alviann.luckyinjector.spigot;

import com.github.alviannn.sqlhelper.SQLBuilder;
import com.github.alviannn.sqlhelper.SQLHelper;
import com.github.alviannn.sqlhelper.utils.Closer;
import dev.luckynetwork.alviann.luckyinjector.bungee.BungeeInjector;
import dev.luckynetwork.alviann.luckyinjector.loader.Loader;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SpigotInjector extends JavaPlugin {

    @Getter private static SpigotInjector instance;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;

        Loader.startInjecting(this.getClass());
        Loader.initConfig(this.getClass());
    }

    /**
     * loads the injector earlier
     */
    public static void loadEarly() throws Exception {
        Plugin plugin = getPlugin();

        if (instance == null && plugin != null)
            instance = (SpigotInjector) plugin;
        if (instance == null)
            instance = new SpigotInjector();

        Loader.startInjecting(instance.getClass());
        Loader.initConfig(instance.getClass());
    }

    private static Plugin getPlugin() {
        PluginManager manager = Bukkit.getPluginManager();
        String pluginName;

        try (Closer closer = new Closer()) {
            ClassLoader loader = BungeeInjector.class.getClassLoader();
            InputStream stream = loader.getResourceAsStream("bungee.yml");

            if (stream == null)
                return null;

            closer.add(stream);

            InputStreamReader streamReader = closer.add(new InputStreamReader(stream));
            FileConfiguration plugin = YamlConfiguration.loadConfiguration(streamReader);

            pluginName = plugin.getString("name");
        }

        return manager.getPlugin(pluginName);
    }

    /**
     * gets the default SQLBuilder instance with the default values (host, port, username, and password)
     * <p>
     * NOTE: this isn't finished yet as the database is empty and you need to fill it out alone like on the code below
     * <p>
     * <pre><code>
     *     SQLBuilder builder = ...;
     *     builder.setDatabase("the database name");
     * </code><pre/>
     */
    @SneakyThrows
    public static SQLBuilder getDefaultSQLBuilder() {
        Loader.initConfig(instance.getClass());

        File configFile = Loader.CONFIG_FILE;
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        return SQLHelper.newBuilder(SQLHelper.Type.MYSQL)
                .setHost(config.getString("sql.host"))
                .setPort(config.getString("sql.port"))
                .setUsername(config.getString("sql.username"))
                .setPassword(config.getString("sql.password"))
                .setHikari(true);
    }

}
