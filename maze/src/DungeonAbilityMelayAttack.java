import java.util.logging.Level;

class DungeonAbilityMelayAttack extends DungeonComponent {
    private Direction dir;
    private World world;
    public DungeonAbilityMelayAttack(DungeonEntity owner, World world, Direction dir) {
        super(owner);
        this.world = world;
        this.dir = dir;
    }
    @Override
    public DungeonComponentResult perform() {
        if(owner.getEquipped() == null) {
            owner.log(Level.WARNING, "You are currently holding nothing to do melay attack with!");
            return null;
        }
        DungeonEntity equip = owner.getEquipped();
        if(equip.getRace()!= DungeonEntityRace.ITEM) {
            owner.log(Level.WARNING, "You can't do melay with that "+equip.getRace()+" you're holding!");
            return null;
        }
        var melay = ( (DungeonComponentItemStat)equip.getStat()).getMelayDP();
        DungeonPoint p=owner.getPosition();
        int range = melay.dist();
        DungeonEntity target = null;
        
        BSPDungeon grid = world.dungeon;
        owner.log(Level.INFO, "You charge with your "+equip.getName()+"!");
        for(int i=0; i<range; i++) {
            p = DungeonUtil.movePoint(p, dir, 1);
            target = world.getEntityAt(p);
            Terrain terrainObstruction = grid.getTerrainAt(p);
            if(terrainObstruction != null && !Terrain.isPassable(terrainObstruction)) {
                // We hit some terrain obstruction (walls/ out of map edges).  There is a 40-50% chance of the melay weapon being damaged
                int perc=DungeonUtil.rollPercentileDice();
                if(perc>=40 && perc <= 50) {
                    owner.log(Level.INFO, "Your "+equip.getName()+" hit an obstruction and took damage! -1 strength");
                    equip.getStat().setStr( equip.getStat().getStr()-1);                    
                }
                break;
            }
            if(target != null) {
                int toHit = owner.getStat().getThaco() - target.getStat().getArmorClass() -equip.getStat().getStr();
                int attackRoll = DungeonUtil.rollDice(1, 20);
                owner.log(Level.INFO, "To hit "+toHit+", attack roll "+attackRoll);
                if(attackRoll >= toHit) // Do we land a blow? 
                {
                    int hit=DungeonUtil.rollDice(melay.diceNumbers(), melay.diceSides()) * (melay.isNegative() ? -1 : 1);
                    target.getStat().addToHitpoints(hit);
                    int targethp=target.getStat().getHitpoints(), targetmaxhp=target.getStat().getMaxHitpoints();
                    owner.log(Level.INFO ,"You hit "+target.getName()+" with a damage of "+hit+"! "+target.getName()+"'s Hitpoints: "+targethp+"/"+targetmaxhp);
                }
                else {
                    owner.log(Level.INFO, "You try to attack "+target.getName()+" but miss!");
                }
                break;
            }

        }

        return new DungeonComponentResult(DungeonComponentResultType.SUCCESS, null, null);
    }
    @Override
    public double getCost() {
        var equipment = owner.getEquipped();
        if(equipment == null || (equipment !=null && equipment.getRace() != DungeonEntityRace.ITEM)) {
            return 0.0;
        }
        return (double)((DungeonComponentItemStat)equipment.getStat()).getStr()/(double)((DungeonComponentItemStat)equipment.getStat()).getMaxStr();
    }
}