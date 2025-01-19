// So every *Entity * in the game has a placeholder for the ComponentStat.  
//This allows for a greater flexibility - ee.g. DungeonItem inherits from Entity and DungeonWeapon from DungeonItem, so a sword should also have mana points.  This is great, when later on the magic system is introduced, or if one want to make breakable weapons etc. etc.

public class DungeonComponentStat extends DungeonComponent  {
    protected double  energy,  recoverEnergy;  // This is for the entity's energy to perform different things (entity action).  The cost of an action is a value between 0-1 with 0.0 thrown in, for one-time performed actions (See World.update())
    
    protected int hitpoints,  maxHitpoints, mana, maxMana,  str, maxStr,  dex, maxDex, intl, maxIntl, wis, maxWis, con, maxCon, armorClass, thaco; //D&D? :)
    protected int level;
    protected double exp;
    
    protected boolean isAlive;
    public DungeonComponentStat(DungeonEntity ow, int health, int maxHealth, double energy, double recoverEnergy, int mana, int maxMana, int strength, int maxStrength, int inteligence, int maxInteligence, int constitution, int maxConstitution, int wisdom, int maxWisdom,  double exp, int level, int thaco, int armorClass) {
        super(ow);
        isAlive = true;
        this.hitpoints = health;
        this.maxHitpoints = maxHealth;
        this.energy = energy;
        this.recoverEnergy = recoverEnergy;        
        this.mana = mana;
        this.maxMana = maxMana;
        this.str = strength;
        this.maxStr = maxStrength;
        this.intl = inteligence;
        this.maxIntl = maxInteligence;
        this.wis = wisdom;
        this.maxWis = maxWisdom;
        this.con = constitution;
        this.maxCon = maxConstitution;
        this.exp = exp;
        this.level = level;
        this.thaco = thaco;
        this.armorClass = armorClass;
    }
    public void setOwner(DungeonEntity ent) {
        owner = ent;
    }
    public DungeonComponentResult perform() {
        if(hitpoints<=0) {
            isAlive = false;
        }
        if(energy< 1.0) {
            energy += recoverEnergy;
        }
        if(energy > 1.0) {
            energy = 1.0;
        }
        return null;
    }
    public double getEnergy() {
        return energy; 
    }
    public void setEnergy(double e) {
        System.out.println("Energy is "+energy+", now setting it to "+e);
        energy = e;
        System.out.println("Energy is "+energy+", now setting it to "+e);
        
    }
    public void setArmorClass(int ac) {
        if(ac>10 || ac<-10) {
            return;
        }
        armorClass = ac;
    }
    public int getArmorClass() {
        return armorClass;
    }
    public int getThaco() {
        return thaco;
    }
    public void addToHitpoints(int hp) {
        hitpoints+=hp;
        if(hitpoints> maxHitpoints) {
            hitpoints = maxHitpoints;
        }
    }
    public int getMaxHitpoints() {
        return maxHitpoints;
    }
    public int getHitpoints() {
        return hitpoints;
    }
    public int getDex() {
        return dex;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }
    public int getStr() {
        return str;
    }
    public int getMaxStr() {
        return maxStr;
    }
    public void setStr(int str) {
        this.str = str;
    }
        public boolean isAlive() {
        return isAlive;
    }
    @Override
    public String toString() {
return "Stats of "+owner.getName()+": \nHitpoints: "+hitpoints+"/"+maxHitpoints+"\nEnergy: "+energy+"/1.0\nrecoverenergy: "+recoverEnergy+"\nDexterity: "+dex+"/"+maxDex+"\nMana: "+mana+"/"+maxMana+"\nWisdom: "+wis+"/"+maxWis+"\nIs alive? "+isAlive;
    }
}
