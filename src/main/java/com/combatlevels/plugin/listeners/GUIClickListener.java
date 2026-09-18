package com.combatlevels.plugin.listeners;

import com.combatlevels.plugin.classes.CombatClass;
import com.combatlevels.plugin.data.PlayerData;
import com.combatlevels.plugin.data.PlayerDataManager;
import com.combatlevels.plugin.gui.ClassSelectionGUI;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIClickListener implements Listener {

    private final PlayerDataManager dataManager;

    public GUIClickListener(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ClassSelectionGUI.GUI_TITLE)) return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().equals(event.getView().getTopInventory())) return;

        CombatClass chosen = ClassSelectionGUI.classFromSlot(event.getSlot());
        if (chosen == null) return;

        if (!chosen.isSupportedOnThisServer()) {
            player.sendMessage("§cهذا الأسلوب غير مدعوم بنسختك الحالية من ماين كرافت!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        PlayerData data = dataManager.getOrCreate(player.getUniqueId());

        if (data.hasChosenClass()) {
            player.sendMessage("§cأنت اخترت أسلوب قتالك مسبقاً: " + data.getCombatClass().getDisplayName());
            player.closeInventory();
            return;
        }

        data.setCombatClass(chosen);
        player.closeInventory();
        player.sendMessage("§a✔ تم اختيار أسلوبك القتالي: " + chosen.getDisplayName());
        player.sendMessage("§7اقتل لاعباً واحداً لتفتح ميزة المبتدئ الخاصة بأسلوبك!");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.4f);
    }
}
