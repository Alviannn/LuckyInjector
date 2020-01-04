package dev.luckynetwork.alviann.sqlhelperinjection.spigot;

import dev.luckynetwork.alviann.sqlhelperinjection.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SQLHelperInjection extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            Utils.injectSQLHelper(this.getClass(), this.getDataFolder());
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

    }

}
