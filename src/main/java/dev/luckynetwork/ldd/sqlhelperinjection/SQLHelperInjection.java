package dev.luckynetwork.ldd.sqlhelperinjection;

import com.github.alviannn.lib.dependencyhelper.DependencyHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class SQLHelperInjection extends JavaPlugin {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        DependencyHelper helper = new DependencyHelper(this.getClass().getClassLoader());
        File dir = new File(this.getDataFolder(), "libs");

        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            helper.download("SQLHelper-2.4.jar", "https://github.com/Alviannn/SQLHelper/releases/download/2.4/SQLHelper-2.4.jar", dir.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

    }

}
