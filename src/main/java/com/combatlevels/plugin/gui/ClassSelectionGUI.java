package com.combatlevels.plugin.gui;

import com.combatlevels.plugin.classes.CombatClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClassSelectionGUI {

    public static final String GUI_TITLE = "\u00A76\u2726 \u00A7lاختر أسلوب قتالك \u00A76\u2726";
    private static final int SIZE = 27;

    private static final int SLOT_SWORD = 11;
    private static final int SLOT_MACE = 13;
    private static final int SLOT_TNT_CART = 15;

    public static Inventory build() {
        Inventory inv = Bukkit.createInventory(null, SIZE, GUI_TITLE);

        inv.setItem(SLOT_SWORD, createClassItem(CombatClass.SWORD));
        inv.setItem(SLOT_MACE, createClassItem(CombatClass.MACE));
        inv.setItem(SLOT_TNT_CART, createClassItem(CombatClass.TNT_CART));

        return inv;
    }

    public static void open(Player player) {
        player.openInventory(build());
    }

    private static ItemStack createClassItem(CombatClass combatClass) {
        ItemStack item = new ItemStack(combatClass.getIconMaterial());
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(combatClass.getDisplayName());

            List<String> lore = new ArrayList<>();
            lore.add(combatClass.getDescription());
            lore.add("");
            for (String line : combatClass.getPerksLore()) {
                lore.add(line);
            }

            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static int getSlotSword() {
        return SLOT_SWORD;
    }

    public static int getSlotMace() {
        return SLOT_MACE;
    }

    public static int getSlotTntCart() {
        return SLOT_TNT_CART;
    }
}
