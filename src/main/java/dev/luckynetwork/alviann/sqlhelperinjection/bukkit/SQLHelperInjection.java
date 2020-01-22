package dev.luckynetwork.alviann.sqlhelperinjection.bukkit;

import dev.luckynetwork.alviann.sqlhelperinjection.loader.Loader;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

public class SQLHelperInjection extends JavaPlugin {

    @Getter private static SQLHelperInjection instance;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        Loader.injectSQLHelper(this.getClass(), this.getDataFolder());
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    public static void loadEarly() throws Exception {
        if (instance == null) {
            instance = new SQLHelperInjection();
        }

        Loader.injectSQLHelper(instance.getClass(), instance.getDataFolder());
    }

}
