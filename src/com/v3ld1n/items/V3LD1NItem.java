package com.v3ld1n.items;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.v3ld1n.Config;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.StringUtil;

public class V3LD1NItem implements Listener {
    final String id;
    Material material;
    String name;
    protected static final Random random = new Random();

    public V3LD1NItem(String id) {
        this.id = id;
        this.setName(this.getStringSetting("name"));
        this.setMaterial(Material.valueOf(this.getStringSetting("item")));
    }

    public String getId() {
        return id;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equalsItem(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName() != null) {
            if (item.getType() == material && item.getItemMeta().getDisplayName().equals(StringUtil.formatText(name))) {
                return true;
            }
        }
        return false;
    }

    public String getStringSetting(String settingName) {
        return Config.ITEMS.getConfig().getString(id + "." + settingName);
    }

    public int getIntSetting(String settingName) {
        return Config.ITEMS.getConfig().getInt(id + "." + settingName);
    }

    public double getDoubleSetting(String settingName) {
        return Config.ITEMS.getConfig().getDouble(id + "." + settingName);
    }

    public boolean getBooleanSetting(String settingName) {
        return Config.ITEMS.getConfig().getBoolean(id + "." + settingName);
    }

    public Location getLocationSetting(String settingName) {
        String setting = Config.ITEMS.getConfig().getString(id + "." + settingName);
        return ConfigUtil.locationFromString(setting);
    }

    public Vector getVectorSetting(String settingName) {
        return Config.ITEMS.getConfig().getVector(id + "." + settingName);
    }

    public Particle getParticleSetting(String settingName) {
        return Particle.fromString(Config.ITEMS.getConfig().getString(id + "." + settingName));
    }

    @Override
    public String toString() {
        return id + "(" + material + ", " + name + ")";
    }
}