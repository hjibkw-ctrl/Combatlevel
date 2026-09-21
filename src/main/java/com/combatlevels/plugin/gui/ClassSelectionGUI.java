package com.combatlevels.plugin.gui;

import com.combatlevels.plugin.classes.CombatClass;
import com.combatlevels.plugin.util.TextUtil;
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

        fillBorder(inv);

        inv.setItem(SLOT_SWORD, createClassItem(CombatClass.SWORD));
        inv.setItem(SLOT_MACE, createClassItem(CombatClass.MACE));
        inv.setItem(SLOT_TNT_CART, createClassItem(CombatClass.TNT_CART));

        return inv;
    }

    /**
     * يعبي كل السلوتات الفاضية بزجاج ملون (أزرق فاتح يمين، أحمر يسار) كإطار زخرفي،
     * ويسيب سلوتات الكلاسات فاضية عشان تنحط فيها الأيقونات بعدين.
     */
    private static void fillBorder(Inventory inv) {
        ItemStack blueFiller = createFiller(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemStack redFiller = createFiller(Material.RED_STAINED_GLASS_PANE);

        for (int slot = 0; slot < SIZE; slot++) {
            if (slot == SLOT_SWORD || slot == SLOT_MACE || slot == SLOT_TNT_CART) continue;

            // نص الصف الأول = أزرق، النص الثاني = أحمر (نفس تقسيم الصورة المرجعية)
            int column = slot % 9;
            ItemStack filler = (column < 4) ? blueFiller : redFiller;
            inv.setItem(slot, filler);
        }
    }

    private static ItemStack createFiller(Material material) {
        ItemStack filler = new ItemStack(material);
        ItemMeta meta = filler.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            filler.setItemMeta(meta);
        }
        return filler;
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

            // عنوان القدرة: الاسم الإنجليزي بتأثير قوس قزح + مقدمة عربية بلون ذهبي
            lore.add(TextUtil.rainbow(combatClass.getAbilityEnglishName()) + " §6§l: §6" + combatClass.getAbilityIntroArabic());
            lore.add("");

            lore.add("§e§lLevel 1");
            for (String line : combatClass.getLevel1Lines()) {
                lore.add("§e" + line);
            }
            lore.add("");

            lore.add("§9§lLevel 3");
            for (String line : combatClass.getLevel3Lines()) {
                lore.add("§9" + line);
            }
            lore.add("");

            lore.add("§f§lLevel 5");
            for (String line : combatClass.getLevel5Lines()) {
                lore.add("§f" + line);
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

    public static CombatClass classFromSlot(int slot) {
        if (slot == SLOT_SWORD) return CombatClass.SWORD;
        if (slot == SLOT_MACE) return CombatClass.MACE;
        if (slot == SLOT_TNT_CART) return CombatClass.TNT_CART;
        return null;
    }
}
