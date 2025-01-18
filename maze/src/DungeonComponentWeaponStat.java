
enum DungeonWeaponType {
    MELAY,
    PROJECTILE
}

public final class DungeonComponentWeaponStat extends DungeonComponentItemStat {
    
    private DungeonWeaponType type;
    public DungeonComponentWeaponStat(DungeonEntity owner, int hitpoints, int maxHitpoints, double  energy, double recoverEnergy, int mana, int maxMana, int str, int maxStr, int intl, int maxIntl, int con, int maxCon, int wis, int maxWis, double exp, int level, DamagePoint throwDP, DamagePoint rangeDP, DamagePoint melayDP, int weight, boolean ispickable)
    {
        super(owner, hitpoints, maxHitpoints, energy, recoverEnergy, mana, maxMana, str, maxStr, intl, maxIntl, con, maxCon, wis, maxWis, exp, level, throwDP, rangeDP, melayDP, weight, DungeonItemCategory.WEAPON, ispickable);
    }    
}
