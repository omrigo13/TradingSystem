package store;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Inventory {

    private ConcurrentHashMap<Item, Integer> items;
    private AtomicInteger id = new AtomicInteger(1);

    public Inventory() {
        this.items = new ConcurrentHashMap<>();
    }
    /**
     * this adds a new item and it's amount to the inventory os a store
     * @param name - the name of the new item
     * @param price - the price of the new item
     * @param category - the category of the new item
     * @param subCategory - the sub category of the new item
     * @param rating - the rating of the new item
     * @param amount the amount in the store for the new item
     * @exception  WrongName,WrongPrice,WrongRating,WrongAmount,WrongCategory,ItemAlreadyExists  */
    public void addItem(String name, double price, String category, String subCategory, double rating, int amount) throws Exception {
        if(name == null || name.isEmpty() || name.trim().isEmpty())
            throw new WrongName("item name is null or contains only white spaces");
        if(name.charAt(0) >= '0' && name.charAt(0) <= '9')
            throw new WrongName("item name cannot start with a number");
        if(price < 0)
            throw new WrongPrice("item price cannot be negative");
        if(rating < 0)
            throw new WrongRating("item rating cannot be negative");
        if(amount < 0)
            throw new WrongAmount("item amount should be 0 or more than that");
        for (Item item: items.keySet())
            if(item.getName().equals(name) && item.getCategory().equals(category) && item.getSubCategory().equals(subCategory))
                throw new ItemAlreadyExists("item already exists");
        if(category.charAt(0) >= '0' && category.charAt(0) <= '9')// add check to category need to add tests
            throw new WrongCategory("item category cannot start with a number");

        items.putIfAbsent(new Item(id.get(), name, price, category, subCategory, rating), amount);
        id.getAndIncrement();
    }

    /**
     * this adds a new item and it's amount to the inventory os a store
     * @param name - the name of the new item
     * @param price - the price of the new item
     * @param category - the category of the new item
     * @param subCategory - the sub category of the new item
     * @param amount the amount in the store for the new item
     * @exception  WrongName,WrongPrice,WrongAmount,WrongCategory,ItemAlreadyExists  */
    public void addItem(String name, double price, String category, String subCategory, int amount) throws Exception {
        if(name == null || name.isEmpty() || name.trim().isEmpty())
            throw new WrongName("item name is null or contains only white spaces");
        if(name.charAt(0) >= '0' && name.charAt(0) <= '9')
            throw new WrongName("item name cannot start with a number");
        if(price < 0)
            throw new WrongPrice("item price cannot be negative");
        if(amount < 0)
            throw new WrongAmount("item amount should be 0 or more than that");
        for (Item item: items.keySet())
            if(item.getName().equals(name) && item.getCategory().equals(category) && item.getSubCategory().equals(subCategory))
                throw new ItemAlreadyExists("item already exists");
        if(category.charAt(0) >= '0' && category.charAt(0) <= '9')// add check to category need to add tests
            throw new WrongCategory("item category cannot start with a number");

        items.putIfAbsent(new Item(id.get(), name, price, category, subCategory, 0), amount);
        id.getAndIncrement();
    }

    /**
     * This method is used to search the inventory for items that matches the param name.
     * @param name - the name of the wanted item
     * @exception  ItemNotFound- On non existing item with param name*/
    public ConcurrentLinkedQueue<Item> searchItemByName(String name) throws Exception
    {
        ConcurrentLinkedQueue<Item> foundItems = new ConcurrentLinkedQueue();
        for (Item item: items.keySet())
            if(item.getName().equals(name))
                foundItems.add(item);
        if(foundItems.isEmpty())
            throw new ItemNotFound("item not found");
        return foundItems;
    }

    /**
     * This method searches the inventory by name, category and sub-Category
     * @param name - name of the wanted item
     * @param category - the category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @exception ItemNotFound- when there are no item that matches the giving parameters.*/
    public Item searchItem(String name, String category, String subCategory) throws Exception
    {
        for (Item item: items.keySet())
            if(item.getName().equals(name) && item.getCategory().equals(category) && item.getSubCategory().equals(subCategory))
                return item;
        throw new ItemNotFound("item not found");
    }
    /**
     * This method changes the amount of an item in the inventory
     * @param name - name of the wanted item
     * @param category - category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @param amount - the new amount fo the item
     * @exception WrongAmount when the amount is illegal*/
    public void changeQuantity(String name, String category, String subCategory, int amount) throws Exception {
        if(amount < 0)
            throw new WrongAmount("item amount should be 0 or more than that");

        items.replace(searchItem(name, category, subCategory), amount);
    }

    /**
     * This method decreases the amount of the item by one
     * @param name - name of the wanted item
     * @param category - category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @exception WrongAmount- when the amount is illegal */
    public void decreaseByOne(String name, String category, String subCategory) throws Exception {
        Item item = searchItem(name, category, subCategory);
        if(items.get(item) == 0)
            throw new WrongAmount("cannot decrease by one an item with amount of 0");
        items.replace(item, items.get(item) - 1);
    }

    /**
     *  This method removes an item
     * @param name - name of the wanted item
     * @param category - category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @exception ItemNotFound - when the wanted item does not exist in the inventory */
    public void removeItem(String name, String category, String subCategory) throws Exception {
        items.remove(searchItem(name, category, subCategory));
    }

    public ConcurrentHashMap<Item, Integer> getItems() {
        return items;
    }

    public void setItemPrice(String name, String category, String subCategory, double price) throws Exception {
        searchItem(name, category, subCategory).setPrice(price);
    }
}
