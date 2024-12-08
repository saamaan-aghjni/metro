import java.util.Set;
import java.util.HashMap;
import java.util.Map;
enum Direction{
    EAST,
    WEST,
    NORTH,
    SOUTH,
    NORTHEAST,
    SOUTHWEST,
    NORTHWEST,
    SOUTHEAST;
	static Direction getOpositeOf(Direction dir)
	{
		if(dir==NORTH) return SOUTH;
		if(dir==SOUTH) return NORTH;
		if(dir==NORTHWEST) return SOUTHEAST;
		if(dir==NORTHEAST) return SOUTHWEST;
		if(dir==WEST) return EAST;
		if(dir==EAST) return WEST;
		if(dir==SOUTHWEST) return NORTHEAST;
		if(dir==SOUTHEAST) return NORTHWEST;
		return null;
	}
}



class Cell
{
        public class Link
        {
            private Cell neighbor;
            private boolean hasPath;
            Link(Cell neighbor, Boolean hasPath)
            {
                this.hasPath=hasPath;
                this.neighbor = neighbor;
            }
            public Cell neighbor() { return neighbor; }
            public boolean hasPath() { return hasPath; }
        }
    private Map<Direction, Link> neighbors=new HashMap<>();
	private int row, col;
    public Cell(int row, int col)
    {
		this.row = row;
		this.col = col;

    }

    public void addNeighbor(Cell other, Direction dir, boolean hasPath)
    {
    if(neighbors.containsKey(dir))
        return; 
        Link l=new Link(other,  hasPath);
        neighbors.put(dir, l);
        other.addNeighbor(this, Direction.getOpositeOf(dir), hasPath);
    }
	public void unlinkNeighbor(Cell other, Direction dir)
	{
        other.neighbors.replace(Direction.getOpositeOf(dir), new Link(this, false));
        neighbors.replace(dir, new Link(other, false));
    }
	public void linkNeighbor(Cell other, Direction dir)
	{
        other.neighbors.replace(Direction.getOpositeOf(dir),  new Link(this, true));
        neighbors.replace(dir, new Link(other, true));
    }
/*	public void linkNeighbor(Cell other)
	{
        Direction dir=neighbors.get(other).dir();
        other.neighbors.replace(Direction.getdir, new Link(Direction.getOpositeOf(dir), true));
        neighbors.replace(other, new Link(dir, true));
    }
*/

    public Link getNeighbor(Direction dir)
	{
		return neighbors.containsKey(dir) ? neighbors.get(dir) : null;
	}
	public Set<Direction> getNeighbors()
	{
		return neighbors.keySet();
	}
int getRow() { return row; }
int getCol() { return col ; }
@Override
public String toString()
{
String st="";
st+="Cell at ("+getRow()+", "+getCol()+")\n    Number of neighbors: "+neighbors.size()+"\n";
for(var neighbor: neighbors.keySet())
    st+="\t\tNeighbor at ("+neighbors.get(neighbor).neighbor().getRow()+", "+neighbors.get(neighbor).neighbor().getCol()+"), direction "+neighbor+", has path? "+neighbors.get(neighbor).hasPath()+"\n";
return st;
}
}
