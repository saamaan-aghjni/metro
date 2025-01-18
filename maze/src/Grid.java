
public class Grid
{
    private Cell[][] cells;
    int max_row, max_col;
    private final DungeonPoint BOTTOM, TOP;
    Grid(int row,int col)
    {
        max_row = row;
        max_col = col;
        BOTTOM = new DungeonPoint(0, 0);
        TOP = new DungeonPoint(max_row, max_col);
        cells=new Cell[max_row][max_col];
        for(int i=0; i<max_row; i++)
        for(int j=0; j<max_col; j++)
            cells[i][j]=new Cell(i, j);
        initCells();
    }
    void initCells()
    {
        for(int i=0; i<max_row; i++)
            for(int j=0; j<max_col; j++)
            {
                if(i-1>=0) 
                    cells[i][j].addneighbour(cells[i-1][j], Direction.NORTH, false);
                if(i+1<max_row) 
                    cells[i][j].addneighbour(cells[i+1][j], Direction.SOUTH, false);
                if(j-1>=0) 
                    cells[i][j].addneighbour(cells[i][j-1], Direction.WEST, false);
                if(j+1<max_col) 
                    cells[i][j].addneighbour(cells[i][j+1], Direction.EAST, false);
                // if(i-1>=0 && j-1>=0) 
                    // cells[i][j].addneighbour(cells[i-1][j-1], Direction.NORTHWEST, false);
                // if(i-1>=0 && j+1<max_col)
                    // cells[i][j].addneighbour(cells[i-1][j+1], Direction.NORTHEAST, false);
                // if(i+1<max_row && j+1<max_col)
                    // cells[i][j].addneighbour(cells[i+1][j+1], Direction.SOUTHEAST, false);
                // if(i+1<max_row && j-1>=0)
                    // cells[i][j].addneighbour(cells[i+1][j-1], Direction.SOUTHWEST, false);
                }
    }

 
    void link(int row1, int col1, Direction dir)
    {        
        if(row1>=0 && row1<=max_row && col1>=0 && col1<max_col)
        {
            Cell other=null;
            if(dir==Direction.NORTH && row1>0 )
                other=cells[row1-1][col1];
            if(dir==Direction.SOUTH && row1<max_row-1)
                other=cells[row1+1][col1];
            if(dir==Direction.WEST && col1>0)
                other=cells[row1][col1-1];
            if(dir==Direction.EAST && col1<max_col-1)
                other=cells[row1][col1+1];
            //if(dir==Direction.NORTHWEST &&  row1>0 && col1>0)
                //other=cells[row1-1][col1-1];
            //if(dir==Direction.NORTHEAST && col1<max_col-1 && row1>0)
                //other=cells[row1-1][col1+1];
            //if(dir==Direction.SOUTHWEST &&  row1<max_row-1 && col1>0)
                //other=cells[row1+1][col1-1];
            //if(dir==Direction.SOUTHEAST && col1<max_col+1 && row1<max_row+1)
                //other=cells[row1+1][col1+1];

            if(other!=null)
                cells[row1][col1].linkneighbour(other, dir);
        }
    }

    void unlink(int row1, int col1, Direction dir)
    {
        if(row1>=0 && row1<=max_row && col1>=0 && col1<max_col)
        {
            Cell other=null;
            if(dir==Direction.NORTH && row1>0)
                other=cells[row1-1][col1];
            if(dir==Direction.SOUTH && row1<max_row-1)
                other=cells[row1+1][col1];
            if(dir==Direction.WEST && col1>0)
                other=cells[row1][col1-1];
            if(dir==Direction.EAST && col1<max_col-1)
                other=cells[row1][col1+1];
            //if(dir==Direction.NORTHWEST &&  row1>0 && col1>0)
                //other=cells[row1-1][col1-1];
            //if(dir==Direction.NORTHEAST && col1<max_col-1 && row1<max_row-1)
                //other=cells[row1-1][col1+1];

            //if(dir==Direction.SOUTHWEST &&  row1<max_row-1 && col1>0)
                //other=cells[row1+1][col1-1];
            //if(dir==Direction.SOUTHEAST && col1<max_col+1 && row1<max_row+1)
                //other=cells[row1+1][col1+1];
            if(other!=null)
                cells[row1][col1].unlinkneighbour(other, dir);
        }
    }

    @Override
    public String toString()
    {
        String b=new String();
        for(int i=0; i<max_row; i++)
        {
            b+="\n";
            for(int j=0; j<max_col; j++)
            {
                if(j==0)    b+="|";
                b+=" * ";
                if(j==max_col-1) b+="|";
                else
                {
                    if(!cells[i][j].getNeighbour(Direction.EAST).hasPath()) b+="|";
else b+=" ";
                }

            }
            if(i<max_row-1)
{
            b+="\n|";
            for(int j=0; j<max_col; j++)
            {
                if(!cells[i][j].getNeighbour(Direction.SOUTH).hasPath()) b+="----|";
                else if(cells[i][j].getNeighbour(Direction.SOUTH).hasPath()) b+="   |";

            }
}
        }
        return b;
    }
    int getRow() { return max_row; }
    int getCol() { return max_col; }
    public Cell.Link getNeighbourTo(int row, int col, Direction dir)
    {
        if(row<0 || col<0 || row>max_row || col>max_col) return null;
        return cells[row][col].getNeighbour(dir);
    }
    public Cell.Link getNeighbourTo(DungeonPoint p, Direction dir)
    {
        if(!DungeonPoint.inRangePoint(p, BOTTOM, TOP)) {
            return null;
        }
        return cells[p.getX()][p.getY()].getNeighbour(dir);
    }

    public Cell cellAt(DungeonPoint p) {
        return DungeonPoint.inRangePoint(p, new DungeonPoint(0, 0), new DungeonPoint(getRow(), getCol())) ? cells[p.getX()][p.getY()] : null;
    }
    public Cell cellAt(int row, int col)
    {
        if(row<0 || row>this.max_row || col<0 || col>this.max_col) return null; //out of bounds
        return cells[row][col];
    }
}
