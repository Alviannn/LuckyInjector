package dev.luckynetwork.alviann.luckyinjector.spigot;

import dev.luckynetwork.alviann.luckyinjector.loader.Loader;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class SpigotInjector extends JavaPlugin {

    @Getter private static SpigotInjector instance;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        Loader.startInjecting(this.getClass(), this.getDataFolder());
    }

    public static void loadEarly() throws Exception {
        if (instance == null)
            instance = new SpigotInjector();

        Loader.startInjecting(instance.getClass(), instance.getDataFolder());
    }

}
