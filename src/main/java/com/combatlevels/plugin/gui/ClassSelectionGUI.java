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
