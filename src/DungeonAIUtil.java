
/**
 *
 * @author saman
 *  Static methods to aid with AI score and/or target calculation
 */

public class DungeonAIUtil {
    private DungeonAIUtil() {

    }
    public static double getInverseDist(DungeonEntity self, DungeonEntity target) {
        return 1.0/(1.0 +self.getPosition().distManhattan(target.getPosition()));
    }
    public static DungeonEntity findNearestItem(DungeonEntity entity, World world) {
        var stat = (DungeonComponentCreatureStat)entity.getStat();
        var targets = world.getItemsInRange(entity, stat.getVision());
        if(targets.isEmpty()) {
            return null;
        }
        double nearestDist =Double.POSITIVE_INFINITY;
        DungeonEntity best=null;
        for(DungeonEntity t: targets)
        {
            if(getInverseDist(t, entity)<nearestDist) {
                nearestDist = getInverseDist(t, entity);
                best = t;
            }
        }
        return best;
    }
    public static DungeonEntity findNearestEntity(DungeonEntity entity, World world) {
        var stat = (DungeonComponentCreatureStat)entity.getStat();
        var targets = world.getEntitiesInRange(entity, stat.getVision());
        if(targets.isEmpty()) {
            return null;
        }
        double nearestDist =Double.POSITIVE_INFINITY;
        DungeonEntity best=null;
        for(DungeonEntity t: targets)
        {
            if(getInverseDist(t, entity)<nearestDist && t.getRace() != DungeonEntityRace.DOOR) {
                nearestDist = getInverseDist(t, entity);
                best = t;
            }
        }
        return best;
    }

}
