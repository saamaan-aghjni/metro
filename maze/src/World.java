import java.util.ArrayList;

public class World {
    private static String HERO_NAME = "Artjom";
    ArrayList<DungeonEntity> actors=new ArrayList<>();
    ArrayList<Integer> registeredActors = new ArrayList<>();
    private int currentActor=0;
    ArrayList<ArrayList<DungeonEntity>> items=new ArrayList<ArrayList<DungeonEntity>>();
    BSPDungeon dungeon;
    int width, height;
    public World(int width, int height, MazeGenerator mg) {
        this.width = width;
        this.height = height;
        Grid g= new Grid(height, width);
        dungeon = new BSPDungeon(g, mg);
        DungeonUtil.fillArray(actors, width*height, null);
        DungeonUtil.fillArray(items, width*height, null);
        placeDoorEntities();
    }
    public boolean registerEntity(DungeonEntity ent) {
        DungeonPoint p = ent.getPosition();
        if(actors.get(DungeonUtil.positionToIndex(p, height))!=null) return false;
        actors.set(DungeonUtil.positionToIndex(p, height), ent);
        registeredActors.add(DungeonUtil.positionToIndex(p, height));
        return true;
    }
    public DungeonEntity removeEntity(DungeonEntity ent) {
        DungeonPoint p = ent.getPosition();
        if(actors.get(DungeonUtil.positionToIndex(p, height))==null) return null;
        actors.set(DungeonUtil.positionToIndex(p, height), null);        
        registeredActors.remove(registeredActors.get(DungeonUtil.positionToIndex(p, height)));
        
        return ent;
    }
    public Terrain getTerrainAt(DungeonPoint p) {
        return dungeon.getTerrainAt(p);
    }
    public DungeonEntity getEntityAt(DungeonPoint p) {
        return actors.get(DungeonUtil.positionToIndex(p, height));
    }
    public boolean moveEntity(DungeonPoint pos, DungeonPoint p) {
        DungeonEntity ent=getEntityAt(pos);
        if(ent==null) return false;
        DungeonPoint oldpos = ent.getPosition();
        actors.set(DungeonUtil.positionToIndex(p, height), ent);
        registeredActors.set(registeredActors.indexOf(DungeonUtil.positionToIndex(pos, height)), DungeonUtil.positionToIndex(p, height));
        actors.set(DungeonUtil.positionToIndex(oldpos, height), null);
        ent.setPosition(p);
        return true;
    }
    public ArrayList<Room> getRooms() {
        return dungeon.getRooms();
    }
    public void update() {        
        DungeonComponentStat stat = actors.get(registeredActors.get(currentActor)).getStat();

        var comp = actors.get(registeredActors.get(currentActor)).getNextAction();
        stat.perform();
        if(comp == null) return;
        var res=comp.perform();

        while(!actors.get(registeredActors.get(currentActor)).getName() .equals(HERO_NAME)) {            
            if(!stat.isAlive()) {
                continue;
            }
            else {
                actors.get(registeredActors.get(currentActor)).getNextAction().perform();
            }
            stat = actors.get(registeredActors.get(currentActor)).getStat();
            currentActor+=1%registeredActors.size();
        }
    } 
@Override    
    public String toString() {
        Room temproom= null;
        DungeonEntity tempent=null;
        String res="";
        for(int i=0; i<dungeon.map.getRow(); i++) {
            res+="\n|";
            for(int j =0; j< dungeon.map.getCol(); j++) {
                temproom = dungeon.getRoomAt(new DungeonPoint(i, j));
                tempent=getEntityAt(new DungeonPoint(i, j));
                res+=dungeon.map.cellAt(i, j).getTerrain().getSymbol();
                if(temproom != null) res+=temproom.getName();
                // else res+="#";
                if(tempent!=null) res+=tempent.getName();
                var east_n=dungeon.map.getNeighborTo(i, j, Direction.EAST);
                if(east_n == null) { 
                    res+="|";
                    continue;
                }
        if(east_n.hasPath())  res+=".";
                if(!east_n.hasPath())  res+="|";
            }
            res+="\n|";
            for(int j =0; j< dungeon.map.getCol(); j++) {
                var east_n=dungeon.map.getNeighborTo(i, j, Direction.SOUTH);
                if(east_n == null) { 
                    res+="--|";
                    continue;
                }
        if(east_n.hasPath())  res+="..|";
                if(!east_n.hasPath())  res+="----|";
            }
            }
        
        return res;
    }
    private void placeDoorEntities() {
        for(Room room: dungeon.getRooms()) {
            var exits = room.getExits();
            if(exits.isEmpty()) {
                continue;
            }
            exits.forEach(p -> {
                int rand=DungeonUtil.random.nextInt(0,4);
                if(rand<=2) {
                    DungeonEntityDoor d = new DungeonEntityDoor(p, DungeonDoorType.WOODEN, 100.0, 100.0);
                    registerEntity(d);
                    dungeon.map.cellAt(p).setTerrain(d.terrainWhenClosed());
                }
                else {
                    dungeon.map.cellAt(p).setTerrain(Terrain.DIRT);
                }
            });
        }
    }
}
