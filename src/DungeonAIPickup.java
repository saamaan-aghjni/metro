
/**
 *
 * @author saman
 */
public class DungeonAIPickup extends DungeonAI {
    private DungeonAIChase itemChase;
    private DungeonEntity item;
    public DungeonAIPickup(DungeonEntity owner, DungeonEntity item) {
        super(owner);
        this.item = item;
        this.itemChase = new DungeonAIChase(owner, item);
    }
    public void act(World w) {
        if(item.getPosition() == null) {
            
            state = DungeonAIState.FINISHED;
            return;
        }
        itemChase.act(w);
        
        if(item.getPosition().equals(owner.getPosition())) {
            state = DungeonAIState.FINISHED;
            
            owner.setNextAction(new DungeonAbilityPickup(owner, w));
        }
    }

    public double getScore(World world) {
        return DungeonAIUtil.getInverseDist(owner, item)+1.0;
    }

}
