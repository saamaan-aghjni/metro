
import java.util.HashMap;

enum DungeonEntityRace {
    HUMAN("H"),
    BLACKMUTANT("B"),
    GHOST("G"),
    RAT("R");
    String identifier;
    DungeonEntityRace(String id) {
        identifier = id;
    }
    @Override
    public String toString() {
        return identifier;
    }
}
enum DungeonEntitySex {
    FEMALE,
    MALE,
    NONBINARY
}
enum DungeonEntityClass {
    WARRIOR,
    MAGE,
    PREACHER,
    MUSHROOMGROWER
}

public class DungeonEntity {
    protected HashMap<DungeonAbilityType, Boolean> abilities = new HashMap<>();
    protected DungeonEntityRace entityRace;
    protected DungeonEntityClass entityClass;
    protected DungeonComponent nextAction;
    protected DungeonComponentStat status;
    protected String name;
    protected DungeonPoint pos;
    protected DungeonEntity(String name, DungeonPoint pos, DungeonEntityRace t, DungeonEntityClass c, DungeonComponentStat s) {
        this.name = name;
        this.pos = pos;
        this.entityRace = t;
        this.entityClass = c;
        this.status = s;
    }
    public boolean hasAbility(DungeonAbilityType c) {
        return abilities.containsKey(c);
    }
    
    public boolean hasAbilityEnabled(DungeonAbilityType c) {
        return hasAbility(c) && abilities.get(c);
    }
    public boolean addAbility(DungeonAbilityType c, boolean enabled) {
        return abilities.put(c, enabled); 
    }
    public boolean removeAbility(DungeonAbilityType c) {
        return hasAbility(c) ? abilities.remove(c) : false; 
    }

    public boolean disableAbility(DungeonAbilityType c) {
        return hasAbility(c) ? abilities.put(c, false) : false;
    }
    public boolean enableAbility(DungeonAbilityType c) {
        return hasAbility(c) ? abilities.put(c, true) : false;
    }
    public DungeonComponent getNextAction() { return nextAction; }
    public void setNextAction(DungeonComponent a) {
        nextAction = a;
    }
    public DungeonEntityClass getEntityClass() { return entityClass; }
    public DungeonPoint getPosition() {
        return pos;
    }
    public void setPosition(DungeonPoint p) {
        pos = p; 
    }
    public void setRace(DungeonEntityRace r) {
        
        entityRace = r;
    }
    public DungeonEntityRace getRace() { return entityRace; }
    public String getName() {
        return name;
    }
    public DungeonComponentStat getStat() {
        return status;
    }
    
    @Override
    public String toString() {
        return "Name: "+ name +" Race: "+ (entityRace==null ? " unknown" : entityRace.toString()) +"\n Class: "+ (entityClass == null ? " unknown " : entityClass.toString())+" \nLocation: "+ pos.toString()+" \nStatus: "+status.toString()+"\nAbilities: "+abilities.toString();
    }
}
