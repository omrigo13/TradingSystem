package main.java.store;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Inventory {

    private ConcurrentHashMap<Item, Integer> items;
    private AtomicInteger id = new AtomicInteger(1);

    public Inventory() {
        this.items = new ConcurrentHashMap<>();
    }

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

        items.putIfAbsent(new Item(id.get(), name, price, category, subCategory, rating), amount);
        id.getAndIncrement();
    }

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

        items.putIfAbsent(new Item(id.get(), name, price, category, subCategory, 0), amount);
        id.getAndIncrement();
    }

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

    public Item searchItem(String name, String category, String subCategory) throws Exception
    {
        for (Item item: items.keySet())
            if(item.getName().equals(name) && item.getCategory().equals(category) && item.getSubCategory().equals(subCategory))
                return item;
        throw new ItemNotFound("item not found");
    }

    public void changeQuantity(String name, String category, String subCategory, int amount) throws Exception {
        if(amount < 0)
            throw new WrongAmount("item amount should be 0 or more than that");

        items.replace(searchItem(name, category, subCategory), amount);
    }

    public void decreaseByOne(String name, String category, String subCategory) throws Exception {
        Item item = searchItem(name, category, subCategory);
        if(items.get(item) == 0)
            throw new WrongAmount("cannot decrease by one an item with amount of 0");
        items.replace(item, items.get(item) - 1);
    }

    public void removeItem(String name, String category, String subCategory) throws Exception {
        items.remove(searchItem(name, category, subCategory));
    }

    public ConcurrentHashMap<Item, Integer> getItems() {
        return items;
    }
}
