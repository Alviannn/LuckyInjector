package dev.luckynetwork.alviann.luckyinjector.bungee;

import com.github.alviannn.sqlhelper.SQLBuilder;
import com.github.alviannn.sqlhelper.SQLHelper;
import dev.luckynetwork.alviann.luckyinjector.bungee.commands.MainCMD;
import dev.luckynetwork.alviann.luckyinjector.loader.Loader;
import dev.luckynetwork.alviann.luckyinjector.updater.Updater;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

public class BungeeInjector extends Plugin {

    @Getter private static BungeeInjector instance;
    @Getter private Updater updater;

    @Getter private File configFile;
    @Getter private Configuration config;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        updater = new Updater();

        BungeeInjector.loadEarly();

        Loader.initConfig(BungeeInjector.class);
        this.reloadConfig();

        // auto update task
        this.getProxy().getScheduler().schedule(this, () -> {
            if (!this.isAutoUpdate())
                return;

            updater.fetchUpdateAsync().whenComplete((result, error) -> {
                if (error != null) {
                    System.err.println(error.getMessage());
                    return;
                }

                if (!result)
                    return;

                if (updater.checkUpdate())
                    updater.update(instance.getDataFolder().getParentFile(), true);
            });
        }, 1L, 30L, TimeUnit.SECONDS);

        PluginManager manager = this.getProxy().getPluginManager();
        manager.registerCommand(this, new MainCMD("luckyinjector"));
    }

    /**
     * loads the injector earlier
     */
    public static void loadEarly() throws Exception {
        Loader.startInjecting(BungeeInjector.class);
        Loader.initConfig(BungeeInjector.class);
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
        Loader.initConfig(BungeeInjector.class);

        File configFile = Loader.CONFIG_FILE;
        ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);

        Configuration config = provider.load(configFile);
        return SQLHelper.newBuilder(SQLHelper.Type.MYSQL)
                .setHost(config.getString("sql.host"))
                .setPort(config.getString("sql.port"))
                .setUsername(config.getString("sql.username"))
                .setPassword(config.getString("sql.password"))
                .setHikari(true);
    }

    /**
     * checks if the auto-update feature is enabled,
     * by default it's {@code true}
     */
    public boolean isAutoUpdate() {
        return config.getBoolean("auto-update", true);
    }

    /**
     * reloads the config instance
     */
    @SneakyThrows
    public void reloadConfig() {
        if (configFile == null)
            configFile = new File(this.getDataFolder(), "config.yml");

        if (!configFile.exists())
            throw new FileNotFoundException("Cannot find config.yml file!");

        ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        config = provider.load(configFile);

        if (!config.contains("auto-update")) {
            config.set("auto-update", true);

            provider.save(config, configFile);
            config = provider.load(configFile);
        }
    }

}
