package store;

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
     * @exception WrongNameException  */
    public Store(int id, String name, String description, String founder) throws ItemException{
        if (name == null || name.isEmpty() || name.trim().isEmpty())
            throw new WrongNameException("store name is null or contains only white spaces");
        if (name.charAt(0) >= '0' && name.charAt(0) <= '9')
            throw new WrongNameException("store name cannot start with a number");
        if (description == null || description.isEmpty() || description.trim().isEmpty())
            throw new WrongNameException("store description is null or contains only white spaces");
        if (description.charAt(0) >= '0' && description.charAt(0) <= '9')
            throw new WrongNameException("store description cannot start with a number");
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


//    /**
//     * This method changes an item's price in the store
//     * @param name - the name of the item
//     * @param price - the price of the item
//     * @param category - the category of the item
//     * @param subCategory - the sub category of the item
//     * @param price- the new price of the item
//     * @exception  ItemNotFound,WrongPrice  */
//    public void setItemPrice(String name, String category, String subCategory, double price) throws Exception {
//        this.inventory.setItemPrice(name, category, subCategory, price);
//    }

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
     * @exception WrongNameException,WrongPriceException,WrongAmountException,WrongCategoryException,ItemAlreadyExistsException  */
    public int addItem(String name, double price, String category, String subCategory, int amount) throws ItemException {
        return this.inventory.addItem(name, price, category, subCategory, amount);
    }

    /**
     * This method is used to search the store's inventory for items that matches the param name.
     * @param name - the name of the wanted item*/
    public ConcurrentLinkedQueue<Item> searchItemByName(String name) {
        return this.inventory.searchItemByName(name);
    }

    /**
     * This method is used to search the store's inventory for items that matches the param category.
     * @param category - the category of the wanted item */
    public ConcurrentLinkedQueue<Item> searchItemByCategory(String category) {
        return this.inventory.searchItemByCategory(category);
    }

    /**
     * This method is used to search the store's inventory for items that matches the param keyword.
     * @param keyword - the keyword of the wanted item */
    public ConcurrentLinkedQueue<Item> searchItemByKeyWord(String keyword)  {
        return this.inventory.searchItemByKeyWord(keyword);
    }


    //    /**
//     * This method searches the store's inventory for an item
//     * @param name - the name of the item
//     * @param category - the category of the item
//     * @param subCategory - the sub category of the item
//     * @exception  ItemNotFound  */
    public ConcurrentLinkedQueue<Item> getItem(String keyWord, String itemName, String category) {
        ConcurrentLinkedQueue list1=inventory.searchItemByName(itemName);
        ConcurrentLinkedQueue list2=inventory.searchItemByCategory(category);
        ConcurrentLinkedQueue list3=inventory.searchItemByKeyWord(keyWord);
        list1.retainAll(list2);
        list1.retainAll(list3);

        return list1;
    }

    /**
     * This method searches the store's inventory by name, category and sub-Category
     * @param name - name of the wanted item
     * @param category - the category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @exception ItemNotFoundException - when there are no item that matches the giving parameters.*/
    public Item searchItem(String name, String category, String subCategory) throws ItemException {
        return this.inventory.searchItem(name, category, subCategory);
    }

    /**
     * This method is used to filter the store's inventory for items that their price is between start price and end price.
     * @param startPrice - the startPrice of the items price
     * @param endPrice - the endPrice of the items price
     * @exception ItemNotFoundException - On non existing item with params startPrice and endPrice*/
    public ConcurrentLinkedQueue<Item> filterByPrice(double startPrice, double endPrice) throws ItemException {
        return this.inventory.filterByPrice(startPrice, endPrice);
    }

    /**
     * This method is used to filter the store's inventory for items that their ratings are equal or above the giving rating.
     * @param rating - the keyword of the wanted item
     * @exception ItemNotFoundException - On non existing item with param rating or greater*/
    public ConcurrentLinkedQueue<Item> filterByRating(double rating) throws ItemException {
        return this.inventory.filterByRating(rating);
    }

//    /**
//     * This method changes the amount of an item in the store's inventory
//     * @param name - name of the wanted item
//     * @param category - category of the wanted item
//     * @param subCategory - the sub category of the wanted item
//     * @param amount - the new amount fo the item
//     * @exception WrongAmount when the amount is illegal*/
//    public void changeQuantity(String name, String category, String subCategory, int amount) throws ItemException {
//        this.inventory.changeQuantity(name, category, subCategory, amount);
//    }

    /**
     * This method checks if there is enough amount of an item in the inventory
     * @param item - a specific item in the inventory
     * @param amount - the amount of the item to check
     * @exception WrongAmountException when the amount is illegal*/
    public boolean checkAmount(Item item, int amount) throws ItemException {
        return inventory.checkAmount(item,amount);
    }

    /**
     * This method decreases the amount of the item in the store's inventory by param quantity.
     * @param name - name of the wanted item
     * @param category - category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @param quantity - the quantity of the wanted item
     * @exception WrongAmountException - when the amount is illegal */
    public void decreaseByQuantity(String name, String category, String subCategory,int quantity ) throws ItemException {
        this.inventory.decreaseByQuantity(name, category, subCategory,quantity);
    }

    /**
     *  This method removes an item from the store's inventory
     * @param itemID- id of the item
     * @exception ItemNotFoundException - when the wanted item does not exist in the inventory */
    public void removeItem(int itemID) throws ItemException {
        this.inventory.removeItem(itemID);
    }

    /**
     *  This method displays the items in the store's inventory
     *  * @param name - name of the wanted item */
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


    public void changeItem(int itemID, String newSubCategory, Integer newQuantity, Double newPrice) throws ItemException {
        inventory.changeItemDetails(itemID, newSubCategory, newQuantity, newPrice);
    }
}
