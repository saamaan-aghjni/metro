// Is buggy, can't break out of the loop.  Fix later

import java.util.ArrayList;
import java.util.stream.IntStream;


/* Implementation of the Aldous-Broder Algorithm.  Please see <https://en.wikipedia.org/wiki/Maze_generation_algorithm>   */
class AldousBroder extends MazeGenerator
{    
    ArrayList<Boolean> visited=null;
    @Override
    public void generate(Grid  grid, DungeonPoint start, DungeonPoint end)
    {        
        visited = new ArrayList<>();
        DungeonUtil.fillArray(visited, grid.getRow()*grid.getCol(), false);        
        processMaze(grid, start, end);
        visited=null;
    }
    private void processMaze(Grid grid, DungeonPoint start, DungeonPoint end) {
        
        Cell current = grid.cellAt(new DungeonPoint(DungeonUtil.random.nextInt(start.getX(), end.getX()), DungeonUtil.random.nextInt(start.getY(), end.getY()) ));
        visited.set(DungeonUtil.positionToIndex(current.getLoc(), grid.getRow()), true);
        int visitedCount = 0;
        while(visitedCount < visited.size())
        {
            var neighbours = current.getNeighbours().toArray();
            var neighbourDir=(Direction)(neighbours[DungeonUtil.random.nextInt(0, neighbours.length)]);
            Cell neighbour = grid.getNeighbourOfCell(current.getLoc(), neighbourDir);
            if(!DungeonPoint.inRangePoint(new DungeonPoint(neighbour.getRow(), neighbour.getCol()), start, end)) {
                continue;
            }
            
            grid.link(current.getLoc(), neighbourDir);
            if(current.getTerrain()== Terrain.DIRT) {
                current.setTerrain(Terrain.RAIL);
            }
            if(visited.get(DungeonUtil.positionToIndex(neighbour.getLoc(), grid.getRow() ))) {
                current = neighbour;
                visited.set(DungeonUtil.positionToIndex(current.getLoc(), grid.getRow()), true);
            }
            else {
                current = grid.cellAt(new DungeonPoint(DungeonUtil.random.nextInt(start.getX(), end.getX()), DungeonUtil.random.nextInt(start.getY(), end.getY())));
                visited.set(DungeonUtil.positionToIndex(current.getLoc(), grid.getRow()), true);
            }
            visitedCount++;
        }            
    }
}
