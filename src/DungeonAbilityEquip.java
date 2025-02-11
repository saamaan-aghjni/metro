import java.util.logging.Level;
/**
 *
 * @author saman
 */
public class DungeonAbilityEquip extends DungeonComponent {
    String itemName;
    public DungeonAbilityEquip(DungeonEntity ow, String item) {
        super(ow);
        this.itemName = item;
    }
    public DungeonComponentResult perform() {
        if(owner.getEquipped()!=null) {
            owner.log(Level.INFO, "You must not be holding any items!");
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
        }
        if(!owner.getInventory().contains(itemName)) {
            owner.log(Level.INFO, "You don't have any "+itemName+" in your inventory!");
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);
        }
        try {
        DungeonEntity item = owner.getInventory().remove(itemName);
        owner.setEquippedItem(item);
        owner.log(Level.INFO, "You equip a "+itemName+"!");
        return new DungeonComponentResult(DungeonComponentResultType.SUCCESS, null, null);
        }
        catch(Exception e) {
            return new DungeonComponentResult(DungeonComponentResultType.FAILURE, null, null);

        }
    }
    
}
