import java.util.logging.Level;

/**
 *
 * @author saman
 */
public class DungeonAbilityDrop extends DungeonComponent {
    private World world;
    public DungeonAbilityDrop(DungeonEntity ent, World w) {
        super(ent);
        world = w;
    }
    public DungeonComponentResult perform() {
        DungeonEntity equip = owner.getEquipped();
        if(equip == null) {
            owner.log(Level.INFO, "You're holding nothing to drop!");
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
        }
        equip.setPosition(owner.getPosition());
        world.addItem(owner.getPosition(), equip);
        owner.log(Level.INFO, "You drop the "+equip.getName()+"!");
        owner.setEquippedItem(null);
        return new DungeonComponentResult(DungeonComponentResultType.SUCCESS, null, null);
    }
}

