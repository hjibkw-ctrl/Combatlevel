package com.combatlevels.plugin.data;

import com.combatlevels.plugin.classes.CombatClass;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private CombatClass combatClass;
    private int kills;

    private UUID swordStreakTarget;
    private int swordStreakCount;

    // وضعية الكريتكال المستمرة بعد 3 ضربات متتالية.
    // تفضل مفعّلة لين تنتهي المدة أو يوقفها ضرب معاكس (نلغيها من الكود الخارجي مباشرة).
    private long critModeExpiryMillis;

    private long maceLastFreezeMillis;

    // "The Biggest Cart": آخر وقت انعطى فيه اللاعب عربة تنت مطورة.
    // تنضبط أول مرة لحظة أول قتلة، وبعدين كل 10 دقايق من هالوقت.
    private long lastBiggestCartMillis;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.combatClass = null;
        this.kills = 0;
        this.swordStreakTarget = null;
        this.swordStreakCount = 0;
        this.critModeExpiryMillis = 0L;
        this.maceLastFreezeMillis = 0L;
        this.lastBiggestCartMillis = 0L;
    }

    public UUID getUuid() {
        return uuid;
    }

    public CombatClass getCombatClass() {
        return combatClass;
    }

    public void setCombatClass(CombatClass combatClass) {
        this.combatClass = combatClass;
    }

    public boolean hasChosenClass() {
        return combatClass != null;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        this.kills++;
    }

    public void addKillsRaw(int kills) {
        this.kills = kills;
    }

    public boolean isBeginnerPerkUnlocked() {
        return kills >= 1;
    }

    public UUID getSwordStreakTarget() {
        return swordStreakTarget;
    }

    public int getSwordStreakCount() {
        return swordStreakCount;
    }

    public int registerSwordHit(UUID target) {
        if (target.equals(swordStreakTarget)) {
            swordStreakCount++;
        } else {
            swordStreakTarget = target;
            swordStreakCount = 1;
        }
        return swordStreakCount;
    }

    public void resetSwordStreak() {
        swordStreakTarget = null;
        swordStreakCount = 0;
    }

    // ===== وضعية الكريتكال المستمرة (السيف) =====

    public boolean isCritModeActive() {
        return System.currentTimeMillis() < critModeExpiryMillis;
    }

    /**
     * يفعّل أو يجدد وضعية الكريتكال لمدة durationMillis من الآن.
     */
    public void activateCritMode(long durationMillis) {
        this.critModeExpiryMillis = System.currentTimeMillis() + durationMillis;
    }

    /**
     * يلغي وضعية الكريتكال فوراً (يستخدم لما اللاعب ينضرب من حد ثاني).
     */
    public void cancelCritMode() {
        this.critModeExpiryMillis = 0L;
    }

    public long getCritModeExpiryMillis() {
        return critModeExpiryMillis;
    }

    public void setCritModeExpiryMillis(long critModeExpiryMillis) {
        this.critModeExpiryMillis = critModeExpiryMillis;
    }

    // ===== الميس =====

    public long getMaceLastFreezeMillis() {
        return maceLastFreezeMillis;
    }

    public void setMaceLastFreezeMillis(long millis) {
        this.maceLastFreezeMillis = millis;
    }

    // ===== عربة التنت (The Biggest Cart) =====

    public long getLastBiggestCartMillis() {
        return lastBiggestCartMillis;
    }

    public void setLastBiggestCartMillis(long millis) {
        this.lastBiggestCartMillis = millis;
    }
}
