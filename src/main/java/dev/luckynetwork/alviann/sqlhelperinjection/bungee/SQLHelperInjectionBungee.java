package dev.luckynetwork.alviann.sqlhelperinjection.bungee;

import dev.luckynetwork.alviann.sqlhelperinjection.loader.Loader;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;

public class SQLHelperInjectionBungee extends Plugin {

    @SneakyThrows
    @Override
    public void onEnable() {
        Loader.injectSQLHelper(this.getClass(), this.getDataFolder());
    }

    public static void loadEarly() throws Exception {
        SQLHelperInjectionBungee instance = new SQLHelperInjectionBungee();

        Loader.injectSQLHelper(instance.getClass(), instance.getDataFolder());
    }

}