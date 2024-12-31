
public class DungeonEntityHero extends DungeonEntity 
{
    public DungeonEntityHero(DungeonPoint loc) {
        super("Artjom", loc, DungeonEntityRace.HUMAN, DungeonEntityClass.WARRIOR, new DungeonComponentStat(null, 1000.0, 1000.0, 100.0, 100.0, 100.0, 1000.0, 1000.0, 1000.0, 1));   
        status.setOwner(this);
        abilities.put(DungeonAbilityType.INTERACTION, true);
        abilities.put(DungeonAbilityType.MOVEMENT, true);
    }
}
