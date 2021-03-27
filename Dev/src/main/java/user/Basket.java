package user;

import user.User;

import java.util.Collection;

//represents a User's basket, which is connected to maximum 1 store
public class Basket {

    private String store;
    private User user;
    private Collection<String> items;

    public Basket(String store, User user) {
        this.store = store;
        this.user = user;
    }

    public void addItem(String item, int amount)
    {

    }

    public Collection<String> getItems()
    {
        return this.items;
    }

    public void deleteItem(String item)
    {
        items.remove(item);
    }

    public void changeAmount(String item, int amount)
    {

    }
}
