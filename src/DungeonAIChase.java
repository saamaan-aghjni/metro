



class DungeonAIChase extends DungeonAI {
    private DungeonAITargetPoint chaseAI;
    private DungeonEntity target;
    private DungeonPoint lastPoint;
    private double lastEvaluatedTargetScore;

    public DungeonAIChase(DungeonEntity owner, DungeonEntity target) {
        super(owner);
        this.target = target;
        lastPoint = new DungeonPoint(-1, -1);
        state = DungeonAIState.WORKING;
    }
    public void act(World world) {
        if(target == null || owner.getPosition().distManhattan(target.getPosition())<=0) {
            state = DungeonAIState.FINISHED;
            
            owner.setNextAction(null);
            
            return;
        }
        if(!lastPoint.equals(target.getPosition())) {
            
            lastPoint = target.getPosition();
            chaseAI = new DungeonAITargetPoint(owner, lastPoint, (p) ->   world.dungeon.unblockedNeighboursOf(p));
        }
        chaseAI.act(world);
    }
    public double getScore(World world) {
        return DungeonAIUtil.getInverseDist(owner, target);
    }
}