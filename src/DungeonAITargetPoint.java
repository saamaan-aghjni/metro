

import java.util.ArrayList;


/**
 *
 * @author saman
 */
public class DungeonAITargetPoint extends DungeonAI {
    private transient DungeonAStar astar;
    
    private int currentPoint;
    private boolean gotAPath=false;
    private DungeonPoint target;
    private ArrayList<DungeonPoint> points;
    public  DungeonAITargetPoint(DungeonEntity ent, DungeonPoint target, DungeonAStarNeighbourhoodCalculator<DungeonPoint> ncalc) {
        super(ent);
        this.target = target;
        astar =DungeonAStar.newAStar(ent.getPosition(), target, ncalc, (p) -> {
            return p.distManhattan(target);
        });
        astar.initiate();
    }
    public void act(World w) {
        switch(state) {
            case IDLE:
                Thread t=new Thread(() -> {
                    while(!astar.isDone()) {
                        astar.procede();
                    }
                });
                
                state = DungeonAIState.WORKING;
                t.run();

                break;
            case WORKING:
                if(astar.isDone() && !gotAPath) {
                    gotAPath=true;
                    points = astar.retrievePath();
                    if(points == null) {
                        state = DungeonAIState.FINISHED;
                        return;
                    }
                    points.add(target);
                    currentPoint = 1;

                    
                }
                else if(gotAPath) {
                    moveTo(w);
                }
                
                break;
            case FINISHED:
                break;
        }
    }
    private void moveTo(World world) {
        owner.setNextAction(null);
        DungeonPoint ownerpos = owner.getPosition();
        if(ownerpos.equals(target)) {
            
            state=DungeonAIState.FINISHED;

            return;
        }
        if(currentPoint > points.size()-1) {
            
            
            state=DungeonAIState.FINISHED;
            return;
        }
        if(ownerpos.equals(points.get(currentPoint))) {
            currentPoint++;
        
        }
        Direction dir = ownerpos.getDirectionTo(points.get(currentPoint));
        
        
        owner.setNextAction(new DungeonAbilityMove(owner, world, dir));
        
        
    }
}
