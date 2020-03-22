package dev.luckynetwork.alviann.luckyinjector.bungee;

import com.github.alviannn.sqlhelper.SQLBuilder;
import com.github.alviannn.sqlhelper.SQLHelper;
import com.github.alviannn.sqlhelper.utils.Closer;
import dev.luckynetwork.alviann.luckyinjector.loader.Loader;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

@SuppressWarnings("unused")
public class BungeeInjector extends Plugin {

    @Getter private static BungeeInjector instance;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        Loader.startInjecting(this.getClass());
    }

    /**
     * loads the injector earlier
     */
    public static void loadEarly() throws Exception {
        if (instance == null)
            instance = new BungeeInjector();

        Loader.startInjecting(instance.getClass());
    }

    /**
     * initializes the config file before the normal load (this is an early load)
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void initConfig() {
        File dataFolder = new File("LuckyInjector");
        if (!dataFolder.exists())
            dataFolder.mkdir();

        File configFile = new File(dataFolder, "config.yml");
        if (configFile.exists())
            return;

        try (Closer closer = new Closer()) {
            InputStream stream = BungeeInjector.class.getResourceAsStream("config.yml");
            if (stream == null)
                return;

            closer.add(stream);
            Files.copy(stream, configFile.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        initConfig();

        File configFile = new File("LuckyInjector", "config.yml");
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
