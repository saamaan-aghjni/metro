import java.util.Random;
import java.util.stream.IntStream;

abstract class MazeGenerator
{
    protected static Random random = new Random();
    protected MazeGenerator() { }    
    public  abstract void generate(Grid grid, DungeonPoint start, DungeonPoint end);

    public void makeLoops(Grid g) {
        IntStream.range(0, g.getRow()).forEach( i -> {
            IntStream.range(0, g.getCol()).forEach( j -> {
                Cell temp = g.cellAt(i, j);
                var neighbours = temp.getLinkedneighbours();
                if(neighbours!=null && neighbours.size()==1 && random.nextInt(0,5)<=3) {
                    neighbours = temp.getUnlinkedneighbours();
                    g.link(temp.getRow(),temp.getCol(), neighbours.get(random.nextInt(0, neighbours.size())));
                }
            });
        });
    }
    public void fillDeadends(Grid g, int t)
    {
        if(t<1) return;
        IntStream.range(0, t).forEach(r -> {
            fillDeadendsTimes(g); 
        });
    }
    private void fillDeadendsTimes(Grid g) {
    for(int i=0; i<g.getRow(); i++)
    {
        for(int j=0; j<g.getCol(); j++) {
            Cell temp = g.cellAt(i, j);
            var neighbours = temp.getLinkedneighbours();
            if(neighbours == null) {
                continue;
            }
            if(neighbours.size()==1) {
                g.unlink(i, j, neighbours.get(0));
            }
        }
    }
}
}
