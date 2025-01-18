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
    private static Map<Terrain, Boolean> PASSABLE = Map.of(
        DIRT, true,
        CONCRETE, true,
        RAIL, true
    );
    public static boolean isPassable(Terrain ter) {
        return PASSABLE.containsKey(ter) && PASSABLE.get(ter);
    } 
    public char getSymbol() { return symbol; }
    public double getCost() { return cost; }
}
class Cell
{
        public class Link
        {
            private Cell neighbour;
            private boolean hasPath;
            Link(Cell neighbour, Boolean hasPath)
            {
                this.hasPath=hasPath;
                this.neighbour = neighbour;
            }
            public Cell neighbour() { return neighbour; }
            public boolean hasPath() { return hasPath; }
        }
    private Map<Direction, Link> neighbours=new HashMap<>();
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
    public void addneighbour(Cell other, Direction dir, boolean hasPath)
    {
        if(neighbours.containsKey(dir)) {
            return; 
    }
        Link l=new Link(other,  hasPath);
        neighbours.put(dir, l);
        other.addneighbour(this, Direction.getOpositeOf(dir), hasPath);
    }
    public void unlinkneighbour(Cell other, Direction dir)
    {
        other.neighbours.replace(Direction.getOpositeOf(dir), new Link(this, false));
        neighbours.replace(dir, new Link(other, false));
    }
    public void linkneighbour(Cell other, Direction dir)
    {
        other.neighbours.replace(Direction.getOpositeOf(dir),  new Link(this, true));
        neighbours.replace(dir, new Link(other, true));
    }
/*	public void linkneighbour(Cell other)
	{
        Direction dir=neighbours.get(other).dir();
        other.neighbours.replace(Direction.getdir, new Link(Direction.getOpositeOf(dir), true));
        neighbours.replace(other, new Link(dir, true));
    }
*/

    public Link getNeighbour(Direction dir)
	{
		return neighbours.containsKey(dir) ? neighbours.get(dir) : null;
	}
	public Set<Direction> getNeighbours()
	{
		return neighbours.keySet();
    }
    public ArrayList<Direction> getLinkedneighbours() {
        ArrayList<Direction> tempcells =  new ArrayList<>();
        for(var n: neighbours.keySet()) {
            if(neighbours.get(n).hasPath()) {
                tempcells.add(n);
            }
        }
        return tempcells.isEmpty() ? null : tempcells;
    }
    public ArrayList<Direction> getUnlinkedneighbours() {
        ArrayList<Direction> tempcells =  new ArrayList<>();
        for(var n: neighbours.keySet()) {
            if(!neighbours.get(n).hasPath()) {
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
st+="Cell at ("+getRow()+", "+getCol()+")\n    Number of neighbours: "+neighbours.size()+"\n";
for(var neighbour: neighbours.keySet())
    st+="\t\tneighbour at ("+neighbours.get(neighbour).neighbour().getRow()+", "+neighbours.get(neighbour).neighbour().getCol()+"), direction "+neighbour+", has path? "+neighbours.get(neighbour).hasPath()+"\n";
return st;
}
}
