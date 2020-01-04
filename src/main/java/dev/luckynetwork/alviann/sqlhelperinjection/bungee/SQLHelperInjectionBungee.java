package dev.luckynetwork.alviann.sqlhelperinjection.bungee;

import dev.luckynetwork.alviann.sqlhelperinjection.utils.Utils;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;

public class SQLHelperInjectionBungee extends Plugin {

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
