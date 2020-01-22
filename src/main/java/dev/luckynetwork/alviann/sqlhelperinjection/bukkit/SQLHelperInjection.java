package dev.luckynetwork.alviann.sqlhelperinjection.bukkit;

import dev.luckynetwork.alviann.sqlhelperinjection.loader.Loader;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

public class SQLHelperInjection extends JavaPlugin {

    @SneakyThrows
    @Override
    public void onEnable() {
        Loader.injectSQLHelper(this.getClass(), this.getDataFolder());
    }

    public static void loadEarly() throws Exception {
        SQLHelperInjection instance = new SQLHelperInjection();

        Loader.injectSQLHelper(instance.getClass(), instance.getDataFolder());
    }

}
