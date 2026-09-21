package com.combatlevels.plugin.tasks;

import com.combatlevels.plugin.classes.CombatClass;
import com.combatlevels.plugin.data.PlayerData;
import com.combatlevels.plugin.data.PlayerDataManager;
import com.combatlevels.plugin.util.BiggestCartUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class BiggestCartTask extends BukkitRunnable {

    // ⚠️ مؤقت للتجربة بس: 10 ثواني بدل 10 دقايق.
    // بعد ما تخلص الاختبار، رجّعها هيك: private static final long INTERVAL_MILLIS = 10 * 60 * 1000L;
    private static final long INTERVAL_MILLIS = 10 * 1000L;

    private final PlayerDataManager dataManager;

    public BiggestCartTask(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = dataManager.getOrCreate(player.getUniqueId());

            if (data.getCombatClass() != CombatClass.TNT_CART) continue;
            if (!data.isBeginnerPerkUnlocked()) continue;
            if (data.getLastBiggestCartMillis() == 0L) continue;
            if (now - data.getLastBiggestCartMillis() < INTERVAL_MILLIS) continue;

            PlayerInventory inv = player.getInventory();
            int slot = findNormalCartSlot(inv);

            if (slot == -1) {
                player.sendMessage("§cانت لا تحمل ماين كارت في الانفنتوري الخاص بك فا لن تحصل على كارت مطورة!");
            } else {
                ItemStack stack = inv.getItem(slot);
                if (stack.getAmount() > 1) {
                    stack.setAmount(stack.getAmount() - 1);
                } else {
                    inv.setItem(slot, null);
                }

                inv.addItem(BiggestCartUtil.createBiggestCart());

                player.sendMessage("§6✔ إحدى عربات التنت عندك تطورت إلى §lThe Biggest Cart§r§6!");
                player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1f, 0.7f);
            }

            // بكل الحالتين (نجح أو فشل)، التايمر يعيد ضبط نفسه لعشر دقايق جديدة
            data.setLastBiggestCartMillis(now);
        }
    }

    private int findNormalCartSlot(PlayerInventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack != null && stack.getType() == Material.TNT_MINECART && !BiggestCartUtil.isBiggestCart(stack)) {
                return i;
            }
        }
        return -1;
    }
}
