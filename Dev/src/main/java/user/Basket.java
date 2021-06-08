package user;

import persistence.Repo;
import store.Item;
import store.ItemId;
import store.Store;

import javax.persistence.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
@Entity
@IdClass(BasketId.class)

public final class Basket {
    @Id
    private int store_id;

//    @Transient
@ElementCollection
@MapKeyJoinColumns({
        @MapKeyJoinColumn(name="item_id"),
        @MapKeyJoinColumn(name="store_id")
})
    private final Map<Item, Integer> items;

    @Id
    private String username;

    public Basket(User user, Store store, Map<Item, Integer> items) {
        this.store_id = store.getId();
        this.items = items;
        if(user instanceof Subscriber)
            this.username = ((Subscriber)user).getUserName();
    }

    public Basket() {
        items = new ConcurrentHashMap<>();
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addItem(Item item, int quantity) {
        items.compute(item, (k, v) -> v == null ? quantity : v + quantity); // add to existing quantity

        EntityManager em = Repo.getEm();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
//            em.merge(store);
            em.merge(this);
            et.commit();
        }
        catch (Exception e){
            if(et != null){
                et.rollback();
            }
            e.printStackTrace();
        }
        finally {
//            em.close();
        }
    }

    public int getQuantity(Item item) {
        return items.getOrDefault(item, 0);
    }

    public void setQuantity(Item item, int quantity) {
        if (quantity == 0)
            removeItem(item);
        else
            items.put(item, quantity);
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Map<Item, Integer> items() {
        return items;
    }



    @Override
    public String toString() {
        return "Basket[" +
                "store=" + store_id + ", " +
                "items=" + items + ']';
    }

}
