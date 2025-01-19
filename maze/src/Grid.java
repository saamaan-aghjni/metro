/* See cell.java 
 * Hak-ish implementation of a graph datastructure
 * TODO:  Improve
 */
import java.io.Serializable;
import java.util.ArrayList;

public class Grid implements Serializable
{
    private ArrayList<Cell> cells;
    
    private final DungeonPoint bottom, top;
    Grid(int row, int col)
    {
        bottom = new DungeonPoint(0, 0);
        top = new DungeonPoint(row, col);
        cells =  new ArrayList<>();
        DungeonUtil.fillArray(cells, col*row, null);
        for(int i=0; i<row; i++) {
            for(int j=0; j<col; j++) {
                cells.set(DungeonUtil.positionToIndex(new DungeonPoint(i, j), row), new Cell(i, j));
            }
        }        
        initCells();
    }
    private void initCells()
    {
        
        for(int i=bottom.getX(); i<top.getX(); i++) {
            for(int j=bottom.getY(); j<top.getY(); j++)
            {
                Cell tmp = cells.get(DungeonUtil.positionToIndex(new DungeonPoint(i, j), top.getX()));
                if(i-1>=0) {
                    tmp.addNeighbour(cells.get(DungeonUtil.positionToIndex(new DungeonPoint(i-1,j), top.getX())), Direction.NORTH, false);
                }
                if(i+1<top.getX()) {
                    tmp.addNeighbour(cells.get(DungeonUtil.positionToIndex(new DungeonPoint(i+1, j), top.getX())), Direction.SOUTH, false);
                }
                if(j-1>=0) {
                    tmp.addNeighbour(cells.get(DungeonUtil.positionToIndex(new DungeonPoint(i, j-1), top.getX())), Direction.WEST, false);
                }
                if(j+1< top.getY()) {
                    tmp.addNeighbour(cells.get(DungeonUtil.positionToIndex(new DungeonPoint(i, j+1), top.getX())), Direction.EAST, false);
                }
                // Diagonals are too much of a headache to debug on Braille-Display, sorry :()
            }
        }
    }
    /* Link the given point to its neighbour
     * 
      */
    void link(DungeonPoint point, Direction dir) throws IndexOutOfBoundsException
    {        
        if(!DungeonPoint.inRangePoint(point, bottom, top))
        {
            throw new IndexOutOfBoundsException("point out of range");
        }
            cells.get(DungeonUtil.positionToIndex(point, top.getX())).linkNeighbour(dir);
    }
/* Unlinks a neighbour  
*/
    public void unlink(DungeonPoint point, Direction dir) throws IndexOutOfBoundsException
    {
        if(!DungeonPoint.inRangePoint(point, bottom, top)) {
            throw new IndexOutOfBoundsException();
        }
        cells.get(DungeonUtil.positionToIndex(point, top.getX())).unlinkNeighbour(dir);

    }

    @Override
    public String toString()
    {
        String b=new String();
        for(int i=bottom.getX(); i<top.getX(); i++)
        {
            b+="\n";
            for(int j=bottom.getY(); j<top.getY(); j++)
            {
                if(j==0)    b+="|";
                b+=" * ";
                if(j==top.getY()-1) b+="|";
                else
                {
                    if(!cells.get(DungeonUtil.positionToIndex(new DungeonPoint(i, j), top.getX())).getNeighbour(Direction.EAST).hasPath()) b+="|";
else b+=" ";
                }

            }
            if(i<top.getX()-1)
        {
            b+="\n|";
            for(int j=bottom.getY(); j<top.getY(); j++)
            {
                if(!cellAt(new DungeonPoint(i, j)).getNeighbour(Direction.SOUTH).hasPath()) b+="----|";
                else if(cellAt(new DungeonPoint (i,j)).getNeighbour(Direction.SOUTH).hasPath()) b+="   |";

            }
}
        }
        return b;
    }
    int getRow() { 
        return top.getX(); 
    }
    int getCol() { 
        return top.getY(); 
    }
    /* Returns the link - if any - between the cell and the given direction */
    public Cell.Link getNeighbourTo(DungeonPoint p, Direction dir) throws IndexOutOfBoundsException
    {
        return cells.get(DungeonUtil.positionToIndex(p, top.getX())).getNeighbour(dir);
    }

    public Cell cellAt(DungeonPoint point) throws IndexOutOfBoundsException {
        if(!DungeonPoint.inRangePoint(point, bottom, top)) {
            throw new IndexOutOfBoundsException("Cell out of bounds: "+point.toString()+", top: "+top.toString()+", bottom: "+bottom.toString());
        }
        return cells.get(DungeonUtil.positionToIndex(point, top.getX()));
    }
}
