import java.util.logging.Level;

public class DungeonAbilityMove extends DungeonComponent {
    private int width;
    private Direction dir;
    private World world;
    public DungeonAbilityMove(DungeonEntity ow,World world, Direction dir) {
        super(ow);
        this.world = world;
        this.dir = dir;
    }
    @Override
    public DungeonComponentResult perform() {
        
        DungeonPoint ownerPos = owner.getPosition();
        DungeonPoint p = DungeonUtil.movePoint(ownerPos, dir, 1);
        DungeonEntity ent = world.getEntityAt(p);
        var stat=owner.getStat();
        if( !world.dungeon.hasPathTo(ownerPos, dir) || !Terrain.isPassable(world.dungeon.getTerrainAt(p)) ) {
            owner.log(Level.WARNING, "Impassable terrain!");
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
        }
    
        if(ent!=null && !ent.equals(owner)) {
            owner.log(Level.WARNING, "Here is "+ent.getName());
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
        }            
        world.moveEntity(owner.getPosition(), p);
        
        return new DungeonComponentResult(DungeonComponentResultType.SUCCESS, null, null);
    }
    @Override
    public double getCost() {
        return 0.5; // Movement should cost half energy points.
    }
}
