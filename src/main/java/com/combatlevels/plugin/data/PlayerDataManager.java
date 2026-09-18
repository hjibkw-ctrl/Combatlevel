package com.combatlevels.plugin.data;

import com.combatlevels.plugin.classes.CombatClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerDataManager {

    private final JavaPlugin plugin;
    private final Map<UUID, PlayerData> dataMap = new HashMap<>();
    private final File file;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "players.yml");
    }

    public PlayerData getOrCreate(UUID uuid) {
        return dataMap.computeIfAbsent(uuid, PlayerData::new);
    }

    public boolean hasData(UUID uuid) {
        return dataMap.containsKey(uuid);
    }

    public void loadAll() {
        if (!file.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.getConfigurationSection("players") == null) return;

        for (String key : config.getConfigurationSection("players").getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                PlayerData data = new PlayerData(uuid);

                String classStr = config.getString("players." + key + ".class");
                if (classStr != null) {
                    try {
                        data.setCombatClass(CombatClass.valueOf(classStr));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                data.addKillsRaw(config.getInt("players." + key + ".kills", 0));

                dataMap.put(uuid, data);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().log(Level.WARNING, "UUID غير صالح بملف players.yml: " + key);
            }
        }
    }

    public void saveAll() {
        FileConfiguration config = new YamlConfiguration();

        for (PlayerData data : dataMap.values()) {
            String path = "players." + data.getUuid();
            if (data.getCombatClass() != null) {
                config.set(path + ".class", data.getCombatClass().name());
            }
            config.set(path + ".kills", data.getKills());
        }

        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "فشل حفظ players.yml", e);
        }
    }
}
