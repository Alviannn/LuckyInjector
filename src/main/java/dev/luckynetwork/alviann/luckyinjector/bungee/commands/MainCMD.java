package dev.luckynetwork.alviann.luckyinjector.bungee.commands;

import dev.luckynetwork.alviann.luckyinjector.bungee.BungeeInjector;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MainCMD extends Command {

    public MainCMD(String name) {
        super(name);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            sender.sendMessage("§cThis command is only for console!");
            return;
        }

        BungeeInjector plugin = BungeeInjector.getInstance();

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /luckyinjector reload");
        } else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage("§aSuccessfully reloaded config file!");
        } else {
            sender.sendMessage("§cUsage: /luckyinjector reload");
        }
    }

}
