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
                nodes.add(new ABNode(i, j));
        ABNode current=null;
    Cell currentCell=null;
        while(nodes.stream().filter(e -> e.visited() ==false).count()>0)
        {
            current = nodes.get(rand.nextInt(0, nodes.size()));
            if(current.visited()==false) grid.link(current.getRow()-1, current.getCol()-1, Direction.values()[rand.nextInt(0,Direction.values().length)]);
            current.setVisited();
        }
    }
}
