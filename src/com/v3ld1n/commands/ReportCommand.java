package com.v3ld1n.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;

public class ReportCommand extends V3LD1NCommand {
    String usageReport = "<player> <reason ...>";
    String usageRead = ("read <report number>");

    public ReportCommand() {
        this.addUsage(usageReport, "Report a player to the server admins");
        this.addUsage(usageRead, "Read a report");
        this.addUsage("list", "List reports");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String playerName = p.getName();
            UUID playerUuid = p.getUniqueId();
            String border = "{text:\"" + Message.REPORT_LIST_BORDER + "\","
                    + "color:dark_red}";
            String top = "{text:\"" + Message.REPORT_LIST_TOP + "\","
                    + "color:red}";
            if (args.length >= 2) {
                if (sender.hasPermission("v3ld1n.report.read") && args.length == 2) {
                    if (args[0].equalsIgnoreCase("read")) {
                        int arg;
                        try {
                            arg = Integer.parseInt(args[1]);
                        } catch (IllegalArgumentException e) {
                            this.sendArgumentUsage(sender, label, command, usageRead);
                            return true;
                        }
                        if (arg <= V3LD1N.getReports().size() && arg > 0) {
                            Report report = V3LD1N.getReports().get(arg - 1);
                            String title = "{text:\"" + Message.REPORT_READ_TITLE + "\","
                                    + "color:gold,"
                                    + "extra:["
                                    + "{text:\"" + report.getTitle() + "\","
                                    + "color:aqua}]}";
                            String senderName = "{text:\"" + Message.REPORT_READ_SENDER + "\","
                                    + "color:yellow,"
                                    + "extra:["
                                    + "{text:\"" + report.getSenderName() + "\","
                                    + "color:green}]}";
                            String reason = "{text:\"" + Message.REPORT_READ_REASON + "\","
                                    + "color:gold,"
                                    + "extra:["
                                    + "{text:\"" + report.getReason() + "\","
                                    + "color:aqua}]}";
                            String back = "{text:\"" + Message.REPORT_READ_BACK + "\","
                                    + "color:" + ConfigSetting.REPORT_READ_BACK_COLOR.getString() + ","
                                    + "clickEvent:{"
                                    + "action:\"run_command\","
                                    + "value:\"/" + label + " list\"}}";
                            ChatUtil.sendJsonMessage(p, border, 0);
                            ChatUtil.sendJsonMessage(p, top, 0);
                            ChatUtil.sendJsonMessage(p, border, 0);
                            ChatUtil.sendJsonMessage(p, title, 0);
                            ChatUtil.sendJsonMessage(p, senderName, 0);
                            ChatUtil.sendJsonMessage(p, reason, 0);
                            ChatUtil.sendJsonMessage(p, back, 0);
                            return true;
                        }
                        p.sendMessage(Message.REPORT_READ_INVALID.toString());
                        return true;
                    }
                }
                String title = args[0];
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String reason = sb.toString();
                reason = reason.substring(0, reason.length() - 1);
                int sameTitles = 0;
                for (Report report : V3LD1N.getReports()) {
                    if (report.getTitle().equalsIgnoreCase(title) || report.getTitle().startsWith(title + "(")) {
                        ++sameTitles;
                    }
                }
                if (sameTitles > 0) {
                    title = title + "(" + sameTitles + ")";
                }
                title = title.replaceAll("[\"\\\\]", "");
                if (title.isEmpty()) {
                    title = Message.REPORT_NO_TITLE.toString();
                }
                reason = reason.replaceAll("[\"\\\\]", "");
                if (reason.isEmpty()) {
                    title = Message.REPORT_NO_REASON.toString();
                }
                Report report = new Report(title, playerName, playerUuid, reason);
                V3LD1N.addReport(report);
                ChatUtil.sendUnreadReports();
                ChatUtil.sendMessage(sender, Message.REPORT_SEND.toString(), 2);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    if (sender.hasPermission("v3ld1n.report.read")) {
                        ChatUtil.sendJsonMessage(p, border, 0);
                        ChatUtil.sendJsonMessage(p, top, 0);
                        ChatUtil.sendJsonMessage(p, border, 0);
                        if (V3LD1N.getReports().isEmpty()) {
                            sender.sendMessage(Message.REPORT_LIST_EMPTY.toString());
                        } else {
                            for (Report report : V3LD1N.getReports()) {
                                ChatUtil.sendJsonMessage(p,
                                "{text:\"" + (V3LD1N.getReports().indexOf(report) + 1) + ". \","
                                + "color:" + "gold" + ","
                                + "extra:["
                                + "{text:\"" + report.getTitle() + "\","
                                + "color:" + "aqua" + ","
                                + "clickEvent:{"
                                + "action:\"run_command\","
                                + "value:\"/" + label + " read " + (V3LD1N.getReports().indexOf(report) + 1) + "\"}}]}"
                                , 0);
                            }
                            ChatUtil.sendJsonMessage(p,
                                    "{text:\"" + Message.REPORT_LIST_HELP + "\","
                                    + "color:green}", 0);
                        }
                        return true;
                    }
                    sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
                    return true;
                }
            }
            if (sender.hasPermission("v3ld1n.report.read")) {
                this.sendUsage(sender, label, command);
            } else {
                this.sendArgumentUsage(sender, label, command, usageReport);
            }
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}