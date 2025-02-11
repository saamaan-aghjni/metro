
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
public class App {
    public static void main(String[] args) throws Exception {
        
         Logger rootLogger = LogManager.getLogManager().getLogger("");
// rootLogger.setLevel(Level.ALL);
rootLogger.removeHandler(rootLogger.getHandlers()[0]);
        System.out.println("Hello, World!");
        Properties prop = System.getProperties();
//   prop.setProperty()"handlers",     "");
// System.setProperties(prop);

        World w=new World(100, 100, new AldousBroder());
    
        Room r=w.dungeon.getRooms().get(DungeonUtil.random.nextInt(0, w.dungeon.getRooms().size()));
        w.loadJSONEntities("creatures.json");
        w.spawnCreature(r.getCenter(), "Artjom", DungeonEntityRace.HUMAN, DungeonCreatureClass.WARRIOR, true);
        w.spawnItem(new DungeonPoint(r.getCenter().getX(), r.getCenter().getY()+1), "knife");
        
        
        System.out.println(w.getItemAt(new DungeonPoint(r.getCenter().getX()+1, r.getCenter().getY())));
         w.spawnItem(new DungeonPoint(r.getCenter().getX(), r.getCenter().getY()), "knife");
        

        //  w.spawnCreature(r.getCenter(), null, DungeonEntityRace.HUMAN, DungeonCreatureClass.WARRIOR, true);
        
        var e=w.spawnCreature(new DungeonPoint(r.getCenter().getX()+1, r.getCenter().getY()+1), null, DungeonEntityRace.HUMAN, DungeonCreatureClass.GENERIC, false);
        e.setAI();
        Scanner reader = new Scanner(System.in); // Reading from System.in
        DungeonEntityHeroInputHandler.setWorld(w);
        DungeonEntityHeroInputHandler.setEntityHandler(new DungeonConsoleOutputHandler());
        
        // Clerk.view(1003);
        
        while (true) { 
            DungeonEntityHeroInputHandler.process(reader.nextLine());
        }        
    }
}
