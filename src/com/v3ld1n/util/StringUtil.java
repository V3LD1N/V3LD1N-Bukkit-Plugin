package com.v3ld1n.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;

public class StringUtil {
    private StringUtil() {
    }

    /**
     * Sends a message to the console if debug is set to true
     * @param message the message to send to the console
     */
    public static void logDebugMessage(String message) {
        if (ConfigSetting.DEBUG.getBoolean()) {
            V3LD1N.getPlugin().getLogger().info(message);
        }
    }

    /**
     * Returns the current server time
     * @return the current server time
     */
    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(calendar.getTime());
    }

    /**
     * Returns a string with player variables
     * @param string the string to edit
     * @param player the player
     * @return the string with variables replaced
     */
    public static String replacePlayerVariables(String string, Player player) {
        String ignoreCase = "(?i)";
        String replaced = string
                .replaceAll(ignoreCase + "%name%", player.getName())
                .replaceAll(ignoreCase + "%uuid%", player.getUniqueId().toString())
                .replaceAll(ignoreCase + "%displayname%", player.getDisplayName())
                .replaceAll(ignoreCase + "%world%", player.getWorld().getName())
                .replaceAll(ignoreCase + "%biome%", upperCaseFirst(player.getLocation().getBlock().getBiome().name().toLowerCase().replaceAll("_", " ")))
                .replaceAll(ignoreCase + "%health%", Double.toString(player.getHealth()))
                .replaceAll(ignoreCase + "%maxhealth%", Double.toString(player.getMaxHealth()))
                .replaceAll(ignoreCase + "%hunger%", Integer.toString(player.getFoodLevel()))
                .replaceAll(ignoreCase + "%xp%", Integer.toString(player.getTotalExperience()))
                .replaceAll(ignoreCase + "%players%", Integer.toString(Bukkit.getServer().getOnlinePlayers().size()))
                .replaceAll(ignoreCase + "%worldplayers%", Integer.toString(player.getWorld().getPlayers().size()))
                .replaceAll(ignoreCase + "%worldtype%", player.getWorld().getWorldType().getName().toLowerCase())
                .replaceAll(ignoreCase + "%weathertime%", Integer.toString(player.getWorld().getWeatherDuration() / 20))
                .replaceAll(ignoreCase + "%version%", Bukkit.getBukkitVersion())
                .replaceAll(ignoreCase + "%worldtime%", Long.toString(player.getWorld().getTime()))
                .replaceAll(ignoreCase + "%servertime%", getCurrentTime());
        if (WorldUtil.getNearestPlayer(player) != null) {
            replaced = replaced.replaceAll(ignoreCase + "%player%", WorldUtil.getNearestPlayer(player).getName());
        } else {
            replaced = replaced.replaceAll(ignoreCase + "%player%", Message.CHAT_STRINGS_NO_PLAYER.toString());
        }
        return replaced;
    }

    /**
     * Returns a string with sign variables
     * @param string the string to edit
     * @param sign the sign
     * @param player the player
     * @return the string with variables replaced
     */
    public static String replaceSignVariables(String string, Sign sign, Player player) {
        return string
                .replaceAll("%line2%", sign.getLine(1))
                .replaceAll("%line3%", sign.getLine(2))
                .replaceAll("%line4%", sign.getLine(3))
                .replaceAll("%player%", player.getName());
    }
    
    /**
     * Returns a string with formatting codes
     * @param string the string to format
     * @return the string with formatting codes
     */
    public static String formatText(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String upperCaseFirst(String string) {
        String[] words = string.split(" ");
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < words.length; i++) {
            sb.append(Character.toUpperCase(words[i].charAt(0)));
            sb.append(words[i].substring(1));
            if(i < words.length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }
}