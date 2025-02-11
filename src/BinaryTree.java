
public class BinaryTree extends MazeGenerator
{    
    @Override 
    public void generate(Grid grid, DungeonPoint start, DungeonPoint end)
    {
        for(int i=start.getX(); i< end.getX(); i++) {
            for(int j= end.getY()-1; j >=start.getY(); j--) {
                int choice=DungeonUtil.random.nextInt(1,3);
                if(choice==1) {
                    grid.link(new DungeonPoint(i, j), Direction.NORTH);
                }
                else if(choice==2) {
                    grid.link(new DungeonPoint(i, j), Direction.EAST);
                }
                if(i==0) {
                    grid.link(new DungeonPoint(i, j), Direction.WEST);
                }
                if(j==grid.getCol()-1) {
                    grid.link(new DungeonPoint(i, j), Direction.NORTH);
                }
                if(grid.cellAt(new DungeonPoint(i, j)).getTerrain() == Terrain.DIRT)  {
                    grid.cellAt(new DungeonPoint(i, j)).setTerrain(Terrain.RAIL);
                }
            }
        }
    }
}
