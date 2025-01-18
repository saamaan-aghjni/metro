/**
 *A   typical implementation of AStar.  
 * Credits go to following articles to have described the concept and provided helpful gotchas
 * https://www.redblobgames.com/pathfinding/a-star/introduction.html
 * https://www.redblobgames.com/pathfinding/a-star/implementation.html#algorithm
 * https://web.archive.org/web/20171022224528/http://www.policyalmanac.org:80/games/aStarTutorial_de.html
 * https://web.archive.org/web/20170924024524/http://www.policyalmanac.org/games/heuristics.htm
 * https://web.archive.org/web/20170924024047/http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html
 * https://web.archive.org/web/20170905212915/http://realtimecollisiondetection.net/blog/?p=56
 */
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.LinkedList;

enum DungeonAStarState {
    SEARCHING,
    SUCCESS,
    CANCELED,
    FAILED,
    NOTSTARTED
}

@FunctionalInterface
interface DungeonAStarNeighbourhoodCalculator<T> {
    public ArrayList<T> getEdges(T p);
}

@FunctionalInterface
interface DungeonAStarHeuristicCalculator<T> {
    int calc(T point);
}

public class DungeonAStar<T> {
    private class AStarPoint {
        T point, parent;
        int gScore;
        public AStarPoint(T p, T par, int g) {
            this.gScore = g;
            this.point = p;
            this.parent = par;
        }
        public AStarPoint(T p, T par) {
            this(p, par, Integer.MAX_VALUE);
        }
        @Override
        public String toString() {
            return point.toString() +" with parent "+(parent == null ? " null " : parent.toString())+" with a gscore of "+gScore;
        }
    }
     T start, goal;
     DungeonAStarHeuristicCalculator h; private DungeonAStarNeighbourhoodCalculator<T> edgeCalc;
     DungeonAStarState state;
       PriorityQueue<AStarPoint > frontier=null;
     LinkedList<AStarPoint> closed = new LinkedList<>();
    private  DungeonAStar(T start, T goal,  DungeonAStarHeuristicCalculator h, DungeonAStarNeighbourhoodCalculator edges) {
        this.edgeCalc = edges;
        this.h = h;
        this.state = DungeonAStarState.NOTSTARTED;
        this.goal = goal;
        this.start = start;
        this.frontier = new PriorityQueue<AStarPoint>((p, q) -> { 
            int fq =  this.h.calc(q.point)+ q.gScore;
            int fp = this.h.calc(p.point) + p.gScore;
            return (fp > fq ? 1 : ( fp == fq ? 0 : -1)); 
        });
    }

    public static<T> DungeonAStar<T> newAStar(T start, T end, DungeonAStarNeighbourhoodCalculator<T> ncalc, DungeonAStarHeuristicCalculator<T> heuristic) throws NullPointerException {
        if(start == null || end == null) {
            throw new NullPointerException("Start and/or goal points are passed null!");
        }
        if(start.equals(end)) {
            throw new NullPointerException("Start and endpoints are equal! "+start.toString()+", "+end.toString());
        }
        return new DungeonAStar<T>(start, end, heuristic, ncalc);
    }
    public boolean initiate() {
        if(state != DungeonAStarState.NOTSTARTED) return false;
        state = DungeonAStarState.SEARCHING;
        frontier.offer(new AStarPoint(start, null, 0));
        return true;
    }
    public boolean procede() {
        if(state  != DungeonAStarState.SEARCHING) {
            return false;
        }
        if(frontier.isEmpty()) {
            state = DungeonAStarState.FAILED;
            return false;
        }
    
        var astarPoint = frontier.poll();
        if(astarPoint.point.equals(goal)) {
            state = DungeonAStarState.SUCCESS;
            return true;
        }
        if(!alreadyClosed(astarPoint.point))  closed.add(astarPoint);
        for(T neighbour: edgeCalc.getEdges(astarPoint.point)) {
            var astarNeighbour = alreadyFrontiered(neighbour);
            if(astarNeighbour!=null) {              
                int newFScore = astarPoint.gScore +1 + h.calc(astarNeighbour.point);
                int oldFScore = astarNeighbour.gScore + h.calc(astarNeighbour.point);
                if(newFScore < oldFScore) {
                    astarNeighbour.gScore = astarPoint.gScore+1;
                    astarNeighbour.parent = astarPoint.point;
                    frontier.remove(astarNeighbour);
                    frontier.add(astarNeighbour);
                }
            }
            else if(!alreadyClosed(neighbour)) {
                frontier.offer(new AStarPoint(neighbour, astarPoint.point, astarPoint.gScore+1));
            }
        }
        return true;
    }
    public boolean isDone() { 
        return state == DungeonAStarState.FAILED || state == DungeonAStarState.SUCCESS;
    }
    public ArrayList<T> retrievePath() {
        if(state != DungeonAStarState.SUCCESS) return null;
        ArrayList<T> points = new ArrayList<>();
        T last_parent = null;
        while(!closed.isEmpty()) {            
            var nextPoint = closed.poll();
            if(last_parent == null) {
                points.add(nextPoint.point);
                last_parent = nextPoint.point;
            }
            else if( last_parent.equals(nextPoint.parent)) {
                last_parent = nextPoint.point;
                points.add(nextPoint.point);
            }
        }
        return points;
    }
private boolean alreadyClosed(T p) {
        return closed.stream().anyMatch(c -> {        
            if(c.point.equals(p)) return true;
            return false;
        });
    }
    private AStarPoint alreadyFrontiered(T point) {
        for(AStarPoint p: frontier) {
            if(p.point.equals(point)) {
                return p;
            }
        }
        return null;
    }    
}
