/** Implementations of Ray-Casting * - Bresenham's Line-Drawing Algorithm - and shadow-casting algorithm
    * http://www.adammil.net/blog/v125_Roguelike_Vision_Algorithms.html#shadowcode
    * Eric's Article-Series (All 6 parts follow):
        * https://learn.microsoft.com/en-us/archive/blogs/ericlippert/shadowcasting-in-c-part-one
        * https://learn.microsoft.com/en-us/archive/blogs/ericlippert/shadowcasting-in-c-part-two
        * https://learn.microsoft.com/en-us/archive/blogs/ericlippert/shadowcasting-in-c-part-three
        * https://learn.microsoft.com/en-us/archive/blogs/ericlippert/shadowcasting-in-c-part-four
        * https://learn.microsoft.com/en-us/archive/blogs/ericlippert/shadowcasting-in-c-part-five
        * https://learn.microsoft.com/en-us/archive/blogs/ericlippert/shadowcasting-in-c-part-six
**/

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.IntStream;


public class DungeonFOV {
    record ShadowColumnPortion(int step, DungeonPoint top, DungeonPoint bottom) {
    }
    private static  void computeShadowFirstOctant(DungeonPoint p, int octtant,  Predicate<DungeonPoint> isBlocked, Predicate<DungeonPoint> isVisible, int radius, ArrayList<DungeonPoint> points) {
        LinkedList<ShadowColumnPortion> worker = new LinkedList<>();
            
        ShadowColumnPortion elem = null;
        worker.offer(new ShadowColumnPortion(1, new DungeonPoint(1,1),  new DungeonPoint(1, 0)));
        while(!worker.isEmpty()) {
            elem = worker.poll();
            System.out.println(elem.toString());

            if(elem.step() >= radius) {
                continue;
            }
            computeShadowForColumn(elem,p,octtant, isBlocked, isVisible, radius, worker, points);

        }        
    }
   
    private static void computeShadowForColumn(ShadowColumnPortion elem,  DungeonPoint orig, int octant,  Predicate<DungeonPoint> isBlocked, Predicate<DungeonPoint> isVisible, int radius, LinkedList worker, ArrayList<DungeonPoint> points) {
        System.out.println("radius is "+radius);
        Boolean lastWasNotVisible = null;
        DungeonPoint top = elem.top();
    
        for(int i=top.getY(); i>=elem.bottom().getY(); --i) 
        {
            
            DungeonPoint c = DungeonPoint.addPoints(orig, DungeonPoint.transformPoint(new DungeonPoint(elem.step(), i), octant));
            boolean inRadius = DungeonPoint.pointInCircle(c, orig, radius);

            System.out.println("c "+c.toString()+" in radius? "+inRadius+" length "+DungeonPoint.getLength(c)+" radius: "+radius);
            if(inRadius && points.indexOf(c) <0 ) {
        
                points.add(c);
            }
            boolean notVisible = !inRadius || !isVisible.test(c);
            if ( lastWasNotVisible != null)
            {
                if (notVisible) {
                    if (!lastWasNotVisible)
                    {
                        worker.offer(new ShadowColumnPortion(elem.step() + 1, elem.top(), new DungeonPoint(elem.step() * 2 - 1, i * 2 + 1)));
                    }
                }
                else if (lastWasNotVisible)
                {
                    top = new DungeonPoint(elem.step() * 2 + 1, i * 2 + 1);
                }
            }        
            lastWasNotVisible = notVisible;
                
                
        }
        if( lastWasNotVisible != null && !lastWasNotVisible) 
        worker.offer(new ShadowColumnPortion(elem.step() + 1, top, elem.bottom()));

    }
    public static ArrayList<DungeonPoint> castShadow(DungeonPoint orig, int radius, Predicate<DungeonPoint> isBlocked, Predicate<DungeonPoint> isVisible) {        
        ArrayList<DungeonPoint> points = new ArrayList<>();
        IntStream.range(0, 8).forEach(o -> {            
            System.out.println("Compute at octant "+o+" "+ DungeonPoint.transformPoint(orig, o).toString()+" origin at "+orig.toString());
            computeShadowFirstOctant(orig, o, isBlocked, isVisible, radius , points);
        });
        return points;
    }
    }