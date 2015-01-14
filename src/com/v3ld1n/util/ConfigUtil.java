package com.v3ld1n.util;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.Config;
import com.v3ld1n.ConfigSetting;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.commands.Report;

public class ConfigUtil {
    private ConfigUtil() {
    }

    /**
     * Sets the header and footer on the player list
     * @param header JSON header
     * @param footer JSON footer
     */
    public static void setPlayerListHeaderFooter(String header, String footer) {
        ConfigSetting.PLAYER_LIST_HEADER.setValue(header);
        ConfigSetting.PLAYER_LIST_FOOTER.setValue(footer);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            PlayerUtil.sendPlayerListHeaderFooter(p, header, footer);
        }
    }

    /**
     * Sets the "fake" max players on the server list
     * @param maxPlayers the max player count
     */
    public static void setServerListMaxPlayers(int maxPlayers) {
        ConfigSetting.SERVER_LIST_MAX_PLAYERS.setValue(maxPlayers);
    }

    /**
     * Toggles a boolean config setting
     * @param setting the setting to toggle
     */
    public static void toggleSetting(ConfigSetting setting) {
        if (!setting.getBoolean()) {
            setting.setValue(true);
        } else {
            setting.setValue(null);
        }
    }

    /**
     * Returns the amount of reports a player has not read
     * @param uuid the player's UUID
     * @return the amount of unread reports
     */
    public static int getUnreadReports(UUID uuid) {
        int unreadReports = 0;
        for (Report report : V3LD1N.getReports()) {
            if (!report.isReadBy(uuid)) {
                unreadReports++;
            }
        }
        return unreadReports;
    }

    /**
     * Returns the text of a custom sign
     * @param sign the config name of the sign
     * @return the text of the sign
     */
    public static String getSignText(String sign) {
        return Config.SIGNS.getConfig().getString(sign + ".text");
    }

    /**
     * Returns the player commands of a custom sign
     * @param sign the config name of the sign
     * @return the player commands of the sign
     */
    public static List<String> getSignPlayerCommands(String sign) {
        return Config.SIGNS.getConfig().getStringList(sign + ".player-commands");
    }

    /**
     * Returns the console commands of a custom sign
     * @param sign the config name of the sign
     * @return the console commands of the sign
     */
    public static List<String> getSignConsoleCommands(String sign) {
        return Config.SIGNS.getConfig().getStringList(sign + ".console-commands");
    }

    /**
     * Returns the click sound of a custom sign
     * @param sign the config name of the sign
     * @return the click sound of the sign
     */
    public static String getSignSound(String sign) {
        if (Config.SIGNS.getConfig().getString(sign + ".sound") != null) {
            return Config.SIGNS.getConfig().getString(sign + ".sound");
        }
        return null;
    }

    /**
     * Returns the click sound of a custom sign
     * @param sign the config name of the sign
     * @return the click sound of the sign
     */
    public static String getSignSoundName(String sign) {
        if (Config.SIGNS.getConfig().getString(sign + ".sound") != null) {
            return Config.SIGNS.getConfig().getString(sign + ".sound").split("\\|")[0];
        }
        return null;
    }

    /**
     * Returns the click particle of a custom sign
     * @param sign the config name of the sign
     * @return the click particle of the sign
     */
    public static String getSignParticle(String sign) {
        if (Config.SIGNS.getConfig().getString(sign + ".particle") != null) {
            return Config.SIGNS.getConfig().getString(sign + ".particle");
        }
        return null;
    }

    /**
     * Returns the name of the click particle of a custom sign
     * @param sign the config name of the sign
     * @return the name of the particle
     */
    public static String getSignParticleName(String sign) {
        if (Config.SIGNS.getConfig().getString(sign + ".particle") != null) {
            return Config.SIGNS.getConfig().getString(sign + ".particle").split("\\|")[0];
        }
        return null;
    }

    /**
     * Returns an item stack from a config string
     * @param configSetting the string
     * @return an item stack
     */
    public static ItemStack itemFromString(String configSetting) {
        String[] split = configSetting.split("\\|");
        ItemStack item = new ItemStack(Material.valueOf(split[0].toUpperCase()), 1);
        if (split.length >= 2) {
            item.setAmount(Integer.parseInt(split[1]));
            if (split.length >= 3) {
                item.setDurability(Short.parseShort(split[2]));
            }
        }
        return item;
    }

    /**
     * Returns a location from a config string
     * @param configSetting the string
     * @return a location
     */
    public static Location locationFromString(String configSetting) {
        String[] split = configSetting.split("\\|");
        String world = split[0];
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        Location location = new Location(Bukkit.getServer().getWorld(world), x, y, z);
        if (split.length >= 5) {
            location.setYaw(Float.parseFloat(split[4]));
            if (split.length >= 6) {
                location.setPitch(Float.parseFloat(split[5]));
            }
        }
        return location;
    }
}