import java.util.ArrayList;
import java.util.Random;

public class Dungeon {
    static Random random = new Random();
    ArrayList<Room> rooms=new ArrayList<>();
    Grid map = null;
    static int MAX_R_WIDTH=6, MAX_R_HEIGHT=6, MIN_R_WIDTH=3, MIN_R_HEIGHT=3;
    
public Dungeon(int width,int height, MazeGenerator which, int room_count) {
    map = new Grid(height, width);
     //mg= which;
    which.generate(map,new DungeonPoint(0, 0), new DungeonPoint(width, height));
    
        which.fillDeadends(map,5);        
        which.makeLoops(map);
fillRooms(room_count);
}

private void fillRooms(int room_count)
{
    int index=0, retries=0;
    //while(visited.stream().flatMap(e -> e.stream()).anyMatch(c -> { return c==false; })) {
    while(index < room_count || retries <=3)
        {
         Room best_room= calculateRoomPosition();
         
                if(best_room == null) {
//                    continue; 
//System.out.println("huh?");
++retries;
break;
                
                }
        index++;        
                best_room.getPoints().stream().forEach(p -> {
map.link(p.getX(), p.getY(), Direction.NORTH);
map.link(p.getX(), p.getY(), Direction.SOUTH);
map.link(p.getX(), p.getY(), Direction.WEST);
map.link(p.getX(), p.getY(), Direction.EAST);
                });
                rooms.add(best_room);
    }
}
    private Room calculateRoomPosition() {
        int best_score = Integer.MAX_VALUE;    
    Room best_room = null;
    for( int i =0; i < map.getRow(); i++) {
        label1:
        for(int j =0; j< map.getCol(); j++) {
            Room current_room = new Room(i, j, random.nextInt(MIN_R_WIDTH, MAX_R_WIDTH), random.nextInt(MIN_R_HEIGHT, MAX_R_HEIGHT),rooms.size()+1+"");         
            int current_score = 0;
            DungeonPoint bottom = current_room.getBottom();
    
            if(  bottom.getX()>= map.getRow() || bottom.getY() >= map.getCol() || current_room.getPoints().stream().anyMatch(p -> { return getRoomAt(p.getX(), p.getY()) != null; })) {                
                //outOfBounds = true;
                continue;
            }        
            if(roomCollides(current_room)) {
                continue;
            }
            for(var p: current_room.getPoints()) {            
                if(map.cellAt(p.getX(), p.getY()).getLinkedNeighbors()==null ) continue;
                if( map.cellAt(p.getX(), p.getY()).getLinkedNeighbors().size()==1) {
                    current_score+=1;
                }
                if( map.cellAt(p.getX(), p.getY()).getLinkedNeighbors().size()>=2) {
                    current_score+=3;
                }
            }
if(best_score > current_score) {    
    best_score = current_score;
    best_room = current_room;
}            
        }
    }

    return best_room;
    }

    public boolean roomCollides(Room current_room) {
            return rooms.stream().anyMatch(r -> {
                return r.equal(current_room);
            });

    }
@Override
public String toString() {
    Room temproom= null;
    String res="";
    for(int i=0; i<map.getRow(); i++) {
        res+="\n|";
        for(int j =0; j< map.getCol(); j++) {
            temproom = getRoomAt(i, j);
            res+="*";
            if(temproom != null) res+=temproom.getName();
            else res+="#";
            var east_n=map.getNeighborTo(i, j, Direction.EAST);
            if(east_n == null) { 
                res+="|";
                continue;
            }
    if(east_n.hasPath())  res+=".";
            if(!east_n.hasPath())  res+="|";
        }
        res+="\n|";
        for(int j =0; j< map.getCol(); j++) {
            var east_n=map.getNeighborTo(i, j, Direction.SOUTH);
            if(east_n == null) { 
                res+="--|";
                continue;
            }
    if(east_n.hasPath())  res+="..|";
            if(!east_n.hasPath())  res+="----|";
        }
        }
    
    // return rooms.stream().q;
    return res;
}
    public Room getRoomAt(int x, int y) {
        if(rooms.isEmpty()) return null;
        if(x<0 || x>=map.getRow() || y<0 || y>=map.getCol()) return null;
        for(Room r: rooms) {
            if(r.contains(new DungeonPoint(x, y))) return r;
        }
        return null;
    }
}
