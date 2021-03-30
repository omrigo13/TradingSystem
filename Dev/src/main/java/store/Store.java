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

    /**
     * This method opens a new store and create its inventory
     * @param name - the name of the new store
     * @param description - the price of the new store
     * @param founder - the fonder of the new store
     * @exception  WrongName  */
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

    /**
     * This method returns the items in the store's inventory*/
    public ConcurrentHashMap<Item, Integer> getItems() {
        return this.inventory.getItems();
    }

    /**
     * This methode searches the store's inventory for an item
     * @param name - the name of the item
     * @param category - the category of the item
     * @param subCategory - the sub category of the item
     * @exception  ItemNotFound  */
    public Item getItem(String name, String category, String subCategory) throws Exception {
        return this.inventory.searchItem(name, category, subCategory);
    }

    /**
     * This method changes an item's price in the store
     * @param name - the name of the item
     * @param price - the price of the item
     * @param category - the category of the item
     * @param subCategory - the sub category of the item
     * @param price- the new price of the item
     * @exception  WrongName,WrongPrice,WrongAmount,WrongCategory,ItemAlreadyExists  */
    public void setItemPrice(String name, String category, String subCategory, double price) throws Exception {
        this.inventory.setItemPrice(name, category, subCategory, price);
    }

//    public void addItem(String name, double price, String category, String subCategory, double rating, int amount) throws Exception {
//        this.inventory.addItem(name, price, category, subCategory, rating, amount);
//    }

    /**
     * this adds a new item and it's amount to the store's inventory
     * @param name - the name of the new item
     * @param price - the price of the new item
     * @param category - the category of the new item
     * @param subCategory - the sub category of the new item
     * @param amount the amount in the store for the new item
     * @exception  WrongName,WrongPrice,WrongAmount,WrongCategory,ItemAlreadyExists  */
    public void addItem(String name, double price, String category, String subCategory, int amount) throws Exception {
        this.inventory.addItem(name, price, category, subCategory, amount);
    }

    /**
     * This method is used to search the store's inventory for items that matches the param name.
     * @param name - the name of the wanted item
     * @exception  ItemNotFound- On non existing item with param name*/
    public ConcurrentLinkedQueue<Item> searchItemByName(String name) throws Exception {
        return this.inventory.searchItemByName(name);
    }

    /**
     * This method is used to search the store's inventory for items that matches the param category.
     * @param category - the category of the wanted item
     * @exception  ItemNotFound- On non existing item with param category*/
    public ConcurrentLinkedQueue<Item> searchItemByCategory(String category) throws Exception {
        return this.inventory.searchItemByCategory(category);
    }

    /**
     * This method is used to search the store's inventory for items that matches the param keyword.
     * @param keyword - the keyword of the wanted item
     * @exception  ItemNotFound- On non existing item with param keyword*/
    public ConcurrentLinkedQueue<Item> searchItemByKeyWord(String keyword) throws Exception {
        return this.inventory.searchItemByKeyWord(keyword);
    }

    /**
     * This method searches the store's inventory by name, category and sub-Category
     * @param name - name of the wanted item
     * @param category - the category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @exception ItemNotFound- when there are no item that matches the giving parameters.*/
    public Item searchItem(String name, String category, String subCategory) throws Exception {
        return this.inventory.searchItem(name, category, subCategory);
    }

    
    public ConcurrentLinkedQueue<Item> filterByPrice(double startPrice, double endPrice) throws Exception {
        return this.inventory.filterByPrice(startPrice, endPrice);
    }
    public ConcurrentLinkedQueue<Item> filterByRating(double rating) throws Exception {
        return this.inventory.filterByRating(rating);
    }

    /**
     * This method changes the amount of an item in the inventory
     * @param name - name of the wanted item
     * @param category - category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @param amount - the new amount fo the item
     * @exception WrongAmount when the amount is illegal*/
    public void changeQuantity(String name, String category, String subCategory, int amount) throws Exception {
        this.inventory.changeQuantity(name, category, subCategory, amount);
    }

    /**
     * This method decreases the amount of the item by amount
     * @param name - name of the wanted item
     * @param category - category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @param quantity - the amount of the item to reduce form the store's inventory
     * @exception WrongAmount- when the amount is illegal */
    public void decreaseByOne(String name, String category, String subCategory, int quantity) throws Exception {
        this.inventory.decreaseByOne(name, category, subCategory);
    }

    /**
     *  This method removes an item
     * @param name - name of the wanted item
     * @param category - category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @exception ItemNotFound - when the wanted item does not exist in the inventory */
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
