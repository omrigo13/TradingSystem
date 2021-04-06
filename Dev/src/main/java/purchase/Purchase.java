package purchase;

import java.util.Collection;
import java.util.Map;

public class Purchase {

    private final Map<Integer, Collection<Integer>> storeItems; //<<storeID, List<ItemID>>
    private final String details;
    private final int id;

    public Purchase(int id, Map<Integer, Collection<Integer>> storeItems, String details) {
        this.id = id;
        this.storeItems = storeItems;
        this.details = details;
    }


    public Map<Integer, Collection<Integer>> getStoreItems() {
        return storeItems;
    }

    public String getDetails() {
        return details;
    }
}
