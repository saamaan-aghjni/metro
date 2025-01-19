
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class App {
    public static boolean test(DungeonPoint p) {
        return true;
    }
    public static void main(String[] args) throws Exception {
        
         Logger rootLogger = LogManager.getLogManager().getLogger("");
// rootLogger.setLevel(Level.ALL);
rootLogger.removeHandler(rootLogger.getHandlers()[0]);
        System.out.println("Hello, World!");
          Properties prop = System.getProperties();
  prop.setProperty(
     "handlers",
     "");
System.setProperties(prop);

World w=new World(10, 10, new Sidewinder());
System.out.println("Size of rooms: "+w.dungeon.getRooms().size());
        Room r=w.dungeon.getRooms().get(DungeonUtil.random.nextInt(0, w.dungeon.getRooms().size()));
        w.loadJSONEntities("creatures.json");
        w.spawnCreature(r.getCenter(), "Artjom", DungeonEntityRace.HUMAN, DungeonCreatureClass.WARRIOR, true);
        
        //  w.spawnCreature(r.getCenter(), null, DungeonEntityRace.HUMAN, DungeonCreatureClass.WARRIOR, true);
        w.spawnCreature(new DungeonPoint(r.getCenter().getX()+1, r.getCenter().getY()+1), null, DungeonEntityRace.HUMAN, DungeonCreatureClass.WARRIOR, false);
        w.spawnItem(r.getCenter(), "knife");
        Scanner reader = new Scanner(System.in); // Reading from System.in
        DungeonEntityHeroInputHandler.world = w;
        DungeonEntityHeroInputHandler.ent = w.getHero();
        var hero = DungeonEntityHeroInputHandler.ent;
        hero.setLoggerHandler(new DungeonConsoleOutputHandler());
        

        while (true) { 

            DungeonEntityHeroInputHandler.process(reader.nextLine());
            w.update();
}        
    }
}
