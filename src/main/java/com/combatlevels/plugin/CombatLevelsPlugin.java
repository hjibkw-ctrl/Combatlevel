package com.combatlevels.plugin;

import com.combatlevels.plugin.classes.CombatClass;
import com.combatlevels.plugin.data.PlayerData;
import com.combatlevels.plugin.data.PlayerDataManager;
import com.combatlevels.plugin.gui.ClassSelectionGUI;
import com.combatlevels.plugin.listeners.CombatListener;
import com.combatlevels.plugin.listeners.GUIClickListener;
import com.combatlevels.plugin.listeners.PlayerJoinListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatLevelsPlugin extends JavaPlugin implements CommandExecutor {

    private PlayerDataManager dataManager;

    @Override
    public void onEnable() {
        this.dataManager = new PlayerDataManager(this);
        dataManager.loadAll();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, dataManager), this);
        getServer().getPluginManager().registerEvents(new GUIClickListener(dataManager), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this, dataManager), this);

        if (getCommand("chooseclass") != null) getCommand("chooseclass").setExecutor(this);
        if (getCommand("combatlevel") != null) getCommand("combatlevel").setExecutor(this);

        getLogger().info("CombatLevels تفعّل بنجاح! (كلاسات: سيف / ميس / عربة تنت)");
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveAll();
        }
        getLogger().info("CombatLevels توقف - تم حفظ بيانات اللاعبين.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName().toLowerCase()) {
            case "chooseclass" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("هذا الأمر للاعبين داخل السيرفر فقط.");
                    return true;
                }
                player.openInventory(ClassSelectionGUI.build());
                return true;
            }
            case "combatlevel" -> {
                Player target;
                if (args.length > 0) {
                    target = getServer().getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage("§cاللاعب غير متصل أو غير موجود.");
                        return true;
                    }
                } else if (sender instanceof Player player) {
                    target = player;
                } else {
                    sender.sendMessage("§cحدد اسم لاعب: /combatlevel <player>");
                    return true;
                }

                PlayerData data = dataManager.getOrCreate(target.getUniqueId());
                sender.sendMessage("§6§l— معلومات " + target.getName() + " القتالية —");
                if (!data.hasChosenClass()) {
                    sender.sendMessage("§7لم يختر أسلوب قتال بعد.");
                } else {
                    CombatClass c = data.getCombatClass();
                    sender.sendMessage("§7الأسلوب: " + c.getDisplayName());
                    sender.sendMessage("§7عدد القتلات: §f" + data.getKills());
                    sender.sendMessage("§7ميزة المبتدئ: " + (data.isBeginnerPerkUnlocked() ? "§aمفتوحة" : "§cمقفلة (اقتل لاعباً واحداً)"));
                }
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
