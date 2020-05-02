package dev.luckynetwork.alviann.luckyinjector.spigot;

import com.github.alviannn.sqlhelper.SQLBuilder;
import com.github.alviannn.sqlhelper.SQLHelper;
import dev.luckynetwork.alviann.luckyinjector.loader.Loader;
import dev.luckynetwork.alviann.luckyinjector.updater.Updater;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SpigotInjector extends JavaPlugin {

    @Getter private static SpigotInjector instance;
    @Getter private Updater updater;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        updater = new Updater();

        SpigotInjector.loadEarly();

        // auto update task
        Bukkit.getScheduler().runTaskTimer(this, () -> updater.fetchUpdateAsync().whenComplete((result, error) -> {
            if (error != null) {
                System.err.println(error.getMessage());
                return;
            }

            if (!result)
                return;

            if (updater.checkUpdate())
                updater.update(instance.getDataFolder().getParentFile(), true);
        }), 20L, 600L);
    }

    /**
     * loads the injector earlier
     */
    public static void loadEarly() throws Exception {
        Loader.startInjecting(SpigotInjector.class);
        Loader.initConfig(SpigotInjector.class);
    }

    /**
     * gets the default SQLBuilder instance with the default values (host, port, username, and password)
     * <p>
     * NOTE: this isn't finished yet as the database is empty and you need to fill it out alone like on the code below
     * <p>
     * <pre><code>
     *     SQLBuilder builder = ...;
     *     builder.setDatabase("the database name");
     * </code></pre>
     */
    @SneakyThrows
    public static SQLBuilder getDefaultSQLBuilder() {
        Loader.initConfig(SpigotInjector.class);

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
