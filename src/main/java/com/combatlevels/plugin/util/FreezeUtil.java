package com.combatlevels.plugin.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FreezeUtil {

    public static void freeze(JavaPlugin plugin, Player player, int durationTicks) {
        if (player == null || !player.isOnline()) return;

        final Location freezeLoc = player.getLocation().clone();

        new BukkitRunnable() {
            int ticksLeft = durationTicks;

            @Override
            public void run() {
                if (!player.isOnline() || ticksLeft <= 0) {
                    this.cancel();
                    return;
                }
                Location current = player.getLocation();
                Location fixed = freezeLoc.clone();
                fixed.setYaw(current.getYaw());
                fixed.setPitch(current.getPitch());

                player.teleport(fixed);
                player.setVelocity(new Vector(0, 0, 0));
                player.setFallDistance(0f);

                ticksLeft--;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
