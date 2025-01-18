import java.util.ArrayList;
import java.util.Stack;
import  java.util.stream.IntStream;

 public class Backtracker extends MazeGenerator {
    static Stack<Cell>  crums= new Stack<>();
    static ArrayList<ArrayList<Boolean>> visited = null;
@Override
    public  void generate(Grid g, DungeonPoint start, final DungeonPoint end)
    {

        visited = new ArrayList<ArrayList<Boolean>>();
            IntStream.range(0, g.getRow()).forEach( i -> {
            visited.add(new ArrayList<Boolean>(g.getCol()));
            IntStream.range(0, g.getCol()).forEach( j -> {
                visited.get(i).add(false);
            });
        });        
        
        ArrayList<Direction> allowedDirs= new ArrayList<>();        
        int x=start.getX() >= end.getX() ? end.getX() : random.nextInt(start.getX(), end.getX());
        int y=start.getY() >= end.getY() ? end.getY() : random.nextInt(start.getY(), end.getY());

        Cell temp=g.cellAt(x, y);
        crums.push(temp);
        while(!crums.isEmpty()) {
            temp = crums.pop();
            allowedDirs = unvisitedneighbours(temp, start, end);            
            if(allowedDirs.isEmpty()) {
                backtrack(g, start, end);
                continue;
            }
            Direction dir = allowedDirs.get(random.nextInt(0, allowedDirs.size()));
            Cell tempn=temp.getNeighbour(dir).neighbour();
            DungeonPoint p=new DungeonPoint(tempn.getRow(), tempn.getCol());           
            g.link(temp.getRow(), temp.getCol(), dir);
            if(temp.getTerrain() == Terrain.DIRT)  temp.setTerrain(Terrain.RAIL);
            crums.push(temp);
            
            crums.push(tempn);
            visited.get(tempn.getRow()).set(tempn.getCol(), true);
            visited.get(temp.getRow()).set(temp.getCol(), true);

        }
        visited = null;
crums.clear();
    }
    private static void backtrack(Grid g, DungeonPoint start, DungeonPoint end)
    {
        ArrayList<Direction> allowedDirs= null;
        
        if(crums.isEmpty())
            return;
        final Cell temp = crums.pop();
        allowedDirs = unvisitedneighbours(temp, start, end);

        if(allowedDirs.isEmpty()) {
             backtrack(g, start, end);            
             return;
        }
        Direction dir = allowedDirs.get(random.nextInt(0, allowedDirs.size()));
        g.link(temp.getRow(), temp.getCol(), dir);
        if(temp.getTerrain() == Terrain.DIRT) temp.setTerrain(Terrain.RAIL);
        crums.push(temp);
        Cell tempn=temp.getNeighbour(dir).neighbour();
        crums.push(tempn);
        visited.get(tempn.getRow()).set(tempn.getCol(), true);
        visited.get(temp.getRow()).set(temp.getCol(), true);

    }

    private static ArrayList<Direction> unvisitedneighbours(Cell c, DungeonPoint start, DungeonPoint end) {        
        ArrayList<Direction> dirs = new ArrayList<>();
        if(c.getNeighbours() == null) return null;
        for(Direction n: c.getNeighbours()) {
            Cell neighbour = c.getNeighbour(n).neighbour();
            if(visited.get(neighbour.getRow()).get(neighbour.getCol())==false && DungeonUtil.inRange(neighbour.getRow(), start.getX(), end.getX()) && DungeonUtil.inRange(neighbour.getCol(), start.getY(), end.getY())) {                
                dirs.add(n);
            }
        }
        return dirs;
    }
}
