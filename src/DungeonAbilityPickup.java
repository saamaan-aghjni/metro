import java.util.logging.Level;

/**
 *
 * @author saman
 */
public class DungeonAbilityPickup extends DungeonComponent {
    private World world;
    public DungeonAbilityPickup(DungeonEntity ent, World w) {
        super(ent);
        this.world =w;
    }
    public DungeonComponentResult perform() {
        var item = world.getItemAt(owner.getPosition());
        if(item == null) {
            owner.log(Level.INFO, "Here is nothing to pickup!");
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
        }
        if(!owner.getInventory().canCarry(item)) {
            owner.log(Level.INFO, "You cannot carry more items!");
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);

        }
        world.removeItem(item);
        owner.log(Level.INFO, "You put the "+ item.getName() +" in your inventory!");
        item.setPosition(null);
        try {
            owner.getInventory().add(item);
            return new DungeonComponentResult(DungeonComponentResultType.SUCCESS, null, null);
        }
        catch(Exception e) {
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);

        }
    }
}
