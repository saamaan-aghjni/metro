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
        Cell neighbour = null;
        int visitedneighbours = 0;
        Direction lastVisitedneighbour = null;
        
        while (true) {
            
            Direction dir = (Direction)temp.getNeighbours().toArray()[random.nextInt(0, temp.getNeighbours().size())];
            neighbour = temp.getNeighbour(dir).neighbour();
                if(visited.get(neighbour.getRow()).get(neighbour.getCol())==true && visitedneighbours<4) {
                if(lastVisitedneighbour==null || lastVisitedneighbour!= dir) {
                    lastVisitedneighbour = dir;
                    ++visitedneighbours;                    
                }
                continue;
            }
            if(visited.get(neighbour.getRow()).get(neighbour.getCol())==false && lastVisitedneighbour!=null ) {
                lastVisitedneighbour = null;
                visitedneighbours = 0;
            }
            if ( visitedneighbours>=temp.getNeighbours().size()-1 ) // Enter hunt mode 
            {
                visitedneighbours = 0;
                lastVisitedneighbour = null;
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
                
                for(var dir: temp.getNeighbours().toArray()) {
                    Cell neighbour = g.getNeighbourTo(i, j, (Direction)dir).neighbour();
                    if(DungeonUtil.inRange(neighbour.getRow(), start.getX(),  end.getX()) &&  DungeonUtil.inRange(neighbour.getCol(), start.getY(), end.getY()) && visited.get( neighbour.getRow()).get(neighbour.getCol())==true) {
                        allowedDirs.add((Direction)dir);
                    }
                }
                if( allowedDirs.isEmpty()) 
                    continue;

                
                    
                // link temp to one of the random visited neighbours
                g.link(temp.getRow(), temp.getCol(), allowedDirs.get(random.nextInt(0, allowedDirs.size())));
                if(temp.getTerrain() == Terrain.DIRT) temp.setTerrain(Terrain.RAIL);
                visited.get(temp.getRow()).set(temp.getCol(), true);

                return true;
            }
        }
    return false;
    }
}