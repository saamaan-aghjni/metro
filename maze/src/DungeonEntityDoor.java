enum DungeonDoorType {
    WOODEN(Terrain.WOODEN_DOOR, Terrain.DIRT),
    METAL(Terrain.METAL_DOOR, Terrain.DIRT);
    private Terrain whenClosed, whenOpened;
    private DungeonDoorType(Terrain wc, Terrain wo) {        
        this.whenClosed = wc;
        this.whenOpened = wo;
    }
    public Terrain whenClosed() { 
        return whenClosed;
    }
    public Terrain whenOpened() { 
        return whenOpened;
    }
}

public class DungeonEntityDoor extends DungeonEntity // Door is just an entity with some more states
{
    private DungeonDoorType doorType = null;    
    private DungeonDoorState state;    
    DungeonEntityDoor(DungeonPoint p, DungeonDoorType type, double hitpoints, double maxHitpoints) {
        super("", p, null, null, new DungeonComponentStat(null, hitpoints, maxHitpoints, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1));
        this.doorType = type;
        state = DungeonDoorState.CLOSED;
        status.setOwner(this);
        abilities.put(DungeonAbilityType.DOORMANAGEMENT, true);        
        setNextAction(DungeonComponent.DUMMY);

    }
    public Terrain terrainWhenClosed() {
        return doorType.whenClosed();
    }
    public Terrain terrainWhenOpened() {
        return doorType.whenOpened();
    }
    public DungeonDoorState getDoorState() {
        return state;
    }
    public void setDoorState(DungeonDoorState st) {
        state = st;
    }    
    @Override 
    public String toString() {
        return super.toString() +"\nCurrent "+state.toString();
        }
}
