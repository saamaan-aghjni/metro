import java.util.logging.Level;
/**
 *
 * @author saman
 */
public class DungeonAbilityUnequip extends DungeonComponent {
    
    public DungeonAbilityUnequip(DungeonEntity ow) {
        super(ow);
    }
    public DungeonComponentResult perform() {
        if(owner.getEquipped()==null) {
            owner.log(Level.INFO, "You must be holding something!");
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
        }
        if(owner.getInventory().isFull()) {
            owner.log(Level.INFO, "Your inventory is full!");
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
        }
        try {
            owner.getInventory().add(owner.getEquipped());            
            owner.log(Level.INFO, "You put the "+owner.getEquipped().getName()+" back in your inventory!");
            owner.setEquippedItem(null);

            return new DungeonComponentResult(DungeonComponentResultType.SUCCESS, null, null);
        }
        catch(Exception e) {
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);

        }
    }
    
}
