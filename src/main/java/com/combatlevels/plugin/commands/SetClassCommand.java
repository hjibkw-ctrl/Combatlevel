package com.combatlevels.plugin.commands;

import com.combatlevels.plugin.classes.CombatClass;
import com.combatlevels.plugin.data.PlayerData;
import com.combatlevels.plugin.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetClassCommand implements CommandExecutor {

    private final PlayerDataManager dataManager;

    public SetClassCommand(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("combatlevels.admin")) {
            sender.sendMessage("§cهذا الأمر خاص بالأونرز فقط.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§cالاستخدام: /" + label + " <player> <sword|mace|tnt_cart>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("§cما لقيت لاعب بهذا الاسم متصل بالسيرفر حالياً.");
            return true;
        }

        CombatClass newClass;
        try {
            newClass = CombatClass.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cاسم كلاس غير صحيح. الخيارات: sword, mace, tnt_cart");
            return true;
        }

        if (!newClass.isSupportedOnThisServer()) {
            sender.sendMessage("§cهذا الكلاس غير مدعوم بنسختك الحالية من ماين كرافت.");
            return true;
        }

        PlayerData data = dataManager.getOrCreate(target.getUniqueId());
        data.setCombatClass(newClass);

        sender.sendMessage("§a✔ تم تغيير كلاس " + target.getName() + " إلى " + newClass.getDisplayName());
        target.sendMessage("§a✔ الأونر غيّر أسلوبك القتالي إلى: " + newClass.getDisplayName());

        return true;
    }
}
