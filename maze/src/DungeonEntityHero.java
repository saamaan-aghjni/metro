
public class DungeonEntityHero extends DungeonEntity 
{
    public DungeonEntityHero(DungeonPoint loc) {
        super("Artjom", loc, DungeonEntityRace.HUMAN, DungeonEntityClass.WARRIOR, new DungeonComponentStat(null, 10, 10, 1.0, 1.0, 0,0 , 2, 18, 0,0, 0, 0, 0, 0, 0.0,  1, 20, 2));   
        status.setOwner(this);
        abilities.put(DungeonAbilityType.INTERACTION, true);
        abilities.put(DungeonAbilityType.MOVEMENT, true);
    }
}
