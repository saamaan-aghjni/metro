/**
 * https://www.roguebasin.com/index.php/Basic_BSP_Dungeon_generation
 * https://eskerda.com/bsp-dungeon-generation/
 * https://antoniosliapis.com/articles/pcgbook_dungeons.php
 **/
import java.util.ArrayList;
import java.util.Stack;

enum Partition {
    HORIZONTAL,
    VERTICAL
}

class Quadtree {
    Quadtree left, right, up, down;
    private Partition partition;
    private int topX, topY, width, height;    
    private static final int MIN_HEIGHT = 3, MIN_WIDTH=3, MAX_HEIGHT = 5, MAX_WIDTH= 5, MAX_DIVISION = 10000;
    public Quadtree(int tx, int ty, int th, int tw) {
        topX = tx;
        topY = ty;
        height = th;
        width = tw;
        partition = null;
        left = right = up = down = null;
    }
    public DungeonPoint getTop() {
        return new DungeonPoint(topX, topY);
    }
    public DungeonPoint getBottom() {
        return new DungeonPoint(topX+height, topY+width);
    }
    public DungeonPoint getCenter() {
        int cheight = height%2 ==0 ? height/2 : (height-1)/2;
        int cwidth = width%2 ==0 ? width/2 : (width-1)/2;
        return new DungeonPoint(topX+cheight, topY+cwidth); 
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
            if(!DungeonUtil.lowerBound( temp.height/2, MIN_HEIGHT) && !DungeonUtil.lowerBound(temp.width/2, MIN_WIDTH)) {
                temp.partition = null ; 
                continue;
            }
            if(temp.partition == Partition.HORIZONTAL && (!DungeonUtil.lowerBound(temp.width/2, MAX_WIDTH) )) {
                temp.partition = Partition.VERTICAL;
            }
            if(temp.partition == Partition.VERTICAL && !DungeonUtil.lowerBound(temp.height/2, MAX_HEIGHT)) {
                temp.partition = Partition.HORIZONTAL;
            }    
            switch(temp.partition) {
                case HORIZONTAL:        
                    int split=(temp.width ==0? temp.width+1 : temp.width);
                    if(temp.width% 2 ==0) {
                        temp.left = new Quadtree(temp.topX, temp.topY, temp.height, (temp.width/2-1));            
                        temp.right  = new Quadtree(temp.topX, temp.topY +temp.width/2, temp.height, temp.width/2);
                    }
                    else {
                        temp.left = new Quadtree(temp.topX, temp.topY, temp.height, (temp.width-1)/2);            
                        temp.right  = new Quadtree(temp.topX, temp.topY +(temp.width-1)/2+1, temp.height, (temp.width-1)/2);
                    }
                    break;
                case VERTICAL:
                    if(temp.height % 2 ==0) {
                        temp.up = new Quadtree(temp.topX, temp.topY, (temp.height/2)-1, temp.width);            
                        temp.down = new Quadtree(temp.topX+(temp.height/2), temp.topY, temp.height/2, temp.width);
                    }
                    else {
                        temp.up = new Quadtree(temp.topX, temp.topY, (temp.height-1)/2, temp.width);            
                        temp.down = new Quadtree(temp.topX+(temp.height-1)/2+1, temp.topY, (temp.height-1)/2, temp.width);
                    }
                    break;
            }

            if(temp.left!=null &&  ( DungeonUtil.lowerBound(temp.left.height/2,  MAX_HEIGHT) || DungeonUtil.lowerBound(temp.left.width/2, MAX_WIDTH)) )  {        
                stack.push(temp.left);
            }
            if(temp.right!=null &&  (DungeonUtil.lowerBound(temp.right.height/2,  MAX_HEIGHT) || DungeonUtil.lowerBound(temp.right.width/2,  MAX_WIDTH)) )  {
                stack.push(temp.right);
            }
            if(temp.down!=null &&  ( DungeonUtil.lowerBound(temp.down.height/2,  MAX_HEIGHT) || DungeonUtil.lowerBound(temp.down.width/2,  MAX_WIDTH)) )  {
                stack.push(temp.down);
            }
            if(temp.up!=null &&  ( DungeonUtil.lowerBound(temp.up.height/2, MAX_HEIGHT) || DungeonUtil.lowerBound(temp.up.width/2,  MAX_WIDTH)) )  {
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
        result+=" Node at ()"+topX+", "+ topY +"), ("+(topX+height)+", "+(topY+width)+"), ";
        result+=( partition !=null ? ( partition == Partition.HORIZONTAL ? " is horizontal" : " is vertical " ) : " no partition ");
        result+="\n";

        if(left!=null) 
    
        result+= "\nLeft child: "+ left.toString();
        if(right!=null) 
        result+= "Right child: \n"+right.toString();
        if(up!=null) 
        result+= "Up child: \n"+ up.toString();
        if(down!=null) 
        result+= "Down child: \n"+ down.toString();
return result;
    }
}

class BSPDungeon
{
    private final ArrayList<Room> rooms = new ArrayList<>();
    private final Grid map;
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
        int probs = 0;
        Cell cell =  null, neighbourCell = null;        
        for(DungeonPoint p: points) {
            cell = map.cellAt(p);
            var neighbours = cell.getLinkedneighbours();            
            if(neighbours == null) {
                continue;
            }
            for(Direction n: neighbours) {
                neighbourCell = map.getNeighbourTo(cell.getRow(), cell.getCol(), n).neighbour();
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
        Quadtree qt = new Quadtree(0, 0, map.getRow(), map.getCol());
        qt.split();
        var leaves=new ArrayList<Quadtree>();
        qt.getLeaves(leaves);
        for(var l: leaves) {
            DungeonPoint tp = l.getTop(), bottom = l.getBottom();
            int width=(bottom.getY()-tp.getY() <=3 ? bottom.getY() - tp.getY() : DungeonUtil.random.nextInt(3, bottom.getY() - tp.getY()));
            int height=(bottom.getX()-tp.getX() <=3 ? bottom.getX() - tp.getX() : DungeonUtil.random.nextInt(3, bottom.getX() - tp.getX()));
            rooms.add(new Room(tp.getX(), tp.getY(), height, width, rooms.size()+1 +""));
            Room r=rooms.get(rooms.size()-1);
            r.getPoints().forEach(p -> {                    
                if(!r.pointOnEdge(p))
                {
                    map.link(p.getX(), p.getY(), Direction.NORTH);
                    map.link(p.getX(), p.getY(), Direction.SOUTH);
                    map.link(p.getX(), p.getY(), Direction.WEST);
                    map.link(p.getX(), p.getY(), Direction.EAST);
                    map.cellAt(p.getX(), p.getY()).setTerrain(Terrain.CONCRETE);
                }
                else {
                    map.cellAt(p.getX(), p.getY()).setTerrain(Terrain.STONE_WALL);
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
            res+=map.cellAt(i, j).getTerrain().getSymbol();
            if(temproom != null) res+=temproom.getName();
            else res+="#";
            var east_n=map.getNeighbourTo(i, j, Direction.EAST);
            if(east_n == null) { 
                res+="|";
                continue;
            }
    if(east_n.hasPath())  res+=".";
            if(!east_n.hasPath())  res+="|";
        }
        res+="\n|";
        for(int j =0; j< map.getCol(); j++) {
            var east_n=map.getNeighbourTo(i, j, Direction.SOUTH);
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