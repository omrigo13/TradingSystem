package store;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Store {

    private int id;
    private String name;
    private String description;
    private double rating;
    private String purchaseType;     // TODO: should check how to implement
    private String discountType;     // TODO: should check how to implement
    private String purchasePolicy;     // TODO: should check how to implement
    private String discountPolicy;     // TODO: should check how to implement
    private String founder;
    private Inventory inventory;

    public Store(int id, String name, String description, String founder) throws Exception{
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
        this.rating = 0;
        this.founder = founder; // TODO: should check how to implement
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

    public double getRating() { return rating; }

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

//    public void addItem(String name, double price, String category, String subCategory, double rating, int amount) throws Exception {
//        this.inventory.addItem(name, price, category, subCategory, rating, amount);
//    }

    public void addItem(String name, double price, String category, String subCategory, int amount) throws Exception {
        this.inventory.addItem(name, price, category, subCategory, amount);
    }

    public ConcurrentLinkedQueue<Item> searchItemByName(String name) throws Exception {
        return this.inventory.searchItemByName(name);
    }

    public ConcurrentLinkedQueue<Item> searchItemByCategory(String category) throws Exception {
        return this.inventory.searchItemByCategory(category);
    }
    public ConcurrentLinkedQueue<Item> searchItemByKeyWord(String keyword) throws Exception {
        return this.inventory.searchItemByKeyWord(keyword);
    }

    public Item searchItem(String name, String category, String subCategory) throws Exception {
        return this.inventory.searchItem(name, category, subCategory);
    }

    public ConcurrentLinkedQueue<Item> filterByPrice(double startPrice, double endPrice) throws Exception {
        return this.inventory.filterByPrice(startPrice, endPrice);
    }
    public ConcurrentLinkedQueue<Item> filterByRating(double rating) throws Exception {
        return this.inventory.filterByRating(rating);
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

    // TODO: should check how to implement
    public String getPurchaseType() {
        return purchaseType;
    }

    public String getDiscountType() {
        return discountType;
    }

    public String getPurchasePolicy() {
        return purchasePolicy;
    }

    public String getDiscountPolicy() {
        return discountPolicy;
    }

    // TODO: should check how to implement
    public void setDiscountType() {}
    public void setPurchaseType() {}
    public void setDiscountPolicy() {}
    public void setPurchasePolicy() {}
}
