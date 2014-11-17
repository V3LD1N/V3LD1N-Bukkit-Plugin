package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;

import com.v3ld1n.Message;

public class SetFullTimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof LivingEntity) {
            if (sender.isOp()) {
                if (args.length == 1) {
                    LivingEntity p = (LivingEntity) sender;
                    long time = p.getWorld().getFullTime();
                    p.getWorld().setFullTime(Long.parseLong(args[0]));
                    sender.sendMessage(String.format(Message.SETFULLTIME_SET.toString(), time, Long.parseLong(args[0])));
                    return true;
                }
                return false;
            }
            sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER_ENTITY.toString());
        return true;
    }
}