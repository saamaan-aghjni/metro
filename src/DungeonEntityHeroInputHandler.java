import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;

public  class DungeonEntityHeroInputHandler {
    private static DungeonEntity ent;
    private static World world;
    private static Handler lastHandler;
    private static Direction getDirectionFrom(String d) {
        Direction dir =null;
        switch(d.toLowerCase()) {
            case "north":
            case "n":
                dir = Direction.NORTH;
                break;
            case "south":
            case "s":
                dir = Direction.SOUTH;
                break;
            case "west":
            case "w":
                dir = Direction.WEST;
                break;
            case "east":
            case "e":
                dir = Direction.EAST;
                break;
            default:
                ent.log(Level.INFO,"Huh?");
                break;
        }
        return dir;
    }

    private static void equip(String item) {
        if(!ent.hasAbilityEnabled(DungeonAbilityType.EQUIP)) {
            ent.log(Level.INFO, "You are not able to equip any items!");
            return;
        }
        ent.setNextAction(new DungeonAbilityEquip(ent, item));
    }        
    private static void unequip() {
        if(!ent.hasAbilityEnabled(DungeonAbilityType.EQUIP)) {
            ent.log(Level.INFO, "You are not able to equip any items!");
            return;
        }
        ent.setNextAction(new DungeonAbilityUnequip(ent));
    }        

    private static void show(String what) {
        switch(what.toLowerCase()) {
            case "Map":
            case "m":
                renderMap(world);
                break;
            case "stats":
            case "status":
                ent.log(Level.INFO,ent.toString());
                break;
            default:
                ent.log(Level.INFO,"Show what?");
                break;
        }
    
    }

    private static void attack(String d) {
        if(!ent.hasAbilityEnabled(DungeonAbilityType.MELAYATTACK)) {
            ent.log(Level.INFO,"You are incapable of doing a melay attack!");
            return;
        }
        Direction dir=getDirectionFrom(d);
        if(dir != null) {
            ent.setNextAction(new DungeonAbilityMelayAttack(ent, world, dir));
        }
    }
    

    private static boolean isBlocked(DungeonPoint p) {
        return world.isInWorld(p) && !Terrain.isPassable(world.dungeon.getTerrainAt(p));
    }

    private static Direction getDirectionTo(DungeonPoint point, DungeonPoint other) {
        if(point.getX() == other.getX()) {
            return point.getY() > other.getY() ? Direction.EAST : Direction.WEST;
        }
        else if(point.getY() == other.getY()) {
            return point.getX() > other.getX() ? Direction.NORTH : Direction.SOUTH;
        }
        return null;
    }

    private static boolean isVisible( DungeonPoint p) {
        return true;
    }
    private static void loadGame() {
        try {
            
            world=World.load("gameSave");
            ent = world.getHero();
            ent.setLoggerHandler(lastHandler);
            ent.log(Level.INFO, "Loaded!");    
        }
        catch(Exception i) {
            System.err.println("An error occured! "+i.getMessage()+" "+i.getCause());
        }
    }

    private static void saveGame() {
        try {
            world.saveGame("gameSave");
            ent.log(Level.INFO, "Saved!");
        }
        catch(Exception i) {
            System.err.println("Error! "+i.getMessage()+" "+i.getCause());
        }
    }

    private static void lookAround() {
        ArrayList<DungeonPoint> points=DungeonFOV.castShadow(ent.getPosition(), 5, DungeonEntityHeroInputHandler::isBlocked, DungeonEntityHeroInputHandler::isVisible);
        StringBuilder sb=new StringBuilder();
        DungeonPoint entPos=ent.getPosition();
        DungeonPoint prev=null;
        for(int i=entPos.getX()-5; i<entPos.getX()+5; i++) {
            sb.append("\n");
            
            for(int j=entPos.getY()-5; j<entPos.getY()+5; j++) {
                
                if( world.isInWorld(new DungeonPoint(i, j)) && points.indexOf(new DungeonPoint(i, j))>=0 ) {
                    if(isVisible(new DungeonPoint(i, j))) {
                        sb.append(world.dungeon.getTerrainAt(new DungeonPoint(i, j)).symbol);
                    }
                    else {
                        sb.append("("+world.dungeon.getTerrainAt(new DungeonPoint(i, j)).symbol+")");
                    }

                    sb.append(whatIsOn(new DungeonPoint(i, j)));
                }
                else {
                    if(ent.getPosition().equals(new DungeonPoint(i, j))) {
                        sb.append("@");
                    }
                    else {
                        sb.append(" ");
                    }                
                }
            }
        }
        ent.log(Level.INFO, sb.toString());
    }
    private static void dropItem() {
        if(!ent.hasAbilityEnabled(DungeonAbilityType.ITEMPICKUP)) {
            ent.log(Level.INFO, "You can't drop items!");
            return;
        }
        ent.setNextAction(new DungeonAbilityDrop(ent, world));
    }
    private static void pickup() {
        if(!ent.hasAbilityEnabled(DungeonAbilityType.ITEMPICKUP)) {
            ent.log(Level.INFO, "You cannot pickup items!");
            return;
        }
        ent.setNextAction(new DungeonAbilityPickup(ent, world));
    }
    private static String whatIsOn(DungeonPoint point) {
        StringBuilder res=new StringBuilder();
        if(world.getEntityAt(point) ==null && world.getItemAt(point) == null || (world.getEntityAt(point) !=null && world.getEntityAt(point).getRace() != DungeonEntityRace.DOOR)) {
            return "";
        }
        res.append("( ");
        if(world.getEntityAt(point)!=null) {
            res.append(world.getEntityAt(point).getName()+", ");
        }
        if(world.getItemAt(point)!=null) {
            res.append(world.getItemAt(point).getName()+", ");
        }
        res.append(" )");
        return res.toString();
    }

    private static void renderMap(World w) {
        ent.log(Level.INFO,w.toString());
    }

    private static void listHelp() {
        ent.log(Level.INFO,"""
            Go/Go/G/g N/S/E/W: Move in the specified direction.
            Show/show/S/s map/Map/m/M: Render world's content to output.
            look/Look/L/l: Perform a 360-view of where you're standing.
            attack/Attack/atck/Atck <direction> : Try to melay-attack in the direction specified with the currently held item .
            Help/help/h/H: Show this message :|)
            load/Load:  Load the game.
            save/Save: Save the game.
            """);
    }

    private static void move(String d) {
        Direction dir = getDirectionFrom(d);
        if(dir !=null) {
            ent.setNextAction(new DungeonAbilityMove(ent, world, dir));
        }
    }

    public static void process(String c) {
        String[] s=c.split(" ");
        if(s.length==0) {
            ent.log(Level.INFO,"I don't understand you :)))");
            return;
        }
        switch(s[0].toLowerCase()) {
            case "help":
            case "h":
                listHelp();
                break;
            case "save":
                saveGame();
                break;
            case "load":
                loadGame();
                break;
            case "go":
            case  "g":
                if(!ent.hasAbilityEnabled(DungeonAbilityType.MOVEMENT)) {
                    ent.log(Level.INFO,"You can't move!");
                    return;
                }
                if(s.length==2) {
                    move(s[1]);
                }
                break;
            case "equip":
            case "e":
                if(s.length==1) {
                    ent.log(Level.INFO, "Equip what?");
                    return;
                }
                equip(s[1]);
                break;
            case "unequip":
            case "ue":
                unequip();
                break;
            case "Interract":
            case "i":
                if(!ent.hasAbilityEnabled(DungeonAbilityType.INTERRACTION)) {
                    ent.log(Level.INFO, "You can't interract!");
                    return;
                }
                ent.setNextAction(new DungeonAbilityInterraction(ent, world));
                break;
            case "pickup":
            case "p":
                pickup();
                break;
            case "drop":
            case "d":
                dropItem();
                break;
            case "show":
            case "s":
				if(s.length==1) {
					ent.log(Level.INFO,"To show or not to show, that is the question!");
					return;
                }
                show(s[1]);
                break;
            case "look":
            case "l":
                lookAround();
                break;
            case "attack":
            case "atck":
                if(s.length!=2) {
                    ent.log(Level.INFO,"Ican't understand you, use attack <direction>!");
                    return;
                }
                attack(s[1]);
                break;
            default:
                ent.log(Level.INFO,"I can't understand you! Use \"help\", \"Help\", \"H\" or \"h\" to get a list of commands for "+ent.getName());
                break;
        }
        world.update();
    }
    public static void setEntity(DungeonEntity e) {
        ent = e;
    }
    public static void setWorld(World w) {
        world = w;
        ent = w.getHero();
    }
    public static void setEntityHandler(Handler h) {
        lastHandler = h;
        ent.setLoggerHandler(h);
    }
    private DungeonEntityHeroInputHandler() {
        
    }
}
