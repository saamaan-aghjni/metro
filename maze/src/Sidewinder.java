import java.util.ArrayList;

class Sidewinder extends MazeGenerator
{
	@Override 
	public void generate(Grid grid, DungeonPoint start, DungeonPoint end)
	{

		ArrayList<DungeonPoint> runs=new ArrayList<>();
		for(int i= start.getX(); i<end.getX(); i++)
		{
			for(int j=end.getY()-1; j>=start.getY(); j--)
			{
				int choice=random.nextInt(1,3);
				switch(choice)
				{
					case 1:
						if(i==start.getX())
							grid.link(i, j, Direction.EAST);
						else if(runs.size()>0)
						{
							DungeonPoint p=runs.get(random.nextInt(0, runs.size()));
							grid.link((int)p.getX(), (int)p.getY(), Direction.NORTH);
                            if(grid.cellAt(p).getTerrain() == Terrain.DIRT) grid.cellAt(p).setTerrain(Terrain.RAIL);

							runs.clear(); //clear runs
						}
						break;
					case 2:
						if(j==end.getY()-1) {
							grid.link(i, j, Direction.NORTH);
                            if(grid.cellAt(new DungeonPoint(i, j)).getTerrain() == Terrain.DIRT) grid.cellAt(new DungeonPoint(i, j)).setTerrain(Terrain.RAIL);
}
						else
						{
							grid.link(i, j, Direction.EAST);
                            if(grid.cellAt(new DungeonPoint(i, j)).getTerrain() == Terrain.DIRT) grid.cellAt(new DungeonPoint(i, j)).setTerrain(Terrain.RAIL);

						}
							runs.add(new DungeonPoint(i, j));
						break;
				}
			}
			runs.clear();
		}
	}
}
