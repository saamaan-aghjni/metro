import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Manage the entity reader/writer cleanly using this class.
 * @author saman
 */

public class DungeonEntityDataManager  {
    private JSONObject data;
    private DungeonEntityDataManager(String data) {
        this.data = new JSONObject(data);
    }
    public static DungeonEntityDataManager loadFile(String filename) {
        DungeonEntityDataManager result = null;
        try {
            var buffer = new BufferedReader(new FileReader(filename));
            StringBuilder sb=new StringBuilder();
            buffer.lines().forEach(l -> {
                sb.append(l);
            });
            result = new DungeonEntityDataManager(sb.toString());
            buffer.close();
        }
        catch(IOException e) {
            return null;
        }
        return result;
    }
    private DamagePoint readDamagePoint(JSONObject dp) throws JSONException {        
        return new DamagePoint(dp.getInt("dNumbers"), dp.getInt("dSides"), dp.getBoolean("negativeDamage"), dp.getInt("dist"));
    }

    private DungeonComponentItemStat readItemStats(JSONObject data) {
        
        DungeonComponentItemStat result=null;
        try {
            boolean isPickable = data.getBoolean("isPickable");            
            int hp=data.getInt("hitpoints"), maxHp=data.getInt("maxHitpoints"), str=data.getInt("str"), maxStr=data.getInt("maxStr"), dex=data.getInt("dex"), maxDex=data.getInt("maxDex"), con=data.getInt("con"), maxCon=data.getInt("maxCon"), wis=data.getInt("wis"), maxWis=data.getInt("maxWis"), level = data.getInt("level"), thaco=data.getInt("thaco"), mana = data.getInt("mana"), maxMana=data.getInt("maxMana"), intl=data.getInt("intel"), maxIntl=data.getInt("maxIntel");
            double energy = data.getDouble("energy"), recoverEnergy=data.getDouble("recoverEnergy");
            DamagePoint throwDP=readDamagePoint((JSONObject)data.get("throwDP")), melayDP=readDamagePoint((JSONObject)data.get("melayDP")), rangeDP=readDamagePoint((JSONObject)data.get("rangeDP"));
            int weight=data.getInt("weight");
            switch(data.getEnum(DungeonItemCategory.class, "type")) {
                case GENERIC:
                    result = new DungeonComponentItemStat(null, hp, maxHp, energy, recoverEnergy, mana, maxMana, str, maxStr, intl, maxIntl, con, maxCon, wis, maxWis, 0.0, level, throwDP, rangeDP, melayDP, weight, DungeonItemCategory.GENERIC, isPickable);
                    break;
                case WEAPON:
                    DungeonWeaponType wtype=data.getEnum(DungeonWeaponType.class, "weaponType");
                    result = new DungeonComponentWeaponStat(null, hp, maxHp, energy, recoverEnergy, mana, maxMana, str, maxStr, intl, maxIntl, con, maxCon, wis, maxWis, 0.0, level, throwDP, rangeDP, melayDP, weight, isPickable, wtype);
                    break;
                default:
                    break;
            }
        }
        catch(JSONException e) {

            return null;
        }
        return result;
    }
    private DungeonComponentCreatureStat readCreatureStats(JSONObject data, DungeonCreatureClass eClass) {
        DungeonComponentCreatureStat result = null;        
        try {
            data = data.getJSONObject("stats");
            int hp=data.getInt("hitpoints"), maxHp=data.getInt("maxHitpoints"), str=data.getInt("str"), maxStr=data.getInt("maxStr"), dex=data.getInt("dex"), maxDex=data.getInt("maxDex"), con=data.getInt("con"), maxCon=data.getInt("maxCon"), wis=data.getInt("wis"), maxWis=data.getInt("maxWis"), level = data.getInt("level"), thaco=data.getInt("thaco"), mana = data.getInt("mana"), maxMana=data.getInt("maxMana"), intl=data.getInt("intel"), maxIntl=data.getInt("maxIntel");
            double energy = data.getDouble("energy"), recoverEnergy=data.getDouble("recoverEnergy");
            
            result = new DungeonComponentCreatureStat(null, hp, maxHp, energy, recoverEnergy, mana, maxMana, str, maxStr, intl, maxIntl, con, maxCon, wis, maxWis, 0.0, level, thaco, 10, eClass);
        }
        catch(JSONException j) {
            return null;
        }
        return result;
    }
    private String readName(String filename)  throws IOException {
        var buffer = new BufferedReader(new FileReader(filename));
        var names=buffer.lines().toList();
        String randomName = names.get(DungeonUtil.random.nextInt(0, names.size()));
        buffer.close();
        return randomName;
    }
    public DungeonEntity createItem(String name, DungeonPoint point) {
        JSONObject obj = getRaceData(DungeonEntityRace.ITEM);
        JSONObject classData = getClassData(obj.getJSONArray("categories"), name);
        
        JSONObject defaultClassData = getClassData(obj .getJSONArray("categories"), "default");
        
        DungeonComponentStat s=readItemStats(classData.getJSONObject("stats"));
        if(s == null) {
            s = readItemStats(defaultClassData);
        }
        DungeonEntity item = new DungeonEntity(name, point, DungeonEntityRace.ITEM, s);
        s.setOwner(item);
        try {
            readEntityAbilities(item, defaultClassData.getJSONArray("abilities"));
            readEntityAbilities(item, classData.getJSONArray("abilities"));
        }
        catch(JSONException e) {
        }
        return item;
    }
    private DungeonInventory readInventory(JSONObject obj) {
        try {
            var invItems = obj.getJSONArray("items");
            DungeonInventory inv = new DungeonInventory(obj.getInt("totalWeightAllowed"));
            invItems.forEach(i -> {
                try {
                    inv.add(createItem(((JSONObject)i).getString("name"), null));
                }
                catch(InventoryException e) {
                    
                }
            });            
            return inv;
        }
        catch(JSONException j) {
            
        }
        
        return null;
    }
    public DungeonEntity createCreature(String name, DungeonPoint point, DungeonEntityRace race, DungeonCreatureClass eClass) {
        if(race == null || race == DungeonEntityRace.ITEM) {
            return null;
        }
        JSONObject obj = getRaceData(race);
        JSONObject classData = getClassData(obj.getJSONArray("classes"), eClass, DungeonCreatureClass.class);
        JSONObject defaultClassData = getClassData(obj .getJSONArray("classes"), DungeonCreatureClass.GENERIC, DungeonCreatureClass.class);
        DungeonInventory inv=        readInventory(classData.getJSONObject("inventory"));
        DungeonComponentStat s=readCreatureStats(classData, eClass);
        if(s == null) {
            s = readCreatureStats(defaultClassData, eClass);
        }
        if(name == null) {
            try {
                name = readName(obj.getString("names"));
            }
            catch(IOException e) {
                name = "";
            
            }
        }
        DungeonEntity creature = new DungeonEntity(name, point, race, s, inv);
        s.setOwner(creature);
        readEntityAbilities(creature, defaultClassData.getJSONArray("abilities"));
        readEntityAbilities(creature, classData.getJSONArray("abilities"));
        return creature;
    }
    private void readEntityAbilities(DungeonEntity ent, JSONArray abilities) {
        for(int i=0; i<abilities.length(); i++) {
            ent.addAbility(abilities.getEnum(DungeonAbilityType.class, i), true);
        }
    }

    private <E extends Enum<E>> JSONObject getClassData(JSONArray datas, E whatClass, Class<E> eClass) {
        for(var data: datas) {
            var tmp = (JSONObject)data;
            if(whatClass.equals(tmp.getEnum(eClass, "type"))) {
                return tmp;
            }
        }
        return null;
    }
    
    private  JSONObject getClassData(JSONArray datas, String name) {
        for(var data: datas) {
            var tmp = (JSONObject)data;
            if(name.equals(tmp.get("name"))) {
                return tmp;
            }
        }
        return null;
    }

    private JSONObject getRaceData(DungeonEntityRace race) {
        var entities = data.getJSONArray("entities");
        /*streams don't work here.  Calling entities.toList().stream() returns hashmap - why? */
        /*  Use smart for-loops :) */
        for(int i=0; i<entities.length(); i++)
        {
            /* It'll surely blow up with invalid data.  Too lazy to sanitize :(*/
            if(((JSONObject)entities.get(i)).getEnum(DungeonEntityRace.class, "race") == race) {
                return (JSONObject)entities.get(i);
            }
        }
        return null;
    }
}