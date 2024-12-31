import java.util.ArrayList;
//The movement Component acts on an entity and a list of other entities and - possibly -  items

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
        owner.setNextAction(null);
        DungeonPoint ownerPos = owner.getPosition();
        DungeonPoint p = DungeonUtil.movePoint(ownerPos, dir, 1);
        DungeonEntity ent = world.getEntityAt(p);
        var stat=owner.getStat();
        if(!DungeonUtil.lowerBound(stat.getEnergy(),10.0)) {
            System.out.println(owner.getName()+" has barely any energy and still tried to walk!");
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
        }
        if(world.getTerrainAt(p)==Terrain.STONE_WALL) {
            System.out.println(owner.getName() +" tried walking  onto a stone wall!");
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
        }
        if(ent!=null && !ent.getName().equals(owner.getName())) {
            System.out.println(" Here is a Entity! "+ ent.toString());
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
        }            
        world.moveEntity(owner.getPosition(), p);
        stat.setEnergy(stat.getEnergy()-10);
        return new DungeonComponentResult(DungeonComponentResultType.SUCCESS, null, null);
    }
}
