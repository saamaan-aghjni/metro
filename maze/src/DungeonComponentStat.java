public class DungeonComponentStat extends DungeonComponent {
    protected double health, maxHealth, energy, maxEnergy;
    protected double mana, armour, attackPoint, exp, dexterity;
    public int level;
    protected boolean isAlive;
    public DungeonComponentStat(DungeonEntity ow, double health, double maxHealth, double energy, double maxEnergy, double mana, double armour, double attackPoint, double exp, int level) {
        super(ow);
        isAlive = true;
        this.health = health;
        this.maxHealth = maxHealth;
        this.energy = energy;
        this.maxEnergy = maxEnergy;        
        this.mana = mana;
        this.armour = armour;
        this.attackPoint = attackPoint;
        this.exp = exp;
        this.level = level;
        this.energy = energy;

    }
    public void setOwner(DungeonEntity ent) {
        owner = ent;
    }
    public DungeonComponentResult perform() {
        if(health<=0) {
            isAlive = false;
        }
        if(energy<maxEnergy) {
            energy += 0.5;
        }
        if(energy > maxEnergy) {
            energy = maxEnergy;
        }
        return null;
    }
    public double getEnergy() {
        return energy; 
    }
    public void setEnergy(double e) {
        energy = e;
    }
    public double getMaxEnergy() {
        return maxEnergy;
    }

    public void setHealth(double hp) {
        health = hp;
    }
    
    public double getHealth() {
        return health;
    }
        public boolean isAlive() {
        return isAlive;
    }
    @Override
    public String toString() {
return "Stats of "+owner.getName()+": \nHitpoints: "+health+"\nMax Hitpoints: "+maxHealth+"\nEnergy: "+energy+"\nMaxenergy: "+maxEnergy+"\nDefence Points: "+armour+"\nDexterity: "+dexterity+"\nMana: "+mana+"\nIs alive? "+isAlive;
    }
}
