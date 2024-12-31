enum DungeonDoorState {
    CLOSED,
    OPENED,
    LOCKED,
    UNLOCKED
}

public class DungeonAbilityDoor extends DungeonComponent {
    private World world;
    public DungeonAbilityDoor(DungeonEntityDoor ow, World world,  DungeonDoorType tp) {
        super(ow);
        owner = owner;
        this.world = world;
    }
    
    public DungeonComponentResult perform() {
        owner.setNextAction(DungeonComponent.DUMMY);
        DungeonEntityDoor doorOwner=(DungeonEntityDoor)owner;
        DungeonDoorState state = doorOwner.getDoorState();
        switch(state) {
        case CLOSED:
            state = DungeonDoorState.OPENED;
            world.dungeon.map.cellAt(owner.getPosition()).setTerrain(doorOwner.terrainWhenOpened());
            break;
        case OPENED:
            state = DungeonDoorState.CLOSED;
            world.dungeon.map.cellAt(owner.getPosition()).setTerrain(doorOwner.terrainWhenClosed());
            break;
        }
        return new DungeonComponentResult(DungeonComponentResultType.SUCCESS, null, null);
    }
}
