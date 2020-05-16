package dev.luckynetwork.alviann.luckyinjector.spigot.commands;

import dev.luckynetwork.alviann.luckyinjector.spigot.SpigotInjector;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage("§cThis command is only for console!");
            return true;
        }

        SpigotInjector plugin = SpigotInjector.getInstance();

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /luckyinjector reload");
        } else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage("§aSuccessfully reloaded config file!");
        } else {
            sender.sendMessage("§cUsage: /luckyinjector reload");
        }

        return true;
    }

}
