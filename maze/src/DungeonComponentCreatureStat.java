/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author saman
 */

enum DungeonCreatureClass {
    WARRIOR,
    MAGE,
    PREACHER,
    GENERIC;
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}


public class DungeonComponentCreatureStat extends DungeonComponentStat {
    private DungeonCreatureClass eClass;
    public DungeonComponentCreatureStat(DungeonEntity ow, int health, int maxHealth, double energy, double recoverEnergy, int mana, int maxMana, int strength, int maxStrength, int inteligence, int maxInteligence, int constitution, int maxConstitution, int wisdom, int maxWisdom,  double exp, int level, int thaco, int armorClass, DungeonCreatureClass c) {
        super(ow, health, maxHealth,  energy,  recoverEnergy,  mana,  maxMana,  strength,  maxStrength,  inteligence,  maxInteligence,  constitution,  maxConstitution,  wisdom,  maxWisdom,   exp,  level, thaco, armorClass);

        this.eClass = c;
    }
        public DungeonCreatureClass getCreatureClass() {
            return eClass;
        }
    @Override
        public String toString() {
            return super.toString() +"\nClass: "+eClass.toString();
        }
}