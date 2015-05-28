package com.v3ld1n.util;

import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Sound {
    private String name;
    private float volume;
    private float pitch;
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private float volume;
        private float pitch;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setVolume(float volume) {
            this.volume = volume;
            return this;
        }

        public Builder setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public Sound build() {
            return new Sound(this);
        }
    }

    private Sound(Builder builder) {
        this.name = builder.name;
        this.volume = builder.volume;
        this.pitch = builder.pitch;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void play(Location location) {
        for (Player p : location.getWorld().getPlayers()) {
            play(location, p);
        }
    }

    public void play(Location location, Player player) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(name, x, y, z, volume, pitch);
        PlayerUtil.sendPacket(packet, player);
    }

    public static Sound fromString(String sound) {
        String[] split = sound.split("\\|");
        Builder builder = builder()
                .setName(split[0]);
        builder.setVolume(1);
        builder.setPitch(1);
        if (split.length >= 2) {
            builder.setVolume(Float.parseFloat(split[1]));
            if (split.length >= 3) {
                builder.setPitch(Float.parseFloat(split[2]));
            }
        }
        return builder.build();
    }

    @Override
    public String toString() {
        String string = name + "|" + volume + "|" + pitch;
        return string;
    }
}