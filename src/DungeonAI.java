/**
 *  Implementation of an utility-based AI.
 * http://robert.zubek.net/publications/Needs-based-AI-draft.pdf
 * https://www.roguebasin.com/index.php/Need_driven_AI
 * https://www.roguebasin.com/index.php/A_Better_Monster_AI
 * https://www.roguebasin.com/index.php/Plug-In_Monster_AI
 * https://www.gamedev.net/tutorials/programming/artificial-intelligence/the-total-beginners-guide-to-game-ai-r4942/
 * https://news.ycombinator.com/item?id=22848106
 **/
import java.io.Serializable;
import java.util.HashMap;

enum DungeonAIState {
    IDLE, WORKING, FINISHED
}
class DungeonAIManager implements Serializable {
    private HashMap<DungeonEntity, DungeonAI> ais= new HashMap<>();
    public DungeonAIManager() {

    }
    private void addAI(DungeonEntity ent, DungeonAI ai) {
        
        ais.put(ent, ai);

    }
    public void removeAI(DungeonEntity ent) {
        if(ent.hasAI() && ais.get(ent)!=null) {
            ais.remove(ent);
        }
    }
    public void update(DungeonEntity entity, World w) {
        if(entity.getRace() == DungeonEntityRace.DOOR) {
            return;
        }        
        if( (entity.getNextAction()  == null && entity.getLastActionResult() != null && entity.getLastActionResult().result() == DungeonComponentResultType.FAILURE) || (ais.get(entity) ==null || ais.get(entity).getState() == DungeonAIState.FINISHED)) {
            entity.setLastActionResult(null);
            updateAIScore(entity, w);
            
        }
        

        var ai=ais.get(entity);
        if(ai==null ) {
            return;
        }
        
        ai.act(w);
        if(ai.getState() == DungeonAIState.FINISHED) {
            ais.remove(entity);
        }
    }
    private void updateAIScore(DungeonEntity e, World w) {
        
        var ailist=DungeonAIFactory.generateAIs(e, w);
        if(ailist.isEmpty()) {
            
            return;
        }
        Double score = Double.NEGATIVE_INFINITY;
        DungeonAI bestAI = null;
        for(DungeonAI ai: ailist) {
            
            if(ai.getScore(w) > score) {
                score = ai.getScore(w);
                bestAI = ai;
            }
        }
        addAI(e, bestAI);
        
    }
}

abstract class DungeonAI implements Serializable {

    protected DungeonAIState state = DungeonAIState.IDLE;
    protected DungeonEntity owner;
    protected DungeonAI(DungeonEntity ent) {
        owner = ent;
    }

    public DungeonAIState getState() {
        return state;
    }

    public double getScore(World world) {
        return 0.0;
    }    
    public abstract void act(World world); // Perform action
}

