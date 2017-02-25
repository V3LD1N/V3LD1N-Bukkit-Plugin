package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class PlayerSayCommand extends V3LD1NCommand {
    private final String DEFAULT_GROUP = "Veldian";

    public PlayerSayCommand() {
        this.addUsage("<player> <message ...>", "Send a chat message as a player");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.owner")) return true;

        if (args.length < 2) {
            this.sendUsage(sender);
            return true;
        }

        say(args[0], StringUtil.fromArray(args, 1));
        return true;
    }

    private void say(String playerName, String message) {
        String nameWithPrefix = playerName;
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (PlayerUtil.getOnlinePlayer(playerName) != null) {
            Player p = PlayerUtil.getOnlinePlayer(playerName);
            p.chat(message);
            return;
        } else if (pluginManager.getPlugin("PermissionsEx") != null && PlayerUtil.getOfflinePlayer(playerName) != null) {
            String name = PlayerUtil.getOfflinePlayer(playerName).getName();
            PermissionUser user = PermissionsEx.getUser(name);
            String prefix = user.getPrefix();
            if (prefix.equals("")) {
                prefix = PermissionsEx.getPermissionManager().getGroup(DEFAULT_GROUP).getPrefix();
            }
            prefix = StringUtil.formatText(prefix);
            nameWithPrefix = prefix + name;
        }
        Bukkit.broadcastMessage("<" + nameWithPrefix + "> " + message);
    }
}