package com.combatlevels.plugin.classes;

import org.bukkit.Material;

public enum CombatClass {

    MACE(
            "§6الميس",
            new String[]{"MACE", "STICK"},
            "(Mace Ability)",
            "قدرة الميس!",
            new String[]{
                    "بعد قتل اول لاعب ستفتح لك ميزة الستن (التجميد).",
                    "كل 3 دقايق يوصلك إشعار إن القدرة تفعّلت،",
                    "وأي ضربة بعدها تكون ستان: تجمّد الخصم لمدة ثانية."
            },
            new String[]{"قيد تطوير..."},
            new String[]{"قيد تطوير..."}
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
            "(Sword Ability)",
            "قدرة سيف!",
            new String[]{
                    "بعد قتل اول لاعب ستفتح لك ميزة الكريتكال التلقائي.",
                    "يعني ما تحتاج تقفز لضرب كريتكال، لمدة 10 ثواني",
                    "كل ضرباتك كريتكال تلقائياً، وتنلغى لو انضربت",
                    "أو انتهت المدة."
            },
            new String[]{"قيد تطوير..."},
            new String[]{"قيد تطوير..."}
    ),

    TNT_CART(
            "§cعربة التنت",
            new String[]{"TNT_MINECART", "MINECART_TNT", "TNT"},
            "(T-Cart Ability)",
            "قدرة الماينكارت!",
            new String[]{
                    "بعد قتل اول لاعب تفتح ميزة §fThe Biggest Cart§e.",
                    "كل 10 دقايق، عربة تنت عادية بإنفنتوريك",
                    "تتحول لعربة مطورة، انفجارها أقوى ×2 من العادية.",
                    "لو ما تحمل عربة وقتها، التايمر يرجع للصفر."
            },
            new String[]{"قيد تطوير..."},
            new String[]{"قيد تطوير..."}
    );

    private final String displayName;
    private final String[] materialCandidates;
    private final String abilityEnglishName;
    private final String abilityIntroArabic;
    private final String[] level1Lines;
    private final String[] level3Lines;
    private final String[] level5Lines;

    CombatClass(String displayName, String[] materialCandidates,
                String abilityEnglishName, String abilityIntroArabic,
                String[] level1Lines, String[] level3Lines, String[] level5Lines) {
        this.displayName = displayName;
        this.materialCandidates = materialCandidates;
        this.abilityEnglishName = abilityEnglishName;
        this.abilityIntroArabic = abilityIntroArabic;
        this.level1Lines = level1Lines;
        this.level3Lines = level3Lines;
        this.level5Lines = level5Lines;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAbilityEnglishName() {
        return abilityEnglishName;
    }

    public String getAbilityIntroArabic() {
        return abilityIntroArabic;
    }

    public String[] getLevel1Lines() {
        return level1Lines;
    }

    public String[] getLevel3Lines() {
        return level3Lines;
    }

    public String[] getLevel5Lines() {
        return level5Lines;
    }

    public Material getIconMaterial() {
        for (String name : materialCandidates) {
            Material mat = Material.matchMaterial(name);
            if (mat != null) return mat;
        }
        return Material.STICK;
    }

    public boolean isSupportedOnThisServer() {
        for (String name : materialCandidates) {
            if (Material.matchMaterial(name) != null) return true;
        }
        return false;
    }
}
