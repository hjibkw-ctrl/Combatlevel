package com.combatlevels.plugin.data;

import com.combatlevels.plugin.classes.CombatClass;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private CombatClass combatClass;
    private int kills;

    private UUID swordStreakTarget;
    private int swordStreakCount;

    private long maceLastFreezeMillis;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.combatClass = null;
        this.kills = 0;
        this.swordStreakTarget = null;
        this.swordStreakCount = 0;
        this.maceLastFreezeMillis = 0L;
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

    public long getMaceLastFreezeMillis() {
        return maceLastFreezeMillis;
    }

    public void setMaceLastFreezeMillis(long millis) {
        this.maceLastFreezeMillis = millis;
    }
}
