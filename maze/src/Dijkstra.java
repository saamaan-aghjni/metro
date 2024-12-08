class  DijkstraNode
{
    private Cell cell, parent;
    private int dist;
    DijkstraNode(Cell cell, int dist)
    {
        this.cell = cell;
        this.parent = null;
        this.dist= dist;
    }
@Override
public boolean equals(Object other)
{
if(other==null) return false;
if(!(other instanceof DijkstraNode)) return false;
DijkstraNode n=(DijkstraNode)other;
return n.cell.equals( this.cell) && n.dist ==this.dist;
}
@Override
    public String toString()
    {
        String st="";
        st+="DijkstraNode with distance "+dist+": \n\tCell: "+cell.toString()+"\n\tParent: "+parent.toString()+"\n";
return st;
    }

    Cell getCell() { return cell; }
    int getDist() { return dist; }
    void setDist(int d) { dist = d; }
    void setParent(Cell p)
    {
        parent = p;
    }
    Cell getParent() { return parent; }
}

class Dijkstra extends MazeSolver
{
    protected Dijkstra() { }
    static public List<DijkstraNode> solve(int startRow, int startCol, Grid grid) //All return a set of cells from start to end?
    {
//        DijkstraNode[][] nodes=new DijkstraNode[grid.getRow()][grid.getCol()];
        LinkedList<DijkstraNode> nodes=new LinkedList<>();
        for(int i=0; i< grid.getRow(); i++)
        {
            for(int j=0; j< grid.getCol(); j++)
            {
//                nodes[i][j]=new DijkstraNode(grids.cellAt(i, j), Integer.MAX_VALUE));
                nodes.add(new DijkstraNode(grid.cellAt(i, j), Integer.MAX_VALUE));
            }
        }
//        nodes[0][0]=new DijkstraNode(grid.cellAt(0,0), 0));
        int current=0;
        nodes.get(current).setDist(0);
        while(current<nodes.size())
        {
            var neighbors=nodes.get(current).getCell().getNeighbors();
            for(var neighbor: neighbors)
            {
                int dist= nodes.get(current).getDist()+1;
int index=nodes.indexOf(new DijkstraNode(neighbor, Integer.MAX_VALUE));
                if(index>=0 && index<nodes.size() && nodes.get(index).getDist()>dist && nodes.get(current).getCell().getNeighbor(neighbor).hasPath())
                {
                    nodes.get(index).setDist(dist);
                    nodes.get(index).setParent(nodes.get(current).getCell());
//    System.out.println("Neighbor at "+neighbor.getRow()+" "+neighbor.getCol()+" with updated distance "+nodes.get(index).getDist());

                }
            }        
if(nodes.get(current).getDist()==Integer.MAX_VALUE || nodes.get(current).getDist() <0) nodes.remove(current);
else 
                current++;
        }
        return nodes;
    }
}
