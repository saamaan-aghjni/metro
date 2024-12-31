import java.util.Scanner;

import java.util.Random;

public class App {
    private static Random random = new Random();
    public static boolean test(DungeonPoint p) {
        return true;
    }
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        World w=new World(900, 900, new Sidewinder());
System.out.println("Size of rooms: "+w.getRooms().size());
        Room r=w.getRooms().get(random.nextInt(0, w.getRooms().size()));
        DungeonEntityHero hero=new DungeonEntityHero(r.getCenter());
        Scanner reader = new Scanner(System.in); // Reading from System.in
        DungeonEntityHeroInputHandler.world = w;
        DungeonEntityHeroInputHandler.ent = hero;
        w.registerEntity(hero);
        while (true) { 
            DungeonEntityHeroInputHandler.process(reader.nextLine());
            w.update();
}        
    }
}
