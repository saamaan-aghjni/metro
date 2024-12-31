import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;


class HuntAndKill extends MazeGenerator
{
     static ArrayList< ArrayList<Boolean>> visited= null ;
@Override 
    public  void generate(Grid g, DungeonPoint start, DungeonPoint end  )
    {
        int x=start.getX() >= end.getX() ? start.getX() : random.nextInt(start.getX(), end.getX());
        int y=start.getY() >= end.getY() ? start.getY() : random.nextInt(start.getY(), end.getY());
        
        Cell startc=g.cellAt(x, y);
        visited = new ArrayList<ArrayList<Boolean>>(g.getRow());
        
        IntStream.range(0, g.getRow()).forEach( i -> {
            visited.add(new ArrayList<Boolean>(g.getCol()));
            IntStream.range(0, g.getCol()).forEach( j -> {
                visited.get(i).add(false);
            });
        });
        
        visited.get(startc.getRow()).set(startc.getCol(), true);
        Cell temp = startc;
        Cell neighbor = null;
        int visitedNeighbors = 0;
        Direction lastVisitedNeighbor = null;
        
        while (true) {
            
            Direction dir = (Direction)temp.getNeighbors().toArray()[random.nextInt(0, temp.getNeighbors().size())];
            neighbor = temp.getNeighbor(dir).neighbor();
                if(visited.get(neighbor.getRow()).get(neighbor.getCol())==true && visitedNeighbors<4) {
                if(lastVisitedNeighbor==null || lastVisitedNeighbor!= dir) {
                    lastVisitedNeighbor = dir;
                    ++visitedNeighbors;                    
                }
                continue;
            }
            if(visited.get(neighbor.getRow()).get(neighbor.getCol())==false && lastVisitedNeighbor!=null ) {
                lastVisitedNeighbor = null;
                visitedNeighbors = 0;
            }
            if ( visitedNeighbors>=temp.getNeighbors().size()-1 ) // Enter hunt mode 
            {
                visitedNeighbors = 0;
                lastVisitedNeighbor = null;
                if(hunt(g, start, end)==false) 
                    break;
            }
            else {
                x=start.getX() >= end.getX() ? start.getX() : random.nextInt(start.getX(), end.getX());
                y=start.getY() >= end.getY() ? start.getY() : random.nextInt(start.getY(), end.getY());
                temp = g.cellAt(x , y );
                g.link(temp.getRow(), temp.getCol(), dir);
                if(temp.getTerrain() == Terrain.DIRT) temp.setTerrain(Terrain.RAIL);
                visited.get(temp.getRow()).set(temp.getCol(), true);    
                    // continue;
                }         
            }            
        visited = null;
    }
    
    private static boolean hunt(Grid g, DungeonPoint start, DungeonPoint end)
    {
        
        if(visited == null ) 
            return false; // called inappropriately
        Cell result = null;
        for(int i=start.getX(); i<end.getX(); i++) {
            for(int j=start.getY(); j< end.getY(); j++) {
                boolean visitedCell = visited.get(i).get(j);
                if(visitedCell==true )
                    continue;
                Cell temp = g.cellAt(i, j);
                
                ArrayList<Direction> allowedDirs=new ArrayList<>();
                
                for(var dir: temp.getNeighbors().toArray()) {
                    Cell neighbor = g.getNeighborTo(i, j, (Direction)dir).neighbor();
                    if(DungeonUtil.inRange(neighbor.getRow(), start.getX(),  end.getX()) &&  DungeonUtil.inRange(neighbor.getCol(), start.getY(), end.getY()) && visited.get( neighbor.getRow()).get(neighbor.getCol())==true) {
                        allowedDirs.add((Direction)dir);
                    }
                }
                if( allowedDirs.isEmpty()) 
                    continue;

                
                    
                // link temp to one of the random visited neighbors
                g.link(temp.getRow(), temp.getCol(), allowedDirs.get(random.nextInt(0, allowedDirs.size())));
                if(temp.getTerrain() == Terrain.DIRT) temp.setTerrain(Terrain.RAIL);
                visited.get(temp.getRow()).set(temp.getCol(), true);

                return true;
            }
        }
    return false;
    }
}