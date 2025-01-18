/**
 * https://journal.stuffwithstuff.com/2014/07/15/a-turn-based-game-loop/
 **/
enum DungeonAbilityType {
    MOVEMENT,
    INTERACTION,
    CONSUME,
    MELAYATTACK,
    DOORMANAGEMENT, // Special for Door entities
    ITEMPICKUP
}

enum DungeonComponentResultType {
    FAILURE,
    SUCCESS,
    TRANSITION
}

record DungeonComponentResult(DungeonComponentResultType result, DungeonAbilityType requiredAbility, DungeonComponent component) { 

}

public  abstract class DungeonComponent {    
    protected DungeonEntity owner;
    protected static final DungeonComponent DUMMY =new DungeonComponentDummy();
    public DungeonComponent(DungeonEntity e) {
        this.owner = e;
    }
    public  abstract DungeonComponentResult perform();
    public double getCost() {
        return 0.0;
    }
}

class DungeonComponentDummy extends DungeonComponent // Because DungeonComponentResult does not allow null values 
{
    public DungeonComponentDummy() {
        super(null);
    }
public DungeonComponentResult perform() {

    return null;
}
} 