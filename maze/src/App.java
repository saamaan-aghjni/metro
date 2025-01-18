import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class App {
    private static Random random = new Random();
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
        World w=new World(7, 10, new Sidewinder());
System.out.println("Size of rooms: "+w.dungeon.getRooms().size());
        Room r=w.dungeon.getRooms().get(random.nextInt(0, w.dungeon.getRooms().size()));
        DungeonEntityHero hero=new DungeonEntityHero(r.getCenter());
        DungeonEntityHero hero2=new DungeonEntityHero(new DungeonPoint(r.getCenter().getX()+1, r.getCenter().getY()+1));

        Scanner reader = new Scanner(System.in); // Reading from System.in
        DungeonEntityHeroInputHandler.world = w;
        DungeonEntityHeroInputHandler.ent = hero;
        hero.setLoggerHandler(new DungeonConsoleOutputHandler());
hero2.name="asdflkj";
        DungeonEntity knife=new DungeonEntity("Knife", null, DungeonEntityRace.ITEM, null, new DungeonComponentItemStat(hero, 0, 0, 0, 0, 0, 0, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, null, null, new DamagePoint(1,6,true,5),0 , DungeonItemCategory.WEAPON, false));
        hero.setEquippedItem(knife);
        hero.addAbility(DungeonAbilityType.MELAYATTACK, true);
        w.registerEntity(hero, true);
        w.registerEntity(hero2);

        while (true) { 

            DungeonEntityHeroInputHandler.process(reader.nextLine());
            w.update();
}        
    }
}
