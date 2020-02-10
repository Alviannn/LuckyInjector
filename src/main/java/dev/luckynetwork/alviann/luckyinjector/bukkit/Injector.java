package dev.luckynetwork.alviann.luckyinjector.bukkit;

import dev.luckynetwork.alviann.luckyinjector.loader.Loader;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Injector extends JavaPlugin {

    @Getter private static Injector instance;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        Loader.startInjecting(this.getClass(), this.getDataFolder());
    }

    @SneakyThrows
    @Override
    public void onLoad() {
        instance = this;
        Loader.startInjecting(this.getClass(), this.getDataFolder());
    }

    public static void loadEarly() throws Exception {
        if (instance == null) {
            instance = new Injector();
        }

        Loader.startInjecting(instance.getClass(), instance.getDataFolder());
    }

}
