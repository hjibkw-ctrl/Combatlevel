package com.combatlevels.plugin.classes;

import org.bukkit.Material;

public enum CombatClass {

    MACE(
            "§6الميس",
            new String[]{"MACE", "STICK"},
            "§7ضربات ثقيلة تعتمد على السيطرة بالساحة.",
            new String[]{
                    "§eميزة المبتدئ §7(تفتح بعد أول قتلة):",
                    "§f- عند ضرب أي كائن، وكل §b3 دقائق§f،",
                    "§f  يتجمد اللاعب المضروب لمدة §bثانية واحدة§f."
            }
    ),

    SWORD(
            "§bالسيف",
            new String[]{
                    "NETHERITE_SWORD",
                    "DIAMOND_SWORD",
                    "IRON_SWORD",
                    "STONE_SWORD",
                    "GOLDEN_SWORD",
                    "WOODEN_SWORD"
            },
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
            new String[]{"TNT_MINECART", "MINECART_TNT", "TNT"},
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
    private final String[] materialCandidates;
    private final String description;
    private final String[] perksLore;

    CombatClass(String displayName, String[] materialCandidates, String description, String[] perksLore) {
        this.displayName = displayName;
        this.materialCandidates = materialCandidates;
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

    /**
     * يجرب كل الأسماء بالترتيب ويرجع أول مادة موجودة فعلياً بهذي النسخة من ماين كرافت.
     * لو ما لقى أي وحدة منهم، يرجع STICK كحل احتياطي أخير عشان الآيتم ما يختفي من الـ GUI.
     */
    public Material getIconMaterial() {
        for (String name : materialCandidates) {
            Material mat = Material.matchMaterial(name);
            if (mat != null) return mat;
        }
        return Material.STICK;
    }

    /**
     * الكلاس مدعوم إذا لقينا ولو مادة وحدة من قائمة البدائل موجودة بهذي النسخة.
     */
    public boolean isSupportedOnThisServer() {
        for (String name : materialCandidates) {
            if (Material.matchMaterial(name) != null) return true;
        }
        return false;
    }
}
