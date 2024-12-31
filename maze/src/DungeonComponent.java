enum DungeonAbilityType {
    MOVEMENT,
    INTERACTION,
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