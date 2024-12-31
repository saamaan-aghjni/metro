
public  class DungeonEntityHeroInputHandler {
    static DungeonEntity ent;
    static World world;
    private static String getVirbalDirectionTo(DungeonPoint orig, DungeonPoint p) {
        String res="";
        
        int dx=p.getX() - orig.getX(), dy = p.getY() - orig.getY();
        
        if(dx<0) {
            res+="north";
        }
        else if(dx>0) {
            res+="south";
        }

        if(dy>0) {
            res+="east";
        }
        else if(dy<0) {
            res+="west";
        }
        return res;
    }

    public static void lookAround() {
        Room temp = world.dungeon.getRoomAt(ent.getPosition());
        if(temp==null) {
            System.out.println("You are not in a specific room right now!");
            return;
        }
        System.out.println("Some sort of a room. ");
        if(temp.getExits().isEmpty()) {
            System.out.println(" There are no exits!");
        }
        else {
            if(temp.getExits().size()==1) {
                System.out.println("There is an exit at ");
        }
        else {
            System.out.println("There are exits at ");
        }
    
        temp.getExits().forEach(p -> {
             System.out.print(" "+getVirbalDirectionTo(ent.getPosition(), p)+", ");
        });
    }
    }
    public static void renderMap(World w) {
        System.out.println(w.toString());
    }
    public static void listHelp() {
        System.out.println("""
            Go/Go/G/g N/S/E/W: Move in the specified direction.
            Show/show/S/s map/Map/m/M: Render world's content to output.
            look/Look/L/l: Perform a 360-view of where you're standing.
            Help/help/h/H: Show this message :|)
            """);
    }
    public static void move(String d) {
        var stat=ent.getStat();
if(!DungeonUtil.lowerBound(stat.getEnergy(),10.0)) {
    System.out.println("You still need sometime to rest!");
}
        switch(d) {
            case "north":
            case "North":
            case "N":
            case "n":
                ent.setNextAction(new DungeonAbilityMove(ent, world,Direction.NORTH));
                break;
            case "south":
            case "South":
            case "S":
            case "s":
                ent.setNextAction(new DungeonAbilityMove(ent, world,Direction.SOUTH));
                break;
                case "west":
                case "West":
                case "W":
                case "w":
                    ent.setNextAction(new DungeonAbilityMove(ent, world,Direction.WEST));
                    break;
                case "East":
                case "east":
                case "E":
                case "e":
                    ent.setNextAction(new DungeonAbilityMove(ent, world,Direction.EAST));
                    break;
                default:
                    System.out.println("Huh?");
                    break;
        }
    }
    public static void process(String c) {
        String[] s=c.split(" ");
        if(s.length==0) {
            System.out.println("I don't understand you :)))");
            return;
        }
        switch(s[0]) {
            case "Help":
            case "help":
            case "H":
            case "h":
                listHelp();
                break;
            case "Go":
            case "go":
            case "G":
            case  "g":
                move(s[1]);
                break;
            case "Show":
            case "show":
            case "S":
            case "s":
				if(s.length==1) {
					System.out.println("To show or not to show, that is the question!");
					return;
				}
				switch(s[1]) {				
					case "Map":
					case "map":
					case "M":
					case "m":
		                renderMap(world);
		                break;
                    case "stats":
                    case "Stats":
                    case "status":
                    case "Status":
                    case "stat":
                    case "Stat":
                        System.out.println(ent.toString());
                        break;
					default:
						System.out.println("Show what?");
						break;
				}
				break;
            case "Look":
            case "look":
            case "l":
            case "L":
                lookAround();
                break;
            default:
                System.out.println("I can't understand you! Use \"help\", \"Help\", \"H\" or \"h\" to get a list of commands for "+ent.getName());
                break;
        }
    }
}
