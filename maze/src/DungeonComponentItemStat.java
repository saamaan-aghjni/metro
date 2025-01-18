
/** So the idea is this:
 * DungeonComponentStat is general applicable for all entities, but item entities only have the DungeonItemStat.
 * Added attributes? ThrowDamage, MelayDamage, RangeDamage, throw range, etc.
 * So why?  Suppose I want to throw a rat's body accross the room :)) well this should pose no problems.  
 * What about a petrifying rat's corpse?  This should have some effects on the entity it hits (not all entities!)
 * But a rat's corpse is no weapon, so it should have the attribute of a weapon (throwable) and perhaps a damage point etc.
 */
enum DungeonItemCategory {
    WEAPON,
    POTION,
    CORPSE,
    BOOK,
    FOOD,
    PROJECTILE
}


 record DamagePoint(int diceNumbers, int diceSides, boolean isNegative, int dist) { // isNegtive indicate if the damage should be subtracted (a knife) or added (heeling spell/potion)
 }

public class DungeonComponentItemStat extends DungeonComponentStat {
    protected DamagePoint throwDP, rangeDP, melayDP;
    protected int weight;
    protected boolean isPickable;
    protected DungeonItemCategory category;
    public DungeonComponentItemStat(DungeonEntity ow, int health, int maxHealth, double energy, double recoverEnergy, int mana, int maxMana, int strength, int maxStrength, int inteligence, int maxInteligence, int constitution, int maxConstitution, int wisdom, int maxWisdom,  double exp, int level, DamagePoint throwDamage, DamagePoint rangeDamage, DamagePoint melayDamage, int weight, DungeonItemCategory category, boolean isPickable) {
        super(ow, health, maxHealth, energy, recoverEnergy, mana, maxMana, strength, maxStrength, inteligence, maxInteligence, constitution, maxConstitution, wisdom, maxWisdom, exp, level, 0, 0);
        this.throwDP = throwDamage;
        this.melayDP = melayDamage;
        this.rangeDP = rangeDamage;
        this.isPickable = isPickable;
        this.category = category;
        this.weight = weight;
    }
    public DamagePoint getThrowDP() {
        return throwDP;
    }
    public DamagePoint getRangeDP() {
        return rangeDP;
    }
    public DamagePoint getMelayDP() {
        return melayDP;
    }
    public boolean isPickable() {
        return isPickable;
    }
    public DungeonItemCategory getCategory() {
        return category;
    }
    public int getWeight() {
        return weight;
    }
}