package com.combatlevels.plugin.classes;

import org.bukkit.Material;

public enum CombatClass {

    MACE(
            "§6الميس",
            "MACE",
            "§7ضربات ثقيلة تعتمد على السيطرة بالساحة.",
            new String[]{
                    "§eميزة المبتدئ §7(تفتح بعد أول قتلة):",
                    "§f- عند ضرب أي كائن، وكل §b3 دقائق§f،",
                    "§f  يتجمد اللاعب المضروب لمدة §bثانية واحدة§f."
            }
    ),

    SWORD(
            "§bالسيف",
            "SWORD",
            "§7دقة وسرعة، يعاقب الخصم اللي ما يقدر يرد.",
            new String[]{
                    "§eميزة المبتدئ §7(تفتح بعد أول قتلة):",
                    "§f- إذا ضربت نفس اللاعب §b3 ضربات متتالية§f",
                    "§f  بدون ما تنضرب أي ضربة بينهم،",
                    "§f  الضربة الثالثة تصير §cكريتكل§f تلقائياً",
                    "§f  حتى لو ما قفزت."
            }
    ),

    TNT_CART(
            "§cعربة التنت",
            "TNT_MINECART",
            "§7تفخيخ وتفجير متحكم به.",
            new String[]{
                    "§eميزة المبتدئ §7(تفتح بعد أول قتلة):",
                    "§f- تفجير عربة التنت اللي تحطها ما راح",
                    "§f  يضرك أنت، بس يضر العدو.",
                    "§f- أي لاعب ينضرب من التفجير، §cالتوتم§f",
                    "§f  ما راح يشتغل عنده لمدة §bدقيقة كاملة§f."
            }
    );

    private final String displayName;
    private final String materialName;
    private final String description;
    private final String[] perksLore;

    CombatClass(String displayName, String materialName, String description, String[] perksLore) {
        this.displayName = displayName;
        this.materialName = materialName;
        this.description = description;
        this.perksLore = perksLore;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String[] getPerksLore() {
        return perksLore;
    }

    public Material getIconMaterial() {
        Material mat = Material.matchMaterial(materialName);
        if (mat != null) return mat;
        switch (this) {
            case MACE: return Material.STICK;
            case SWORD: return Material.IRON_SWORD;
            case TNT_CART: return Material.TNT;
            default: return Material.STICK;
        }
    }

    public boolean isSupportedOnThisServer() {
        return Material.matchMaterial(materialName) != null;
    }
}
