package store;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Store {

    private int id;
    private String name;
    private String description;
    private Inventory inventory;

    public Store(int id, String name, String description) throws Exception{
        if (name == null || name.isEmpty() || name.trim().isEmpty())
            throw new WrongName("store name is null or contains only white spaces");
        if (name.charAt(0) >= '0' && name.charAt(0) <= '9')
            throw new WrongName("store name cannot start with a number");
        if (description == null || description.isEmpty() || description.trim().isEmpty())
            throw new WrongName("store description is null or contains only white spaces");
        if (description.charAt(0) >= '0' && description.charAt(0) <= '9')
            throw new WrongName("store description cannot start with a number");
        this.id = id;
        this.name = name;
        this.description = description;
        this.inventory = new Inventory();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ConcurrentHashMap<Item, Integer> getItems() {
        return this.inventory.getItems();
    }

    public Item getItem(String name, String category, String subCategory) throws Exception {
        return this.inventory.searchItem(name, category, subCategory);
    }

    public void setItemPrice(String name, String category, String subCategory, double price) throws Exception {
        this.inventory.setItemPrice(name, category, subCategory, price);
    }

    public void addItem(String name, double price, String category, String subCategory, double rating, int amount) throws Exception {
        this.inventory.addItem(name, price, category, subCategory, rating, amount);
    }

    public void addItem(String name, double price, String category, String subCategory, int amount) throws Exception {
        this.inventory.addItem(name, price, category, subCategory, amount);
    }

    public ConcurrentLinkedQueue<Item> searchItemByName(String name) throws Exception {
        return this.inventory.searchItemByName(name);
    }

    public Item searchItem(String name, String category, String subCategory) throws Exception {
        return this.inventory.searchItem(name, category, subCategory);
    }

    public void changeQuantity(String name, String category, String subCategory, int amount) throws Exception {
        this.inventory.changeQuantity(name, category, subCategory, amount);
    }

    public void decreaseByOne(String name, String category, String subCategory) throws Exception {
        this.inventory.decreaseByOne(name, category, subCategory);
    }

    public void removeItem(String name, String category, String subCategory) throws Exception {
        this.inventory.removeItem(name, category, subCategory);
    }

    public String displayItems() {return inventory.toString();}
}
