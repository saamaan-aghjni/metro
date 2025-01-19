import java.util.ArrayList;

class Sidewinder extends MazeGenerator
{
private void carveNorth(Grid grid, ArrayList<DungeonPoint> runs, DungeonPoint point, DungeonPoint start, DungeonPoint end) {
	if(point.getY()==end.getY()-1) {
		
		grid.link(point, Direction.NORTH);
		if(grid.cellAt(point).getTerrain() == Terrain.DIRT) { 
			grid.cellAt(point).setTerrain(Terrain.RAIL);
		}
	}
	else
	{
		
		
		grid.link(point, Direction.EAST);
		if(grid.cellAt(point).getTerrain() == Terrain.DIRT) {
			grid.cellAt(point).setTerrain(Terrain.RAIL);
		}
	}
		runs.add(point);

}
	private void carveEast(Grid grid, DungeonPoint p, ArrayList<DungeonPoint> runs, DungeonPoint start, DungeonPoint end) {
		if(p.getX() != start.getX()-1) {
			grid.link(p, Direction.EAST);
		}
		else if(runs.size()>0)
		{
			DungeonPoint prand=runs.get(DungeonUtil.random.nextInt(0, runs.size()));
			grid.link(prand, Direction.NORTH);
			if(grid.cellAt(prand).getTerrain() == Terrain.DIRT)  {
				grid.cellAt(prand).setTerrain(Terrain.RAIL);

			}
			
		}
	}

    @Override 
    public void generate(Grid grid, DungeonPoint start, DungeonPoint end)
    {

        ArrayList<DungeonPoint> runs=new ArrayList<>();
        for(int i= start.getX(); i<end.getX(); i++)
        {
            for(int j=end.getY()-1; j>=start.getY(); j--)
            {
                int choice=DungeonUtil.random.nextInt(1,3);
                switch(choice)
                {
                    case 1:
                    	carveEast(grid, new DungeonPoint(i, j), runs, start, end);
                    	break;
                    case 2:
						carveNorth(grid, runs, new DungeonPoint(i, j), start, end);
						break;
                }
            }
			runs.clear();
        }
    }
}
