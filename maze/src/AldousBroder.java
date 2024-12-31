// Is buggy, can't break out of the loop.  Fix later

import java.util.ArrayList;
// import java.util.concurrent.Delayed;

import java.util.stream.IntStream;


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
    ArrayList<Boolean> visited=null;
    @Override
    public void generate(Grid  grid, DungeonPoint start, DungeonPoint end)
    {        
        visited = new ArrayList<>();
        DungeonUtil.fillArray(visited, grid.getRow()*grid.getCol()+1, false);
System.out.println("Called "+start.toString()+" and "+end.toString()+" with indices of "+DungeonUtil.positionToIndex(start, grid.getRow())+" and "+DungeonUtil.positionToIndex(end, grid.getRow()));
        boolean done = false;
        int x=start.getX() >= end.getX() ? start.getX() : random.nextInt(start.getX(), end.getX());
        int y=start.getY() >= end.getY() ? start.getY() : random.nextInt(start.getY(), end.getY());
        Cell current = grid.cellAt(x, y);
         visited.set(DungeonUtil.positionToIndex(new DungeonPoint(x,y), grid.getRow()), true);
         /* done =IntStream.range(DungeonUtil.positionToIndex(start, grid.getRow()), DungeonUtil.positionToIndex(end, grid.getRow())).anyMatch(a -> { 
            System.out.println("point at "+a +" "+visited.get(a));
            return visited.get(a)== false; 
    });        
    */
    /* done =IntStream.range(DungeonUtil.positionToIndex(start, grid.getRow()), DungeonUtil.positionToIndex(end, grid.getRow())).anyMatch(a -> { 
        // System.out.println("point at "+a);
        return visited.get(a)==false; 
});        */
System.out.println(done);
        while(!done)
        {
            var neighbours = current.getNeighbors().toArray();
            var neighbourDir=(Direction)(neighbours[random.nextInt(0, neighbours.length)]);
            Cell neighbour = current.getNeighbor(neighbourDir).neighbor();
             if(!DungeonUtil.inRangePoint(new DungeonPoint(neighbour.getRow(), neighbour.getCol()), start, end)) {
                continue;
            }
            
            System.out.println("Current "+current.getRow()+" "+current.getCol()+" "+DungeonUtil.positionToIndex(new DungeonPoint(current.getRow(), current.getCol()), grid.getRow())+" neighbour at "+neighbour.getRow() +" "+neighbour.getCol()+" with an index of "+DungeonUtil.positionToIndex(new DungeonPoint(neighbour.getRow(), neighbour.getCol()), y));

            grid.link(current.getRow(), current.getCol(), neighbourDir);
System.out.println("linkage done!");            
            if(!visited.get(DungeonUtil.positionToIndex(new DungeonPoint(neighbour.getRow(), neighbour.getCol()), grid.getRow()))) {
                current = neighbour;
                 visited.set(DungeonUtil.positionToIndex(new DungeonPoint(current.getRow(), current.getCol()), grid.getRow()), true);
                // visited.remove(DungeonUtil.positionToIndex(new DungeonPoint(current.getRow(), current.getCol()), grid.getRow()));

            }
              else {
                x=start.getX() >= end.getX() ? start.getX() : random.nextInt(start.getX(), end.getX());
                y=start.getY() >= end.getY() ? start.getY() : random.nextInt(start.getY(), end.getY());
                current = grid.cellAt(x, y);
                System.out.println("Setting "+ current.getRow()+" "+current.getCol()+" at "+DungeonUtil.positionToIndex(new DungeonPoint(current.getRow(), current.getCol()), grid.getRow())+" to true");
                 visited.set(DungeonUtil.positionToIndex(new DungeonPoint(current.getRow(), current.getCol()), grid.getRow()), true);
                
            }
            done =IntStream.range(DungeonUtil.positionToIndex(start, grid.getRow()), DungeonUtil.positionToIndex(end, grid.getRow())).allMatch(a -> { 
                //  System.out.println("point at "+a);
                return a <= visited.size() && visited.get(a)==true; 
        });
        
        }
        
        visited = null;
    }
}
