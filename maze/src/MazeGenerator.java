import java.util.stream.IntStream;

abstract class MazeGenerator
{
    
    protected MazeGenerator() { }    
    public  abstract void generate(Grid grid, DungeonPoint start, DungeonPoint end);

    public void makeLoops(Grid g) {
        IntStream.range(0, g.getRow()).forEach( i -> {
            IntStream.range(0, g.getCol()).forEach( j -> {
                Cell temp = g.cellAt(new DungeonPoint(i, j));
                var neighbours = temp.getLinkedneighbours();
                if(neighbours!=null && neighbours.size()==1 && DungeonUtil.random.nextInt(0,5)<=4) {
                    neighbours = temp.getUnlinkedneighbours();
                    g.link(new DungeonPoint(temp.getRow(), temp.getCol()), neighbours.get(DungeonUtil.random.nextInt(0, neighbours.size())));
                }
            });
        });
    }
    /* Iteratively tries to fill dead-ends */
    public void fillDeadends(Grid grid, int times)
    {
        if(times<1) return;
        IntStream.range(0, times).forEach(r -> {
            fillDeadendsTimes(grid); 
        });
    }
    private void fillDeadendsTimes(Grid g) {
    for(int i=0; i<g.getRow(); i++)
    {
        for(int j=0; j<g.getCol(); j++) {
            Cell temp = g.cellAt(new DungeonPoint(i, j));
            var neighbours = temp.getLinkedneighbours();
            if(neighbours == null) {
                continue;
            }
            if(neighbours.size()==1) {
                g.unlink(new DungeonPoint(i, j), neighbours.get(0));
            }
        }
    }
}
}
