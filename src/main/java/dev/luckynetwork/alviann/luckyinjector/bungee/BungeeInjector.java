package dev.luckynetwork.alviann.luckyinjector.bungee;

import com.github.alviannn.sqlhelper.SQLBuilder;
import com.github.alviannn.sqlhelper.SQLHelper;
import com.github.alviannn.sqlhelper.utils.Closer;
import dev.luckynetwork.alviann.luckyinjector.loader.Loader;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        Plugin plugin = getPlugin();

        if (instance == null && plugin != null)
            instance = (BungeeInjector) plugin;
        if (instance == null)
            instance = new BungeeInjector();

        Loader.startInjecting(instance.getClass());
        Loader.initConfig(instance.getClass());
    }

    private static Plugin getPlugin() {
        PluginManager manager = ProxyServer.getInstance().getPluginManager();
        String pluginName;

        try (Closer closer = new Closer()) {
            ClassLoader loader = BungeeInjector.class.getClassLoader();
            InputStream stream = loader.getResourceAsStream("bungee.yml");

            if (stream == null)
                return null;

            closer.add(stream);

            InputStreamReader streamReader = closer.add(new InputStreamReader(stream));
            ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
            Configuration plugin = provider.load(streamReader);

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
