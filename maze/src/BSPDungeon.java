/**
 * https://www.roguebasin.com/index.php/Basic_BSP_Dungeon_generation
 * https://eskerda.com/bsp-dungeon-generation/
 * https://antoniosliapis.com/articles/pcgbook_dungeons.php
    * https://medium.com/@guribemontero/dungeon-generation-using-binary-space-trees-47d4a668e2d0
    * So the idea is simple.  Devide the grid into rectangular chuncs and place rooms and change terrains.
 **/
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

enum Partition {
    HORIZONTAL,
    VERTICAL
}

class Quadtree {
    Quadtree left, right, up, down;
    private Partition partition;
    private DungeonPoint top, bottom;
    private static final int MIN_HEIGHT = 3, MIN_WIDTH=3, MAX_HEIGHT = 5, MAX_WIDTH= 5, MAX_DIVISION = 10000;
    public Quadtree(int bottomX, int bottomY, int height, int width) {
        partition = null;
        this.top = new DungeonPoint(height, width);
        this.bottom = new DungeonPoint(bottomX, bottomY);

        left = right = up = down = null;
    }
    public DungeonPoint getTop() {
        return top;
    }
    public DungeonPoint getBottom() {
        return bottom;
    }
    public DungeonPoint getCenter() {
        DungeonPoint cent = new DungeonPoint((bottom.getX()+top.getX())/2, (bottom.getY()+top.getY())/2);
        return cent;
    }
    public void getLeaves(ArrayList<Quadtree> leaves) {
        if(partition == null) {
            leaves.add(this);
            return;
        }
        switch(partition) {
            case HORIZONTAL:
                left.getLeaves(leaves);
                right.getLeaves(leaves);
                break;
            case VERTICAL:
                up.getLeaves(leaves);
                down.getLeaves(leaves);
                break;
        }
    }
    private void splitHorizontal() {
        int width = top.getY(), height = top.getX();
        if(width% 2 ==0) {
            left = new Quadtree(bottom.getX(), bottom.getY(), height, (width/2-1));
            right  = new Quadtree(bottom.getX(), bottom.getY() +(width/2), height, width/2);
        }
        else {
            left = new Quadtree(bottom.getX(), bottom.getY(), height, (width-1)/2);            
            right  = new Quadtree(bottom.getX(), bottom.getY() +(width-1)/2+1, height, (width-1)/2);
        }
    }
    private void splitVertical() {
        int width = top.getY(), height = top.getX();
        if(height % 2 ==0) {
            up = new Quadtree(bottom.getX(), bottom.getY(), (height/2)-1, width);            
            down = new Quadtree(bottom.getX()+(height/2), bottom.getY(), height/2, width);
        }
        else {
            up = new Quadtree(bottom.getX(), bottom.getY(), (height-1)/2, width);            
            down = new Quadtree(bottom.getX()+(height-1)/2+1, bottom.getY(), (height-1)/2, width);
        }
    }
    /* Splits and builds the tree. The recursive version is broken - not suitable for large-sized grids! */
    public Stack<Quadtree> split() // Iterative Version 
    {
        Quadtree temp =null;
        Stack<Quadtree> stack = new Stack<>();
        stack.push(this);
        while(!stack.isEmpty()) 
        {
            temp = stack.pop();
            if(stack.size()>=MAX_DIVISION) {
                return stack;
            }
            temp.partition = Partition.values()[DungeonUtil.random.nextInt(0, 2)];
            if(!DungeonUtil.lowerBound( temp.top.getX()/2, MIN_HEIGHT) && !DungeonUtil.lowerBound(top.getY()/2, MIN_WIDTH)) {
                temp.partition = null ; 
                continue;
            }
            if(temp.partition == Partition.HORIZONTAL && (!DungeonUtil.lowerBound(temp.top.getY()/2, MAX_WIDTH) )) {
                temp.partition = Partition.VERTICAL;
            }
            if(temp.partition == Partition.VERTICAL && !DungeonUtil.lowerBound(temp.top.getX()/2, MAX_HEIGHT)) {
                temp.partition = Partition.HORIZONTAL;
            }    
            switch(temp.partition) {
                case HORIZONTAL:        
                    temp.splitHorizontal();
                    break;
                case VERTICAL:
                    temp.splitVertical();
                    break;
            }
            if(temp.left!=null &&  ( DungeonUtil.lowerBound(temp.left.top.getX()/2,  MAX_HEIGHT) || DungeonUtil.lowerBound(temp.left.top.getY()/2, MAX_WIDTH)) )  {        
                stack.push(temp.left);
            }
            if(temp.right!=null &&  (DungeonUtil.lowerBound(temp.right.top.getX()/2,  MAX_HEIGHT) || DungeonUtil.lowerBound(temp.right.top.getY()/2,  MAX_WIDTH)) )  {
                stack.push(temp.right);
            }
            if(temp.down!=null &&  ( DungeonUtil.lowerBound(temp.down.top.getX()/2,  MAX_HEIGHT) || DungeonUtil.lowerBound(temp.down.top.getY()/2,  MAX_WIDTH)) )  {
                stack.push(temp.down);
            }
            if(temp.up!=null &&  ( DungeonUtil.lowerBound(temp.up.top.getX()/2, MAX_HEIGHT) || DungeonUtil.lowerBound(temp.up.top.getY()/2,  MAX_WIDTH)) )  {
                stack.push(temp.up);
            }
        }
        return null;
    }
    
    /* public void split() {
        if( (height >= MIN_HEIGHT && height <= MAX_HEIGHT) ||  (width >= MIN_WIDTH && width <= MAX_WIDTH) || partition !=null ) return;

        partition = Partition.values()[DungeonUtil.DungeonUtil.random.nextInt(0, 2)];
        if(partition == Partition.HORIZONTAL && (width >= MIN_WIDTH && width <= MAX_WIDTH)) {
            partition = Partition.VERTICAL;
        }
        else if(partition == Partition.VERTICAL && (height >= MIN_HEIGHT && height <= MAX_HEIGHT)) {
            partition = Partition.HORIZONTAL;
        }

        int split = 0;
        switch(partition) {
            case HORIZONTAL:            
            if(width% 2 ==0) {
                left = new Quadtree(topX, topY, height, (width/2)-1);            
                right  = new Quadtree(topX, topY +width/2, height, width/2);
            }
            else {
                left = new Quadtree(topX, topY, height, (width-1)/2);            
                right  = new Quadtree(topX, topY +(width-1)/2+1, height, (width-1)/2);
            }

            break;
        case VERTICAL:
            if(height % 2 ==0) {
                up = new Quadtree(topX, topY, (height/2)-1, width);            
                down = new Quadtree(topX+(height/2), topY, height/2, width);
            }
            else {
                up = new Quadtree(topX, topY, (height-1)/2, width);            
                down = new Quadtree(topX+(height-1)/2+1, topY, (height-1)/2, width);
            }
            break;
        }
        if(left!=null) {
            left.split();
        }
        if(right!=null) {
            right.split();
        }
        if(up!=null) {
            up.split();
        }
        if(down!=null) {
            down.split();
        }
    }
*/

    @Override
    public String toString() {
        String result="";
        result+=" Node at ()"+bottom.toString()+","+top.toString()+"), ";
        result+=( partition !=null ? ( partition == Partition.HORIZONTAL ? " is horizontal" : " is vertical " ) : " no partition ");
        result+="\n";

        if(left!=null) {
            result+= "\nLeft child: "+ left.toString();
        }
        if(right!=null) {
            result+= "Right child: \n"+right.toString();
        }
        if(up!=null) {
            result+= "Up child: \n"+ up.toString();
        }
        if(down!=null)  {
            result+= "Down child: \n"+ down.toString();
        }
        return result;
    }
}

class BSPDungeon implements Serializable
{
    private final ArrayList<Room> rooms = new ArrayList<>();
    private Grid map;
    private final DungeonPoint BOTTOM_GRID, TOP_GRID;
    public BSPDungeon(int width, int height, MazeGenerator mg) {
        map = new Grid( height, width);
            
            generateAndFillWithRooms();
            mg.generate(map, new DungeonPoint(0, 0), new DungeonPoint(map.getRow(), map.getCol()));
         mg.makeLoops(map);
        makeExitsOnRooms();
        BOTTOM_GRID = new DungeonPoint(0, 0);
        TOP_GRID = new DungeonPoint(height-1, width-1);
}
    private void makeExitsOnRooms() {
        for(Room room: rooms) {
            placeExits(room, Direction.NORTH);
            placeExits(room, Direction.WEST);
            placeExits(room, Direction.SOUTH);
            placeExits(room, Direction.EAST);
        }
    }
    private void placeExits(Room room, Direction dir) {
        ArrayList<DungeonPoint> points=null;
        switch(dir) {
            case Direction.SOUTH:
                points = room.getPointsSouthernEdge();
                break;
            case EAST:
                points = room.getPointsEasternEdge();
                break;
            case NORTH:
                points = room.getPointsNorthernEdge();
                break;
            case WEST:
                points = room.getPointsWesternEdge();
                break;
        }
        if(points == null) {
            return;
        }
        processExits(points, room);
    }
    private void processExits(ArrayList<DungeonPoint> points, Room room){
        int probs = 0;
        Cell cell =  null, neighbourCell = null;        
        for(DungeonPoint p: points) {
            cell = map.cellAt(p);
            var neighbours = cell.getLinkedneighbours();            
            if(neighbours == null) {
                continue;
            }
            for(Direction n: neighbours) {
                neighbourCell = map.getNeighbourTo(p, n).neighbour();
                if(Terrain.isPassable(neighbourCell.getTerrain())) {
                    probs++;
                }
            }
            if(probs>=2) {
                room.addExit(p);
                break; // no more exits at this side!
            }
            probs = 0;
            neighbourCell = cell = null;
        }
    }

    private void generateAndFillWithRooms() {
        Quadtree qt = new Quadtree(0, 0, map.getRow()-1, map.getCol()-1);
        qt.split();
        
        var leaves=new ArrayList<Quadtree>();
        qt.getLeaves(leaves);
        for(var l: leaves) {
            DungeonPoint top = l.getTop(), bottom = l.getBottom();
            // int width=(top.getY()-bottom.getY() <=3 ? (top.getY())  : DungeonUtil.random.nextInt(3, top.getY() - bottom.getY()));
            // int height=(top.getX()-bottom.getX() <=3 ? ( top.getX())  : DungeonUtil.random.nextInt(3, top.getX() - bottom.getX()));

            rooms.add(new Room(bottom.getX(), bottom.getY(), DungeonUtil.random.nextInt(3, top.getX()), DungeonUtil.random.nextInt(3, top.getY()), rooms.size()+1 +""));
            Room r=rooms.get(rooms.size()-1);
            
            r.getPoints().forEach(p -> {                    
                if(!r.pointOnEdge(p))
                {
                    map.link(p, Direction.NORTH);
                    map.link(p, Direction.SOUTH);
                    map.link(p, Direction.WEST);
                    map.link(p, Direction.EAST);
                    map.cellAt(p).setTerrain(Terrain.CONCRETE);
                }
                else {
                    
                    map.cellAt(p).setTerrain(Terrain.STONE_WALL);
                    
                }
            }); 
        }
    }

    public Terrain getTerrainAt(DungeonPoint p) {        
        return DungeonPoint.inRangePoint(p, BOTTOM_GRID, TOP_GRID) ? map.cellAt(p).getTerrain() : null;         
    }
    public boolean setTerrainAt(DungeonPoint p, Terrain ter) {
        if(!DungeonPoint.inRangePoint(p, BOTTOM_GRID, TOP_GRID)) {
            return false;
        }
        map.cellAt(p).setTerrain(ter);
        return true;
    }
    public ArrayList<Room> getRooms() {
        return rooms;
    }
    public Room getRoomAt(DungeonPoint p) {
        if(rooms.isEmpty()) {
            return null;
        }
        if(!DungeonPoint.inRangePoint(p, BOTTOM_GRID,TOP_GRID)) {
            return null;
        }
        for(Room r: rooms) {
            if(r.contains(p)) {
                return r;
            }
        }
        return null;
    }
    public int getRow() {
        return map.getRow();
    }
    public int getCol() {
        return map.getCol();
    }
    public boolean hasPathTo(DungeonPoint p, Direction dir) {
        return DungeonPoint.inRangePoint(p, BOTTOM_GRID, TOP_GRID) && map.getNeighbourTo(p, dir)!=null && map.getNeighbourTo(p, dir).hasPath();
    }
    @Override
public String toString() {
    Room temproom= null;
    String res="";
    for(int i=0; i<map.getRow(); i++) {
        res+="\n|";
        for(int j =0; j< map.getCol(); j++) {
            temproom = getRoomAt(new DungeonPoint(i, j));
            res+=map.cellAt(new DungeonPoint(i, j)).getTerrain().getSymbol();
            if(temproom != null) res+=temproom.getName();
            else res+="#";
            var east_n=map.getNeighbourTo(new DungeonPoint(i, j), Direction.EAST);
            if(east_n == null) { 
                res+="|";
                continue;
            }
    if(east_n.hasPath())  res+=".";
            if(!east_n.hasPath())  res+="|";
        }
        res+="\n|";
        for(int j =0; j< map.getCol(); j++) {
            var east_n=map.getNeighbourTo(new DungeonPoint(i, j), Direction.SOUTH);
            if(east_n == null) { 
                res+="--|";
                continue;
            }
    if(east_n.hasPath())  res+="..|";
            if(!east_n.hasPath())  res+="----|";
        }
        }
    
    return res;
}
}