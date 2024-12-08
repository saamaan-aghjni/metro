import java.awt.Point;
import java.util.Random;

class Sidewinder extends MazeGenerator
{
	static private Random rand=new Random();
	private Sidewinder() { }
	static public void generate(Grid grid)
	{
		List<Point> runs=new ArrayList<>();
		for(int i=grid.getRow()-1; i>=0; i--)
		{
			for(int j=0; j<grid.getCol(); j++)
			{
				int choice=rand.nextInt(1,3);
				switch(choice)
				{
					case 1:
						if(i==0)
							grid.link(i, j, Direction.EAST);
						else if(runs.size()>0)
						{
							Point p=runs.get(rand.nextInt(0, runs.size()));
							grid.link((int)p.getX(), (int)p.getY(), Direction.NORTH);
							runs.clear(); //clear runs
						}
						break;
					case 2:
						if(j==grid.getCol()-1) 
							grid.link(i, j, Direction.NORTH);
						else
						{
							grid.link(i, j, Direction.EAST);
						}
							runs.add(new Point(i, j));
						break;
				}
			}
			runs.clear();
		}
	}
}
