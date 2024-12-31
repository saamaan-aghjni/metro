
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Random;

class DungeonPoint {
    private int x, y;
    private static int xxcomp[] =new int[]{ 1, 0, 0, -1, -1, 0, 0, 1 };
    private static int xycomp[] =new int[] { 0, 1, -1, 0, 0, -1, 1, 0 };
    private static int yxcomp[] = new int[]{ 0, 1, 1, 0, 0, -1, -1, 0 };
    private static int yycomp[] =new int[]{ 1, 0, 0, 1, -1, 0, 0, -1 };
    public static boolean pointInCircle(DungeonPoint p, DungeonPoint center, int radius) {
        return p.distEucledian(center) <= radius;

    }
    public static DungeonPoint addPoints(DungeonPoint p1, DungeonPoint p2) {
        return new DungeonPoint(p1.getX() + p2.getX(), p1.getY() + p2.getY());
    }
    public static DungeonPoint normalize(DungeonPoint p) {
        double len = Math.sqrt(Math.pow(p.getX(),2) + Math.pow(p.getY(), 2));
        return new DungeonPoint((int)(p.getX()/len), (int)(p.getY()/len));
    }
    public static DungeonPoint transformPoint(DungeonPoint p, int o) {
        return new DungeonPoint(p.getX() *  xxcomp[o] + p.getY() * xycomp[o], p.getX() * yxcomp[o] + p.getY() * yycomp[o]);
        }

    public DungeonPoint(int px, int py) {
        x = px;
        y = py;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public double distManhaton(DungeonPoint other) {
        return Math.abs(other.x - this.x) + Math.abs(other.y - this.y);
    }
    public static double getLength(DungeonPoint p) {
        return Math.sqrt( Math.pow(p.getX(), 2)+Math.pow(p.getY(), 2));
    }
    public double distEucledian(DungeonPoint other) {
        return Math.sqrt(Math.pow(other.x - this.x, 2) +Math.pow(other.y - this.y, 2));
    }
    
    @Override
    public String toString() {
        return "Point at ()"+x+", "+y+")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof DungeonPoint)) return false;
        if(this == obj) return true;
        var p=(DungeonPoint)obj;
        return this.x == p.x && this.y == p.y;
    }
}


public class DungeonUtil {
    public static Random random = new Random();
    
public static boolean lowerBound(int number, int lower) {
    return number >= lower;

}
public static boolean lowerBound(double number, double lower) {
    return number >= lower;
}


    public static int positionToIndex(DungeonPoint p,   int width) {
        return (p.getX() ) + (width * p.getY());
    }
    public static DungeonPoint minimum(DungeonPoint p1, DungeonPoint p2) {
        return p1.getX() >= p2.getX() && p1.getY() >= p2.getY() ? p2 : p1;
    }
    public static DungeonPoint maximum(DungeonPoint p1, DungeonPoint p2) {
        return p1.getX() >= p2.getX() && p1.getY() >= p2.getY() ? p1 : p2;
    }

    public static DungeonPoint movePoint(DungeonPoint p, Direction dir, int steps) {
        if(steps<=0) return p;
        DungeonPoint p2=null;
        switch(dir) {
            case NORTH:
                p2 =new DungeonPoint(p.getX()-steps, p.getY());
                break;
            case SOUTH:
                p2 =new DungeonPoint(p.getX()+steps, p.getY());
                break;
            case EAST:
                p2 =new DungeonPoint(p.getX(), p.getY() +steps);
                break;
            case WEST:
                p2 =new DungeonPoint(p.getX(), p.getY()-steps);
                break;    
        }
        return p2;

    }
    public static <T> void fillArray(ArrayList<T> a, int length, T default2) 
    {
        
        for(int i=0; i<length; ++i) {
            a.add(default2);
        }            
        }
    public static boolean inRangePoint(DungeonPoint p, DungeonPoint start, DungeonPoint end) {
        return inRange(p.getX(), start.getX(), end.getX()) && inRange(p.getY(), start.getY(), end.getY());
    }
    public static boolean inRange(int number, int lower, int upper) {
        return number >= lower && number <= upper; 
    }
    private DungeonUtil() { }
}