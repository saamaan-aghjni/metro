class Grid
{
    private Cell[][] cells;
	int max_row, max_col;
	Grid(int row,int col)
	{
        max_row = row;
        max_col = col;
        cells=new Cell[max_row][max_col];
        for(int i=0; i<max_row; i++)
        for(int j=0; j<max_col; j++)
            cells[i][j]=new Cell(i+1, j+1);
        initCells();
    }
    void initCells()
    {
        for(int i=0; i<max_row; i++)
            for(int j=0; j<max_col; j++)
            {
                if(i-1>=0) 
                    cells[i][j].addNeighbor(cells[i-1][j], Direction.NORTH, false);
                if(i+1<max_row) 
                    cells[i][j].addNeighbor(cells[i+1][j], Direction.SOUTH, false);
                if(j-1>=0) 
                    cells[i][j].addNeighbor(cells[i][j-1], Direction.WEST, false);
                if(j+1<max_col) 
                    cells[i][j].addNeighbor(cells[i][j+1], Direction.EAST, false);
                if(i-1>=0 && j-1>=0) 
                    cells[i][j].addNeighbor(cells[i-1][j-1], Direction.NORTHWEST, false);
                if(i-1>=0 && j+1<max_col)
                    cells[i][j].addNeighbor(cells[i-1][j+1], Direction.NORTHEAST, false);
                if(i+1<max_row && j+1<max_col)
                    cells[i][j].addNeighbor(cells[i+1][j+1], Direction.SOUTHEAST, false);
                if(i+1<max_row && j-1>=0)
                    cells[i][j].addNeighbor(cells[i+1][j-1], Direction.SOUTHWEST, false);
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
            if(dir==Direction.NORTHWEST &&  row1>0 && col1>0)
                other=cells[row1-1][col1-1];
            if(dir==Direction.NORTHEAST && col1<max_col-1 && row1>0)
                other=cells[row1-1][col1+1];
            if(dir==Direction.SOUTHWEST &&  row1<max_row-1 && col1>0)
                other=cells[row1+1][col1-1];
            if(dir==Direction.SOUTHEAST && col1<max_col+1 && row1<max_row+1)
                other=cells[row1+1][col1+1];

            if(other!=null)
                cells[row1][col1].linkNeighbor(other, dir);
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
            if(dir==Direction.NORTHWEST &&  row1>0 && col1>0)
                other=cells[row1-1][col1-1];
            if(dir==Direction.NORTHEAST && col1<max_col-1 && row1<max_row-1)
                other=cells[row1-1][col1+1];

            if(dir==Direction.SOUTHWEST &&  row1<max_row-1 && col1>0)
                other=cells[row1+1][col1-1];
            if(dir==Direction.SOUTHEAST && col1<max_col+1 && row1<max_row+1)
                other=cells[row1+1][col1+1];
            if(other!=null)
                cells[row1][col1].unlinkNeighbor(other, dir);
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
                    if(!cells[i][j].getNeighbor(Direction.EAST).hasPath()) b+="|";
else b+=" ";
                }

            }
            if(i<max_row-1)
{
            b+="\n|";
            for(int j=0; j<max_col; j++)
            {
                if(!cells[i][j].getNeighbor(Direction.SOUTH).hasPath()) b+="---|";
                else if(cells[i][j].getNeighbor(Direction.SOUTH).hasPath()) b+="   |";

            }
}
        }
        return b;
    }
    int getRow() { return max_row; }
    int getCol() { return max_col; }
    public Cell.Link getNeighborTo(int row, int col, Direction dir)
    {
        if(row<0 || col<0 || row>max_row || col>max_col) return null;
        return cells[row-1][col-1].getNeighbor(dir);
    }
    public Cell cellAt(int row, int col)
    {
        if(row<0 || row>this.max_row || col<0 || col>this.max_col) return null; //out of bounds
        return cells[row][col];
    }
}
