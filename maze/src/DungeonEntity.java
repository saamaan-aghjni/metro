import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

enum DungeonEntityRace {
    HUMAN("H"),
    BLACKMUTANT("B"),
    GHOST("G"),
    ITEM("I"),
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
    protected UUID id;
    protected DungeonComponent nextAction;
    protected DungeonComponentStat status;
    protected String name;
    protected DungeonPoint pos;
    protected DungeonEntity equipedItem;
    private Logger logger;
    protected DungeonEntity(String name, DungeonPoint pos, DungeonEntityRace t, DungeonEntityClass c, DungeonComponentStat s) {
        this.name = name;
        this.pos = pos;
        this.entityRace = t;
        this.entityClass = c;
        this.status = s;
        this.status.setOwner(this);
        this.logger = null;
        this.id = UUID.randomUUID();
    }
    public boolean hasAbility(DungeonAbilityType c) {
        return abilities.containsKey(c);
    }
    public DungeonEntity getEquipped() {
        return equipedItem;
    }
    public void setEquippedItem(DungeonEntity ent) // Should be removed 
    {
        equipedItem = ent;
    }
    public boolean hasAbilityEnabled(DungeonAbilityType c) {
        return hasAbility(c) && abilities.get(c);
    }
    public void addAbility(DungeonAbilityType c, boolean enabled) {
         abilities.put(c, enabled); 
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
    private void initLogger() 
    {
        logger = Logger.getAnonymousLogger();
    }
    public void setLoggerHandler(Handler h) {
        if(logger == null) {
            initLogger();
        }
        logger.addHandler(h);
    }
    public void log(Level level, String message) {
        if(logger == null) {
            return;
        }
        logger.log(level, message);
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
    public UUID getID() {
        return id;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if( obj == this) {
            return true;
        }
        if(!(obj instanceof DungeonEntity)) {
            return false;
        }
        return this.id.equals(((DungeonEntity)obj).id);
    }
    @Override
    public String toString() {
        return "Name: "+ name +" Race: "+ (entityRace==null ? " unknown" : entityRace.toString()) +"\n Class: "+ (entityClass == null ? " unknown " : entityClass.toString())+" \nLocation: "+ pos.toString()+" \nStatus: "+status.toString()+"\nCurrently holds "+ ( equipedItem == null ? " nothing " : equipedItem.getName())+".\nAbilities: "+abilities.toString();
    }
}
