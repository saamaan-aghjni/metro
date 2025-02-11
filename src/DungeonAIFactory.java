import java.util.ArrayList;
import java.util.List;

public class DungeonAIFactory {

    /**
     * Generates a list of AI instances based on the entity's enabled abilities.
     *
     * @param entity the entity for which to generate AIs
     * @return a list of AI instances
     */
    public static List<DungeonAI> generateAIs(DungeonEntity entity, World world) {
        List<DungeonAI> aiList = new ArrayList<>();

        // Example: If the entity has movement ability, add a movement AI
        if (entity.hasAbilityEnabled(DungeonAbilityType.MOVEMENT)) {
            DungeonEntity nearestEntity = DungeonAIUtil.findNearestEntity(entity, world);
            if(nearestEntity!=null) {
                aiList.add(new DungeonAIChase(entity, nearestEntity));
            }
        }

        // Example: If the entity can pick up items, add a pickup AI
        if (entity.hasAbilityEnabled(DungeonAbilityType.ITEMPICKUP)) {
            DungeonEntity nearestItem = DungeonAIUtil.findNearestItem(entity, world);

            if (nearestItem != null) {
                aiList.add(new DungeonAIPickup(entity, nearestItem));
            }
        }

        // Add more AI types based on abilities
        // Example:
        // if (entity.hasAbilityEnabled(DungeonAbilityType.ATTACK)) {
        //     aiList.add(new DungeonAIAttack(entity));
        // }

        return aiList;
    }
}