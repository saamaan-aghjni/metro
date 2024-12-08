import java.util.Random;
import java.util.ArrayList;


class ABNode
{
    private int row, col;
    private boolean visited;
    ABNode(int row, int col)
    {
        this.row = row;
        this.col =col;
        this.visited=false;
    }
//    public Cell getCell() { return cell; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean visited() { return visited; }
    public void setVisited() { visited=true; }
    @Override
    public boolean equals(Object other)
    {
        if(other==null || !(other instanceof ABNode)) return false;
        ABNode oth=(ABNode)other;
        return oth.row == this.row && oth.col == this.col ;
    }
}

class AldousBroder extends MazeGenerator
{
    private static Random rand=new Random();
    private AldousBroder() { }
    public static void generate(Grid  grid)
    {
        ArrayList<ABNode> nodes=new ArrayList<>();
        for(int i=0; i<grid.getRow(); i++)
            for(int j=0; j<grid.getCol(); j++)
                nodes.add(new ABNode(i+1, j+1));
        ABNode current =  nodes.get(rand.nextInt(0, nodes.size()));
            current.setVisited();
        while(nodes.stream().filter(e -> e.visited() ==false).count()>0)
        {
            var neighborDir=Direction.values()[rand.nextInt(0, 4)];
            Cell.Link linkNeighbor=grid.getNeighborTo(current.getRow(), current.getCol(), neighborDir);
            if(linkNeighbor==null) continue;
Cell neighbor=linkNeighbor.neighbor();

//            try 
{

                int ABNodeNeighbor=nodes.indexOf(new ABNode(neighbor.getRow(), neighbor.getCol()));
if(nodes.get(ABNodeNeighbor).visited()==false)
                grid.link(current.getRow()-1, current.getCol()-1, neighborDir);

            current=nodes.get(ABNodeNeighbor);

            current.setVisited();

            }
//            catch(Exception e)
            {
                continue;
            }
        }
    }
}
