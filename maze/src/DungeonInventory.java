import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

/* No more extending DungeonEntity please?
 * So DungeonInventory should only hold items.  Not rats/mutants/humans/bla bla-race.
 * Does everyone own inventories?  Yes.  What about rats?  Well we suppose they carry a small pouch on their bellies :)
 * Okay, I should think up a way to disable inventory for that.  Perhaps include inventory, only when it is stated in the JSON configfile.
 */

class  InventoryException  extends Exception {
    InventoryException(String message) {
        super(message);
    }
}

 class DungeonInventory {
    // Brute-force idea: Keep a key/value of all items, with key being the string name and value the list of items of that name plus the reference to the items. 
    private HashMap<String, Stack<DungeonEntity>> objects = new HashMap<>();
    private int currentCarryingWeight=0, totalWeightAllowed;

    DungeonInventory(int w) {
        totalWeightAllowed = w;
    }
    public DungeonEntity remove(String name) throws InventoryException {
        if(!contains(name)) {
            throw new InventoryException("Item not found to remove! "+name);
        }
        DungeonEntity top = objects.get(name).pop();
        currentCarryingWeight  -= ((DungeonComponentItemStat)top.getStat()).getWeight();
        return top;
    }
    public void add(DungeonEntity entItem) throws InventoryException {
        if(entItem.getRace() != DungeonEntityRace.ITEM) {
            // Save future possible headaches
            throw new InventoryException("You  can store  items only!");
        }
        DungeonComponentItemStat stat = (DungeonComponentItemStat)entItem.getStat();
        if(currentCarryingWeight+stat.getWeight() > totalWeightAllowed) {
            throw new InventoryException("This item is too heavy to carry!");
        }
        if(!contains(entItem.getName())) {
            objects.put(entItem.getName(), new Stack<>());
        }
        var stored = objects.get(entItem.getName());
        stored.push(entItem);
        currentCarryingWeight += stat.getWeight();
    }
    public Set<String> getItems() {
        return objects.keySet();
    }
    public boolean contains(String name) {
        return objects.containsKey(name);
    }
    public boolean isFull() {
        return currentCarryingWeight == totalWeightAllowed;
    }
    @Override
    public String toString() {
        return objects.keySet()
            .stream()
            .reduce((a, b) -> {
                return objects.get(a).size() +" "+a+"\n"+objects.get(b).size()+" "+b;
            })
        .get();
    }
}