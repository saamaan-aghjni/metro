/**
 * TODO:  Allow the creation of other non-square-shaped rooms (Circles/triangles for a start?)
 * Perhaps even more complex shapes
 * Currently *not* on the priority list.
 */
import java.util.ArrayList;
import java.util.stream.IntStream;

public class Room {    
    private int height, width, topX, topY;
    private String name;
    private ArrayList<DungeonPoint> exits=new ArrayList<>();

    public Room(int tx, int ty, int th, int tw, String name) {
        this.topX = tx;
        this.topY = ty;
        this.height = th;
        this.width = tw;
        this.name = name;
    }
    DungeonPoint getTop() {
        return new DungeonPoint(topX, topY);
    }
    public DungeonPoint getCenter() {
        return new DungeonPoint( (topX+topX+height)/2, (topY+topY+width)/2); 
    }
    public void setTop(DungeonPoint p) {
        topX = p.getX();
        topY = p.getY();
    }
    public String getName() { return name; }
    DungeonPoint getBottom() {
        return new DungeonPoint(topX+height, topY+width);
    }
    public boolean contains(DungeonPoint p) {
        DungeonPoint p1=getTop(), p2=getBottom();
        return p.getX() >= p1.getX() && p.getY() >= p1.getY() && p.getX() <= p2.getX() && p.getY() <= p2.getY();
    }
    public boolean pointOnEdge(DungeonPoint p) {
        boolean leftEdge = (p.getX() >= topX && p.getX() <= topX+height && p.getY() == topY);
        boolean rightEdge = (p.getX() >= topX && p.getX() <= topX + height && p.getY() == topY + width);
        boolean upperEdge = (p.getY() >= topY && p.getY() <= topY+width && p.getX() == topX);
        boolean lowerEdge = (DungeonUtil.inRange(p.getY(), topY, topY+width) && p.getX() == topX + height);
return leftEdge || rightEdge || upperEdge || lowerEdge;
    }
    public void addExit(DungeonPoint p) {
        if(!pointOnEdge(p)) return;
        exits.add(p);
    }
    public boolean removeExit(DungeonPoint p) {
        int i=exits.indexOf(p);
        if(i<0) return false;
        exits.remove(p);
return true;
    }
    public ArrayList<DungeonPoint> getExits() {
        return exits;
    }
public ArrayList<DungeonPoint> getPointsInside() {
        ArrayList<DungeonPoint> result = new ArrayList<>();
        IntStream.range(topX+1, topX+height-1).forEach(i -> { 
            IntStream.range(topY+1, topY+width-1).forEach(j -> {
                result.add(new DungeonPoint(i, j));
            });
        });
        return result;
    }
    
    public ArrayList<DungeonPoint> getPoints() {
        ArrayList<DungeonPoint> result = new ArrayList<>();
        IntStream.range(topX, topX+height+1).forEach(i -> { 
            IntStream.range(topY, topY+width+1).forEach(j -> {
                result.add(new DungeonPoint(i, j));
            });
        });
        return result;
    }
    public ArrayList<DungeonPoint> getPointEdges() {
        ArrayList<DungeonPoint> points=new ArrayList<>();
        IntStream.range(0, width).forEach(i -> {
            points.add(new DungeonPoint(topX, topY+i));
            points.add(new DungeonPoint(topX+height, topY+i));
        });
        IntStream.range(0, height).forEach(i -> {
            points.add(new DungeonPoint(topX+i, topY));
            points.add(new DungeonPoint(topX+i, topY+width));
        });
      
        return points;
    }
    public ArrayList<DungeonPoint> getPointsWesternEdge() {
        ArrayList<DungeonPoint> points=new ArrayList<>();
        IntStream.range(0, height).forEach(i -> {
            points.add(new DungeonPoint(topX+i, topY+width));
        });      
        return points;
    }
    public ArrayList<DungeonPoint> getPointsEasternEdge() {
        ArrayList<DungeonPoint> points=new ArrayList<>();
        IntStream.range(0, height).forEach(i -> {
            points.add(new DungeonPoint(topX+i, topY));
        });
        return points;
    }
    public ArrayList<DungeonPoint> getPointsNorthernEdge() {
        ArrayList<DungeonPoint> points=new ArrayList<>();
        IntStream.range(0, width).forEach(i -> {
            points.add(new DungeonPoint(topX+height, topY+i));
        });      
        return points;
    }
    public ArrayList<DungeonPoint> getPointsSouthernEdge() {
        ArrayList<DungeonPoint> points=new ArrayList<>();
        IntStream.range(0, width).forEach(i -> {
            points.add(new DungeonPoint(topX, topY+i));
        });
        return points;
    }

    @Override
    public String toString() {
        return "Room at "+topX+" "+topY+", width "+width+", height "+height;
    }
    boolean equal(Object other) {
        if(other == null) return false;
        if( !( other instanceof Room)) return false;
if(this == other) return true;
Room temp=(Room)other;
// return this.topX >= temp.topX && this.topY >= temp.topY && this.height == temp.height && this.width == temp.width
// return this.getPoints().stream().anyMatch(p -> temp.contains(p)) || temp.getPoints().stream().anyMatch(p -> this.contains(p));
    return this.topX <= temp.topX + temp.height && this.topY <= temp.topY + temp.width && temp.topX <= this.topX + this.height && temp.topY <= this.topY + this.width;
    }
}
