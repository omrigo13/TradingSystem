



package store;

import exceptions.*;
import policies.DiscountPolicy;
import user.Basket;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Inventory {

    private final Map<Item, Integer> items;
    private final AtomicInteger id = new AtomicInteger(0);

    public Inventory() {
        this.items = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * this adds a new item and it's amount to the inventory os a store
     * @param name - the name of the new item
     * @param price - the price of the new item
     * @param category - the category of the new item
     * @param subCategory - the sub category of the new item
     * @param amount the amount in the store for the new item
     * @exception ItemException  */
    public int addItem(String name, double price, String category, String subCategory, int amount) throws ItemException {
        if(name == null || name.isEmpty() || name.trim().isEmpty())
            throw new WrongNameException("item name is null or contains only white spaces");
        if(name.charAt(0) >= '0' && name.charAt(0) <= '9')
            throw new WrongNameException("item name cannot start with a number");
        if(price < 0)
            throw new WrongPriceException("item price cannot be negative");
        if(amount < 0)
            throw new WrongAmountException("item amount should be 0 or more than that");

        synchronized (items) {
            for (Item item : items.keySet())
                if (item.getName().equalsIgnoreCase(name) && item.getCategory().equalsIgnoreCase(category) && item.getSubCategory().equalsIgnoreCase(subCategory))
                    throw new ItemAlreadyExistsException("item already exists");
            items.putIfAbsent(new Item(id.get(), name, price, category, subCategory, 0), amount);
            return id.getAndIncrement();
        }
    }

    /**
     * This method is used to search the inventory for items that matches the param name.
     * @param name - the name of the wanted item
     * @exception ItemNotFoundException - On non existing item with param name
     * @return*/
    public Collection<Item> searchItemByName(String name)
    {
        Collection<Item> foundItems = new LinkedList<>();
        for (Item item: items.keySet())
            if(item.getName().equalsIgnoreCase(name))
                foundItems.add(item);
        return foundItems;
    }

    /**
     * This method is used to search the inventory for items that matches the param category.
     * @param category - the category of the wanted item
     * @exception ItemNotFoundException - On non existing item with param category*/
    public Collection<Item> searchItemByCategory(String category)
    {
        Collection<Item> foundItems = new LinkedList<>();
        for (Item item: items.keySet())
            if(item.getCategory().equalsIgnoreCase(category))
                foundItems.add(item);
        return foundItems;
    }

    /**
     * This method is used to search the inventory for items that matches the param keyword.
     * @param keyword - the keyword of the wanted item
     * @exception ItemNotFoundException - On non existing item with param keyword*/
    public Collection<Item> searchItemByKeyWord(String keyword)
    {
        Collection<Item> foundItems = new LinkedList<>();
        for (Item item: items.keySet())
            if(item.getName().toLowerCase().contains(keyword.toLowerCase()) || item.getCategory().toLowerCase().contains(keyword.toLowerCase()) ||
                    item.getSubCategory().toLowerCase().contains(keyword.toLowerCase()))
                foundItems.add(item);
        return foundItems;
    }

    /**
     * This method searches the inventory by name, category and sub-Category
     * @param name - name of the wanted item
     * @param category - the category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @exception ItemNotFoundException - when there are no item that matches the giving parameters.*/
    public Item getItem(String name, String category, String subCategory) throws ItemException
    {
        for (Item item: items.keySet())
            if(item.getName().equalsIgnoreCase(name) && item.getCategory().equalsIgnoreCase(category)
                    && item.getSubCategory().equalsIgnoreCase(subCategory))
                return item;
        throw new ItemNotFoundException("item not found");
    }

    public Item searchItem(int itemId) throws ItemException {
        for (Item item: items.keySet())
            if(item.getId()==itemId)
                return item;
        throw new ItemNotFoundException("item not found");

    }

    /**
     * This method is used to filter the inventory for items that matches the params startPrice and endPrice.
     * @param startPrice - the startPrice of the items price
     * @param endPrice - the endPrice of the items price */
    public Collection<Item> filterByPrice(double startPrice, double endPrice)
    {
        Collection<Item> foundItems = new LinkedList<>();
        for (Item item: items.keySet())
            if(item.getPrice() >= startPrice && item.getPrice() <= endPrice)
                foundItems.add(item);

        return foundItems;
    }

    /**
     * This method is used to filter the inventory for items that matches the params startPrice and endPrice.
     * @param startPrice - the startPrice of the items price
     * @param endPrice - the endPrice of the items price */
    public Collection<Item> filterByPrice(Collection<Item> itemsList, double startPrice, double endPrice)
    {
        Collection<Item> foundItems = new LinkedList<>();
        for (Item item: itemsList)
            if(item.getPrice() >= startPrice && item.getPrice() <= endPrice)
                foundItems.add(item);
        return foundItems;
    }

    /**
     * This method is used to filter the inventory for items that matches the param rating.
     * @param rating - the keyword of the wanted item */
    public Collection<Item> filterByRating(double rating)
    {
        Collection<Item> foundItems = new LinkedList<>();
        for (Item item: items.keySet())
            if(item.getRating() >= rating)
                foundItems.add(item);
        return foundItems;
    }

    /**
     * This method is used to filter the inventory for items that matches the param rating.
     * @param rating - the keyword of the wanted item
     * @exception ItemNotFoundException - On non existing item with param rating or greater*/
    public Collection<Item> filterByRating(Collection<Item> itemsList, double rating)
    {
        Collection<Item> foundItems = new LinkedList<>();
        for (Item item: itemsList)
            if(item.getRating() >= rating)
                foundItems.add(item);
        return foundItems;
    }

    /**
     * This method changes the amount of an item in the inventory
     * @param itemId- id of the wanted item
     * @param amount - the new amount fo the item
     * @exception WrongAmountException when the amount is illegal*/
    public void changeQuantity(int itemId, int amount) throws ItemException {
        if(amount < 0)
            throw new WrongAmountException("item amount should be 0 or more than that");
        items.replace(searchItem(itemId), amount);
    }

    /**
     * This method checks if there is enough amount of an item in the inventory
     * @param itemId - id of the item in the inventory
     * @param amount - the amount of the item to check
     * @exception WrongAmountException when the amount is illegal*/
    public boolean checkAmount(int itemId, int amount) throws ItemException {
        if(amount > items.get(searchItem(itemId)))
            throw new WrongAmountException("there is not enough from the item");
        if(amount<0)
            throw new WrongAmountException("amount can't be a negative number");
        return true;
    }

    /**
     *  This method removes an item
     * @param itemID - the id of the item
     * @exception ItemNotFoundException - when the wanted item does not exist in the inventory */
    public Item removeItem(int itemID) throws ItemException {
        Item item = searchItem(itemID);
        items.remove(item);
        return item;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public String toString() {
        String itemsDisplay = "";
        for (Item item: items.keySet())
            itemsDisplay += item.toString();
        return itemsDisplay;
    }

    public void changeItemDetails(int itemID, String newSubCategory, Integer newQuantity, Double newPrice) throws ItemException {
        synchronized (this.items)
        {
            for (Item item: items.keySet()) {
                if(item.getId()==itemID)
                {
                    if(newSubCategory != null && !newSubCategory.trim().isEmpty())
                        item.setSubCategory(newSubCategory);

                    if(newQuantity !=null)
                        changeQuantity(itemID,newQuantity);

                    if(newPrice != null)
                        item.setPrice(newPrice);

                    return;
                }
            }
            throw new ItemNotFoundException("no item in inventory matching item id");
        }
    }

    public double calculate(Basket basket, StringBuilder details, DiscountPolicy storeDiscountPolicy) throws ItemException, PolicyException {

        double totalValue;
        synchronized (this.items) {
            // check that every item has quantity in inventory
            for (Map.Entry<Item, Integer> entry : basket.getItems().entrySet()) {
                checkAmount(entry.getKey().getId(), entry.getValue());
            }
            // update inventory quantity and calculate basket price
            totalValue = storeDiscountPolicy.cartTotalValue(basket);
            for (Map.Entry<Item, Integer> entry : basket.getItems().entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                //noinspection ConstantConditions
                this.items.compute(item, (k, v) -> v - quantity);
                details.append("\tItem: ").append(item.getName()).append(" Price: ").append(item.getPrice())
                        .append(" Quantity: ").append(quantity).append("\n");
            }
        }
        return totalValue;
    }
}
