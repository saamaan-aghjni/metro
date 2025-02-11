import java.util.logging.Level;

public class DungeonAbilityInterraction extends DungeonComponent {
    private World world;
    public DungeonAbilityInterraction(DungeonEntity owner,  World world) {
        super(owner);
        this.world = world;
    }
    private boolean interractNorth() {
        DungeonPoint ownerpos = owner.getPosition();
        for(int i=ownerpos.getX(); i>ownerpos.getX()-2; i--) {
            DungeonEntity ent=world.getEntityAt(new DungeonPoint(i, ownerpos.getY()));
            if(ent!=null && !ent.equals(owner)) {
                InterractWith(ent);
                return true;            
            }
        }
        return false;
    }
    private boolean interractSouth() {
        DungeonPoint ownerpos = owner.getPosition();
        for(int i=ownerpos.getX(); i<ownerpos.getX()+2; i++) {
            DungeonEntity ent=world.getEntityAt(new DungeonPoint(i, ownerpos.getY()));
            if(ent!=null && !ent.equals(owner)) {
                InterractWith(ent);
                return true;            
            }
        }
        return false;
    }
    private boolean interractWest() {
        DungeonPoint ownerpos = owner.getPosition();
        for(int i=ownerpos.getY(); i>ownerpos.getY()-2; i--) {
            DungeonEntity ent=world.getEntityAt(new DungeonPoint(ownerpos.getX(), i));
            if(ent!=null && !ent.equals(owner)) {
                InterractWith(ent);
                return true;            
            }
        }
        return false;
    }
    private boolean interractEast() {
        DungeonPoint ownerpos = owner.getPosition();
        for(int i=ownerpos.getY(); i<ownerpos.getY()+2; i++) {
            DungeonEntity ent=world.getEntityAt(new DungeonPoint(ownerpos.getX(), i));
            if(ent!=null && !ent.equals(owner)) {
                InterractWith(ent);
                return true;            
            }
        }
        return false;
    }

    public DungeonComponentResult perform() {
        boolean foundToInterract = false;
        DungeonPoint ownerpos=owner.getPosition();
        Direction facing = owner.getStat().getFacing();
        switch(facing) {
            case Direction.NORTH: 
                foundToInterract = interractNorth();
                break;
                case Direction.SOUTH: 
                foundToInterract = interractSouth();
                break;
                case Direction.WEST: 
                foundToInterract = interractWest();
                break;
                case Direction.EAST: 
                foundToInterract = interractEast();
                break;
        }


        if(foundToInterract) {
            return new DungeonComponentResult(DungeonComponentResultType.SUCCESS, null, null);
        }
        owner.log(Level.INFO, "There is nothing around you to Interract with!");
        return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
    }
    void InterractWith(DungeonEntity ent) {
        switch(ent.getRace()) {
            case DOOR:
                InterractWithDoor(ent);
                break;
        }
    }
    private void InterractWithDoor(DungeonEntity ent) {
        DungeonEntityDoor door = (DungeonEntityDoor)ent;
        if(door.getDoorState() == DungeonDoorState.CLOSED) {
            owner.log(Level.INFO, "You open the door");
        }
        if(door.getDoorState() == DungeonDoorState.OPENED) {
            owner.log(Level.INFO, "You close the door");
        }

        ent.setNextAction(new DungeonAbilityDoor((DungeonEntityDoor)ent, world));
        
    }
}
