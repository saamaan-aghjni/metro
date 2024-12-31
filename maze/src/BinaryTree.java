
public class BinaryTree extends MazeGenerator
{    
    public void generate(Grid grid, DungeonPoint start, DungeonPoint end)
    {
        start = DungeonUtil.minimum(start, end);
        end = DungeonUtil.maximum(start, end);
        for(int i=start.getX(); i< end.getX(); i++)
            for(int j= end.getY()-1; j >=start.getY(); j--)
            {
                int choice=random.nextInt(1,3);
if(choice==1) grid.link(i, j, Direction.NORTH);
if(choice==2) grid.link(i, j, Direction.EAST);
if(i==0) grid.link(i, j, Direction.WEST);
if(j==grid.getCol()-1) grid.link(i, j, Direction.NORTH);
                if(grid.cellAt(i, j).getTerrain() == Terrain.DIRT)  grid.cellAt(i, j).setTerrain(Terrain.RAIL);
            }
    }
}
