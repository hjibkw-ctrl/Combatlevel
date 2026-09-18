package com.combatlevels.plugin.listeners;

import com.combatlevels.plugin.classes.CombatClass;
import com.combatlevels.plugin.data.PlayerData;
import com.combatlevels.plugin.data.PlayerDataManager;
import com.combatlevels.plugin.util.FreezeUtil;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatListener implements Listener {

    private static final String META_TNT_OWNER = "cl_tnt_owner";
    private static final String META_TOTEM_BROKEN = "cl_totem_broken_until";

    private static final long MACE_FREEZE_COOLDOWN_MILLIS = 3 * 60 * 1000L;
    private static final int MACE_FREEZE_DURATION_TICKS = 20;
    private static final long TOTEM_BREAK_DURATION_MILLIS = 60 * 1000L;
    private static final long TNT_PLACEMENT_MATCH_WINDOW_MILLIS = 3000L;

    private final JavaPlugin plugin;
    private final PlayerDataManager dataManager;

    private final Map<UUID, Long> pendingTntCartPlacement = new HashMap<>();

    public CombatListener(JavaPlugin plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player victim) {
            PlayerData victimData = dataManager.getOrCreate(victim.getUniqueId());
            if (victimData.getCombatClass() == CombatClass.SWORD) {
                victimData.resetSwordStreak();
            }
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            handleTntCartExplosion(event);
            return;
        }

        if (!(event.getDamager() instanceof Player attacker)) return;

        PlayerData attackerData = dataManager.getOrCreate(attacker.getUniqueId());
        if (attackerData.getCombatClass() == null || !attackerData.isBeginnerPerkUnlocked()) return;

        switch (attackerData.getCombatClass()) {
            case SWORD -> handleSwordHit(event, attacker, attackerData);
            case MACE -> handleMaceHit(event, attacker, attackerData);
            case TNT_CART -> { }
        }
    }

    private void handleSwordHit(EntityDamageByEntityEvent event, Player attacker, PlayerData attackerData) {
        if (!(event.getEntity() instanceof Player victim)) return;

        int streak = attackerData.registerSwordHit(victim.getUniqueId());
        if (streak >= 3) {
            event.setDamage(event.getDamage() * 1.5);

            victim.getWorld().spawnParticle(Particle.CRIT, victim.getLocation().add(0, 1, 0), 30, 0.3, 0.5, 0.3, 0.1);

            // نستخدم world.playSound بدل player.playSound عشان الصوت يوصل لأي حد قريب،
            // مو بس للمهاجم لوحده (packet خاص). كذا نضمن إنه مسموع فعلياً.
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.2f, 1.0f);
            victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.8f, 1.6f);

            attacker.sendMessage("§c⚔ كريتكل مضاعف! (3 ضربات متتالية)");
            attackerData.resetSwordStreak();
        }
    }

    private void handleMaceHit(EntityDamageByEntityEvent event, Player attacker, PlayerData attackerData) {
        long now = System.currentTimeMillis();
        if (now - attackerData.getMaceLastFreezeMillis() < MACE_FREEZE_COOLDOWN_MILLIS) return;

        if (!(event.getEntity() instanceof Player victim)) return;

        attackerData.setMaceLastFreezeMillis(now);
        FreezeUtil.freeze(plugin, victim, MACE_FREEZE_DURATION_TICKS);

        // إيفكت بصري وصوتي واضح للتجميد (كان ناقص تماماً بالكود القديم)
        victim.getWorld().spawnParticle(Particle.SNOWFLAKE, victim.getLocation().add(0, 1, 0), 40, 0.4, 0.6, 0.4, 0.02);
        victim.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, victim.getLocation().add(0, 1, 0), 15, 0.3, 0.5, 0.3);
        victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_POWDER_SNOW_HIT, 1.2f, 0.8f);
        victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.6f, 1.4f);

        victim.sendMessage("§b❄ تجمدت لمدة ثانية بسبب ضربة الميس!");
        attacker.sendMessage("§6✔ فعّلت تجميد الميس (كولداون 3 دقائق)");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;
        if (!item.getType().name().equals("TNT_MINECART")) return;

        pendingTntCartPlacement.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (!vehicle.getType().name().equals("MINECART_TNT")) return;

        long now = System.currentTimeMillis();
        UUID bestMatch = null;
        long bestTime = -1;

        pendingTntCartPlacement.entrySet().removeIf(e -> now - e.getValue() > TNT_PLACEMENT_MATCH_WINDOW_MILLIS);
        for (Map.Entry<UUID, Long> entry : pendingTntCartPlacement.entrySet()) {
            if (entry.getValue() > bestTime) {
                bestTime = entry.getValue();
                bestMatch = entry.getKey();
            }
        }

        if (bestMatch != null) {
            vehicle.setMetadata(META_TNT_OWNER, new FixedMetadataValue(plugin, bestMatch.toString()));
            pendingTntCartPlacement.remove(bestMatch);
        }
    }

    private void handleTntCartExplosion(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!damager.getType().name().equals("MINECART_TNT")) return;
        if (!damager.hasMetadata(META_TNT_OWNER)) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        UUID ownerUuid;
        try {
            ownerUuid = UUID.fromString(damager.getMetadata(META_TNT_OWNER).get(0).asString());
        } catch (IllegalArgumentException e) {
            return;
        }

        PlayerData ownerData = dataManager.getOrCreate(ownerUuid);
        if (ownerData.getCombatClass() != CombatClass.TNT_CART || !ownerData.isBeginnerPerkUnlocked()) return;

        if (victim.getUniqueId().equals(ownerUuid)) {
            event.setCancelled(true);
            return;
        }

        long expiry = System.currentTimeMillis() + TOTEM_BREAK_DURATION_MILLIS;
        victim.setMetadata(META_TOTEM_BROKEN, new FixedMetadataValue(plugin, expiry));
        victim.sendMessage("§c⚠ انكسر تأثير التوتم لديك لمدة دقيقة بسبب انفجار عربة التنت!");
    }

    @EventHandler
    public void onResurrect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.hasMetadata(META_TOTEM_BROKEN)) return;

        long expiry = player.getMetadata(META_TOTEM_BROKEN).get(0).asLong();
        if (System.currentTimeMillis() < expiry) {
            event.setCancelled(true);
            player.sendMessage("§c✖ التوتم لم يشتغل بسبب تأثير عربة التنت!");
        } else {
            player.removeMetadata(META_TOTEM_BROKEN, plugin);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;

        Player killer = victim.getKiller();

        if (killer == null) {
            EntityDamageEvent lastDamage = victim.getLastDamageCause();
            if (lastDamage instanceof EntityDamageByEntityEvent lastDamageByEntity) {
                Entity damager = lastDamageByEntity.getDamager();
                if (damager.getType().name().equals("MINECART_TNT") && damager.hasMetadata(META_TNT_OWNER)) {
                    try {
                        UUID ownerUuid = UUID.fromString(damager.getMetadata(META_TNT_OWNER).get(0).asString());
                        if (plugin.getServer().getPlayer(ownerUuid) != null) {
                            killer = plugin.getServer().getPlayer(ownerUuid);
                        }
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }

        if (killer == null) return;

        PlayerData killerData = dataManager.getOrCreate(killer.getUniqueId());
        boolean wasLocked = !killerData.isBeginnerPerkUnlocked();
        killerData.addKill();

        if (wasLocked && killerData.isBeginnerPerkUnlocked() && killerData.getCombatClass() != null) {
            killer.sendMessage("§a★ فتحت ميزة المبتدئ الخاصة بـ" + killerData.getCombatClass().getDisplayName() + " بعد أول قتلة!");
            killer.playSound(killer.getLocation(), Sound.ITEM_TOTEM_USE, 1f, 1.2f);
        }
    }
            }
