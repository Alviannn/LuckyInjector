package dev.luckynetwork.alviann.luckyinjector.bungee;

import dev.luckynetwork.alviann.luckyinjector.loader.Loader;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;

@SuppressWarnings("unused")
public class BungeeInjector extends Plugin {

    @Getter private static BungeeInjector instance;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        Loader.startInjecting(this.getClass(), this.getDataFolder());
    }

    public static void loadEarly() throws Exception {
        if (instance == null)
            instance = new BungeeInjector();

        Loader.startInjecting(instance.getClass(), instance.getDataFolder());
    }

}
