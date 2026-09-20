package com.combatlevels.plugin.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BiggestCartUtil {

    private static NamespacedKey KEY;

    /**
     * لازم ينادى مرة وحدة بس، من onEnable() بالكلاس الرئيسي، قبل أي استخدام ثاني.
     */
    public static void init(JavaPlugin plugin) {
        KEY = new NamespacedKey(plugin, "biggest_cart");
    }

    public static boolean isBiggestCart(ItemStack item) {
        if (item == null || item.getType() != Material.TNT_MINECART) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(KEY, PersistentDataType.BYTE);
    }

    /**
     * ينشئ نسخة عربة تنت مطورة: نفس المادة العادية، بس معلّمة بتاغ خاص
     * ومزوّدة بلمعان Loyalty (بدون ما يظهر بالتولتيب) للتفريق البصري.
     */
    public static ItemStack createBiggestCart() {
        ItemStack item = new ItemStack(Material.TNT_MINECART);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§6§lذا بيقست كارت §c(The Biggest Cart)");

            List<String> lore = new ArrayList<>();
            lore.add("§7عربة تنت مطورة، انفجارها §c×2§7 من العادية.");
            meta.setLore(lore);

            // إنشانت للمعان بصري بس (Loyalty)، مخفي عن التولتيب عشان ما يبين اسمه
            meta.addEnchant(Enchantment.LOYALTY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            meta.getPersistentDataContainer().set(KEY, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
        }

        return item;
    }
}
