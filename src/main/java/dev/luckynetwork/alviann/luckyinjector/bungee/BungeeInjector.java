package dev.luckynetwork.alviann.luckyinjector.bungee;

import com.github.alviannn.sqlhelper.SQLBuilder;
import com.github.alviannn.sqlhelper.SQLHelper;
import dev.luckynetwork.alviann.luckyinjector.loader.Loader;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;

@SuppressWarnings("unused")
public class BungeeInjector extends Plugin {

    @Getter private static BungeeInjector instance;

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
        if (instance == null)
            instance = new BungeeInjector();

        Loader.startInjecting(instance.getClass());
        Loader.initConfig(instance.getClass());
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
        ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);

        Configuration config = provider.load(configFile);
        return SQLHelper.newBuilder(SQLHelper.Type.MYSQL)
                .setHost(config.getString("sql.host"))
                .setPort(config.getString("sql.port"))
                .setUsername(config.getString("sql.username"))
                .setPassword(config.getString("sql.password"))
                .setHikari(true);
    }

}
