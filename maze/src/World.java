/**
 * https://journal.stuffwithstuff.com/2008/11/17/using-an-iterator-as-a-game-loop/
 * https://gameprogrammingpatterns.com/component.html
 * https://medium.com/@guribemontero/dungeon-generation-using-binary-space-trees-47d4a668e2d0
**/

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public final class World {
    private static UUID heroEntityID = null;
    private ArrayList<DungeonEntity> actors=new ArrayList<>();
    private ArrayList<Integer> registeredActors = new ArrayList<>();
    private int currentActor=0;
    private ArrayList<ArrayList<DungeonEntity>> items=new ArrayList<ArrayList<DungeonEntity>>();
    BSPDungeon dungeon;    
    public World(int width, int height, MazeGenerator mg) {
        dungeon = new BSPDungeon(width, height, mg);
        DungeonUtil.fillArray(actors, width*height, null);
        DungeonUtil.fillArray(items, width*height, null);
        placeDoorEntities();
    }
    public boolean registerEntity(DungeonEntity ent, boolean isHero) {
        DungeonPoint p = ent.getPosition();
        if(actors.get(DungeonUtil.positionToIndex(p, dungeon.getRow()))!=null) {
            return false;
        }
        actors.set(DungeonUtil.positionToIndex(p, dungeon.getRow()), ent);

        registeredActors.add(DungeonUtil.positionToIndex(p, dungeon.getRow()));
        if(isHero && heroEntityID == null) {
            heroEntityID = ent.getID();
            currentActor = registeredActors.size()-1;
            
        }

        return true;
    }
    boolean registerEntity(DungeonEntity ent) {
        return registerEntity(ent, false);
    }
    public DungeonEntity removeEntity(DungeonEntity ent) {
        int index  = DungeonUtil.positionToIndex(ent.getPosition(), dungeon.getRow());
        
        if(actors.get(index)==null) {
            return null;
        }
        actors.set(index, null);        
        int ra = registeredActors.indexOf(index);
        if(ra>=0) {
            
            registeredActors.remove(ra);
            
        }
        if(ent.getID().equals(heroEntityID)) {
            heroEntityID = null;
        }
        return ent;
    }
    public DungeonEntity getEntityAt(DungeonPoint p) {
        int index = DungeonUtil.positionToIndex(p, dungeon.getRow());
        return index >= dungeon.getRow() * dungeon.getCol() ? null :actors.get(index);
    }
    public boolean moveEntity(DungeonPoint pos, DungeonPoint p) {
        DungeonEntity ent=getEntityAt(pos);
        if(ent==null) {
            return false;
        }
        DungeonPoint oldpos = ent.getPosition();
        actors.set(DungeonUtil.positionToIndex(p, dungeon.getRow()), ent);
        registeredActors.set(registeredActors.indexOf(DungeonUtil.positionToIndex(pos, dungeon.getRow())), DungeonUtil.positionToIndex(p, dungeon.getRow()));
        actors.set(DungeonUtil.positionToIndex(oldpos, dungeon.getRow()), null);
        ent.setPosition(p);
        return true;
    }
    
    public void update() {        
        DungeonEntity hero = getRegisteredEntityAt(currentActor);

        DungeonComponentStat stat = hero.getStat();
System.out.println(heroEntityID.equals(hero.getID()));
        var comp = hero.getNextAction();
        if(comp == null) {
            System.out.println("Action null");
            return;
        }
        if(!stat.isAlive()) {
            hero.log(Level.SEVERE, "You have died!");
            removeEntity(hero);
            return;
        }
        if(comp.getCost() <= 0.0) {
            var res = comp.perform();
            hero.setNextAction(null);
        }
        else {
            var energyReduction=stat.getEnergy() -comp.getCost();
            System.out.println("energy "+stat.getEnergy()+" cost was "+comp.getCost());
            
            if(energyReduction <= 0.0) {
                hero.log(Level.WARNING, "You need a moment to rest!");

                hero.setNextAction(null);
                updateOtherEntities();
                cleanupEntities();
            }
            else {
                
                stat.setEnergy(energyReduction);
                comp.perform();
                hero.setNextAction(null);
            }
        }
        // updateItems();
    } 

    private void cleanupEntities() {
        for(int i=0; i<registeredActors.size(); i++) {
            DungeonEntity current = getRegisteredEntityAt(i);
            var stat = current.getStat();
            if(!stat.isAlive() && !current.getID().equals(heroEntityID)) {
                removeEntity(current);
                continue;
            }
        }
    }
    private void updateOtherEntities() {
        DungeonEntity currentEntity = null;
        System.out.println("other");
        while(true) 
        {
            currentActor = (currentActor+1)%registeredActors.size();
            System.out.println("currentactor "+currentActor);
            currentEntity = getRegisteredEntityAt(currentActor);
            var stat = currentEntity.getStat();
            
            stat.perform();
                
            if(currentEntity.getID().equals(heroEntityID)) {
                break;
            }
            var comp = currentEntity .getNextAction();
            if(comp==null) {
                continue;
            }
            if(comp.getCost()<=0.0) {
                comp.perform();
                currentEntity.setNextAction(DungeonComponent.DUMMY);
                continue;
            }
            double energyReduction = stat.getEnergy() - comp.getCost();
            if(energyReduction<=0.0) {
                currentEntity.setNextAction(DungeonComponent.DUMMY);
            }
            else {
                while(stat.getEnergy() >0.0) {
                    stat.setEnergy(energyReduction);
                    comp.perform();
                    energyReduction = stat.getEnergy() - comp.getCost();
                }
            }
            stat.perform();
        }
    }
    private DungeonEntity getRegisteredEntityAt(int index) {
        if(index>=registeredActors.size() || index<0) { 
            return null;
        }
        return actors.get(registeredActors.get(index));
    }
@Override    
    public String toString() {
        Room temproom= null;
        DungeonEntity tempent=null;
        String res="";
        DungeonPoint p;
        for(int i=0; i<dungeon.getRow(); i++) {
            res+="\n|";
            for(int j =0; j< dungeon.getCol(); j++) {
                p = new DungeonPoint(i ,j);
                temproom = dungeon.getRoomAt(p);
                tempent=getEntityAt(p);
                res+=dungeon.getTerrainAt(p).getSymbol();
                if(temproom != null) res+=temproom.getName();
                // else res+="#";
                if(tempent!=null) res+=tempent.getName();
                var east_n=dungeon.hasPathTo(p, Direction.EAST);
                if(!east_n ) { 
                    res+="|";
                    continue;
                }
                else  res+=".";                
            }
            res+="\n|";
            for(int j =0; j< dungeon.getCol(); j++) {
                p = new DungeonPoint(i ,j);
                var east_n=dungeon.hasPathTo(p, Direction.SOUTH);
                if(!east_n ) { 
                    res+="--|";
                    continue;
                }
        else res+="..|";
            
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
                    dungeon.setTerrainAt(p ,d.terrainWhenClosed());
                }
                else {
                    dungeon.setTerrainAt(p,Terrain.DIRT);
                }
            });
        }
    }
}
