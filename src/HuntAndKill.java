import java.util.ArrayList;



class HuntAndKill extends MazeGenerator
{
    private ArrayList<Boolean> visited =new ArrayList<>();
    @Override 
    public  void generate(Grid g, DungeonPoint start, DungeonPoint end  )
    {
        Cell startc = g.cellAt(new DungeonPoint(DungeonUtil.random.nextInt(start.getX(), end.getX()), DungeonUtil.random.nextInt(start.getY(), end.getY())));
        DungeonUtil.fillArray(visited, g.getCol() * g.getRow(), false);
        visited.set(DungeonUtil.positionToIndex(startc.getLoc(), g.getRow()), true);
        doHunt(g, start, end, startc);
        visited.clear();

    }
    private void doHunt(Grid g, DungeonPoint start, DungeonPoint end, Cell currentCell) {
        Cell temp = currentCell;
        Cell neighbour = null;
        int visitedneighbours = 0;
        Direction lastVisitedNeighbour=null;
        while(true) {
            Direction dir = (Direction)temp.getNeighbours().toArray()[DungeonUtil.random.nextInt(0, temp.getNeighbours().size())];
            neighbour = g.getNeighbourOfCell(temp.getLoc(), dir);
            if(visited.get(DungeonUtil.positionToIndex(neighbour.getLoc(), g.getRow())) == true && visitedneighbours<4) {
                if(lastVisitedNeighbour==null || lastVisitedNeighbour!= dir) {
                    lastVisitedNeighbour = dir;
                    ++visitedneighbours;                    
                }
                continue;
            }
            if( visited.get(DungeonUtil.positionToIndex(neighbour.getLoc(), g.getRow()))==false && lastVisitedNeighbour!=null ) {
                lastVisitedNeighbour = null;
                visitedneighbours = 0;
            }
            if ( visitedneighbours>=temp.getNeighbours().size()-1 ) // Enter hunt mode 
            {
                visitedneighbours = 0;
                lastVisitedNeighbour = null;
                if(hunt(g, start, end)==false) {
                    break;
                }
            }
            else {
                temp = g.cellAt(new DungeonPoint(DungeonUtil.random.nextInt(start.getX(), end.getX()), DungeonUtil.random.nextInt(start.getY(), end.getY())));
                dir = (Direction)temp.getNeighbours().toArray()[DungeonUtil.random.nextInt(0, temp.getNeighbours().size())];
                
                g.link(temp.getLoc(), dir);
                if(temp.getTerrain() == Terrain.DIRT) {
                    temp.setTerrain(Terrain.RAIL);
                }
                visited.set(DungeonUtil.positionToIndex(temp.getLoc(), g.getRow()), true);    
            }         
        }            
    }
    
    private  boolean hunt(Grid g, DungeonPoint start, DungeonPoint end)
    {
        
        if(visited == null ) {
            return false; // called inappropriately
        }
        Cell result = null;
        for(int i=start.getX(); i<end.getX(); i++) {
            for(int j=start.getY(); j< end.getY(); j++) {
                boolean visitedCell = visited.get(DungeonUtil.positionToIndex(new DungeonPoint(i, j), g.getRow()));
                if(visitedCell==true )
                {
                    continue;
                }
                Cell temp = g.cellAt(new DungeonPoint(i, j));
                ArrayList<Direction> allowedDirs=new ArrayList<>();
                for(var dir: temp.getNeighbours().toArray()) {
                    Cell neighbour = g.getNeighbourOfCell(new DungeonPoint(i, j), (Direction)dir);
                    if(visited.get(DungeonUtil.positionToIndex(neighbour.getLoc(), g.getRow()))==true) {
                        allowedDirs.add((Direction)dir);
                    }
                }
                if( allowedDirs.isEmpty()) {
                    continue;
                }
                g.link(temp.getLoc(), allowedDirs.get(DungeonUtil.random.nextInt(0, allowedDirs.size())));
                if(temp.getTerrain() == Terrain.DIRT) {
                    temp.setTerrain(Terrain.RAIL);
                }
                visited.set(DungeonUtil.positionToIndex(temp.getLoc(), g.getRow()), true);

                return true;
            }
        }
    return false;
    }
}