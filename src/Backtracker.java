import java.util.ArrayList;
import java.util.Stack;
import  java.util.stream.IntStream;

 public class Backtracker extends MazeGenerator {
    private Stack<DungeonPoint>  crums= new Stack<>();
    ArrayList<Boolean> visited = null;
    private int rowLength=0;
    @Override
    public  void generate(Grid g, DungeonPoint start, DungeonPoint end)
    {
        visited = new ArrayList<>();
        DungeonUtil.fillArray(visited, g.getRow() * g.getCol(), false);        
        ArrayList<Direction> allowedDirs= new ArrayList<>();        
        DungeonPoint temp = null;        
        
        crums.push(DungeonPoint.getRandomPointBetween(start, end));
        rowLength = g.getRow();
        while(!crums.isEmpty()) {
            temp = crums.pop();
            allowedDirs = unvisitedneighbours(g, temp, start, end);            
            if(allowedDirs.isEmpty()) {
                backtrack(g, start, end);
                continue;
            }
            Direction dir = allowedDirs.get(DungeonUtil.random.nextInt(0, allowedDirs.size()));
            Cell tempn= g.getNeighbourOfCell(temp, dir);
            g.link(temp, dir);
            if(g.cellAt(temp).getTerrain() == Terrain.DIRT)  {
                g.cellAt(temp).setTerrain(Terrain.RAIL);
            }
            crums.push(temp);

            crums.push(tempn.getLoc());
            visited.set(DungeonUtil.positionToIndex(tempn.getLoc(), rowLength), true);
            visited.set(DungeonUtil.positionToIndex(temp, rowLength), true);
        }
    }

    private void backtrack(Grid g, DungeonPoint start, DungeonPoint end)
    {
        ArrayList<Direction> allowedDirs= null;
        
        if(crums.isEmpty()) {
            return;
        }
        var temp = crums.pop();
        allowedDirs = unvisitedneighbours(g, temp, start, end);

        if(allowedDirs.isEmpty()) {
            backtrack(g, start, end);            
            return;
        }
        crums.push(temp);
    }

    private ArrayList<Direction> unvisitedneighbours(Grid g, DungeonPoint c, DungeonPoint start, DungeonPoint end) {        
        ArrayList<Direction> dirs = new ArrayList<>();
        if(g.cellAt(c).getNeighbours() == null) {
            return null;
        }
        for(Direction n: g.cellAt(c).getNeighbours()) {
            Cell neighbour = g.getNeighbourOfCell(c, n);
            if(visited.get(DungeonUtil.positionToIndex(neighbour.getLoc(), rowLength)) ==false) {                
                dirs.add(n);
            }
        }
        return dirs;
    }
}
