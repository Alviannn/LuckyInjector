package dev.luckynetwork.alviann.sqlhelperinjection.bungee;

import dev.luckynetwork.alviann.sqlhelperinjection.loader.Loader;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;

public class SQLHelperInjectionBungee extends Plugin {

    @Getter private static SQLHelperInjectionBungee instance;

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
            instance = new SQLHelperInjectionBungee();
        }

        Loader.injectSQLHelper(instance.getClass(), instance.getDataFolder());
    }

}
