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

record Link(Direction dir, boolean hasPath)
{

}

class Cell
{
    private Map<Cell, Link> neighbors=new HashMap<>();
	private int row, col;
    public Cell(int row, int col)
    {
		this.row = row;
		this.col = col;

    }

    public void addNeighbor(Cell other, Direction dir, boolean hasPath)
    {
    if(neighbors.containsKey(other))
        return; 
        Link l=new Link(dir, hasPath);
        neighbors.put(other, l);
        other.addNeighbor(this, Direction.getOpositeOf(dir), hasPath);
    }
	public void unlinkNeighbor(Cell other, Direction dir)
	{
        other.neighbors.replace(this, new Link(Direction.getOpositeOf(dir), false));
        neighbors.replace(other, new Link(dir, false));
    }
/*
	public void linkNeighbor(Direction dir)
	{
        try 
	        {
            neighbors.get(new Link(Direction.getOpositeOf(dir) , false)).neighbors.replace(this, new Link(Direction.getOpositeOf(dir), true));
            neighbors.replace(neighbors.get(new Link(dir, false)), new Link(dir, true));
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
	public void unlinkNeighbor(Direction dir)
	{
        try {
            neighbors.get(new Link(Direction.getOpositeOf(dir) , true)).neighbors.replace(this, new Link(Direction.getOpositeOf(dir), false));
            neighbors.replace(neighbors.get(new Link(dir, true)), new Link(dir, false));
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
*/

	public void linkNeighbor(Cell other, Direction dir)
	{
        other.neighbors.replace(this, new Link(Direction.getOpositeOf(dir), true));
        neighbors.replace(other, new Link(dir, true));
    }
	public void linkNeighbor(Cell other)
	{
        Direction dir=neighbors.get(other).dir();
        other.neighbors.replace(this, new Link(Direction.getOpositeOf(dir), true));
        neighbors.replace(other, new Link(dir, true));
    }


    public Link getNeighbor(Cell other)
	{
		return neighbors.containsKey(other) ? neighbors.get(other) : null;
	}
	public Set<Cell> getNeighbors()
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
    st+="\t\tNeighbor at ("+neighbor.getRow()+", "+neighbor.getCol()+"), direction "+neighbors.get(neighbor).dir()+", has path? "+neighbors.get(neighbor).hasPath()+"\n";
return st;
}
}