package com.spectrasonic.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class SoundUtils {

    public static void playerSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player, sound, SoundCategory.MASTER, volume, pitch);
    }

    public static void broadcastSound(Sound sound, float volume, float pitch) {
        Bukkit.getOnlinePlayers()
                .forEach(player -> player.playSound(player, sound, SoundCategory.MASTER, volume, pitch));
    }

    public static void CustomSound(Player player, String sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void broadcastCustomSound(String sound, float volume, float pitch) {
        Bukkit.getOnlinePlayers()
                .forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
    }
}
