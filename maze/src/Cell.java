import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
enum Terrain {    
    DIRT(0.0, 'd'),
    CONCRETE(0.2, 'c'),
    RAIL(0.3, 'r'),
    MUSHROOM_PATCH(0.2, 'm'),
    WOODEN_DOOR(1.0, 'w'),
    METAL_DOOR(1.0, 'm'),
    STONE_WALL(1.0, 'W');
    double cost;
    char symbol;
    private Terrain(double cost, char c) {
        this.cost = cost;
        this.symbol = c;
    }
    public  static Map<Terrain, Boolean> PASSABLE = Map.of(
        DIRT, true,
        CONCRETE, true,
        RAIL, true
    );

    public char getSymbol() { return symbol; }
    public double getCost() { return cost; }
}
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
    Terrain terrain;
    public Cell(int row, int col)
    {
        this.row = row;
        this.col = col;
        terrain = Terrain.DIRT; // default
    }
    public Terrain getTerrain() { 
        return terrain;
    }
    public void setTerrain(Terrain ter) {
        this.terrain = ter;
    }
    public void addNeighbor(Cell other, Direction dir, boolean hasPath)
    {
        if(neighbors.containsKey(dir)) {
            return; 
    }
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
    public ArrayList<Direction> getLinkedNeighbors() {
        ArrayList<Direction> tempcells =  new ArrayList<>();
        for(var n: neighbors.keySet()) {
            if(neighbors.get(n).hasPath()) {
                tempcells.add(n);
            }
        }
        return tempcells.isEmpty() ? null : tempcells;
    }
    public ArrayList<Direction> getUnlinkedNeighbors() {
        ArrayList<Direction> tempcells =  new ArrayList<>();
        for(var n: neighbors.keySet()) {
            if(!neighbors.get(n).hasPath()) {
                tempcells.add(n);
            }
        }
        return tempcells.isEmpty() ? null : tempcells;
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
