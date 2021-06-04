package store;

import Offer.Offer;
import exceptions.*;
import policies.DefaultDiscountPolicy;
import policies.DefaultPurchasePolicy;
import policies.DiscountPolicy;
import policies.PurchasePolicy;
import spellChecker.Spelling;
import user.Basket;
import notifications.Observable;
import review.Review;
import user.Subscriber;
import user.User;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Store {


    @Id
    private int id;
    private String name;
    private String description;
    private double rating;
    @Transient
    private DiscountPolicy discountPolicy;
    @Transient
    private PurchasePolicy purchasePolicy;
    //private String founder;
    private boolean isActive = true;
    @OneToOne(cascade = {CascadeType.ALL})
    private Inventory inventory;
    @ElementCollection
    private final Collection<String> purchases = new LinkedList<>();
    @Transient
    private Observable observable;
    @ElementCollection
    private final Map<String, Double> totalValuePerDay = new HashMap<>();
    @OneToMany
    private final Map<Integer, Offer> storeOffers = new HashMap<>();
    private final AtomicInteger offerIdCounter = new AtomicInteger();

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Collection<String> getPurchases() {
        return purchases;
    }

    public AtomicInteger getOfferIdCounter() {
        return offerIdCounter;
    }

    public Store() {
        this.observable = new Observable();
    }

    /**
     * This method opens a new store and create its inventory
     *
     * @param name        - the name of the new store
     * @param description - the price of the new store
     *                    //  * @param founder - the fonder of the new store
     * @throws WrongNameException
     */
    public Store(int id, String name, String description, PurchasePolicy purchasePolicy, DiscountPolicy discountPolicy, Observable observable) throws ItemException {
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
        // this.founder = founder;
//        this.inventory = new Inventory(tradingSystem);
        if(purchasePolicy == null)
            this.purchasePolicy = new DefaultPurchasePolicy();
        else
            this.purchasePolicy = purchasePolicy;
        this.inventory = new Inventory(id);
        if(discountPolicy == null)
            this.discountPolicy = new DefaultDiscountPolicy(this.inventory.getItems().keySet());
        else
            this.discountPolicy = discountPolicy;
        this.isActive = true;
//        this.observable = observable;
        this.observable = new Observable();
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) throws WrongRatingException {
        if (rating < 0)
            throw new WrongRatingException("rating must be a positive number");
        this.rating = rating;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * This method returns the items in the store's inventory
     */
    public Map<Item, Integer> getItems() {
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
     *
     * @param name        - the name of the new item
     * @param price       - the price of the new item
     * @param category    - the category of the new item
     * @param subCategory - the sub category of the new item
     * @param amount      the amount in the store for the new item
     * @throws ItemException
     */
    public int addItem(String name, double price, String category, String subCategory, int amount) throws ItemException {
        return this.inventory.addItem(name, price, category, subCategory, amount);
    }

//    /**
//     * This method is used to search the store's inventory for items that matches the param name.
//     * @param name - the name of the wanted item*/
//    public ConcurrentLinkedQueue<Item> searchItemByName(String name) {
//        return this.inventory.searchItemByName(name);
//    }

//    /**
//     * This method is used to search the store's inventory for items that matches the param category.
//     * @param category - the category of the wanted item */
//    public ConcurrentLinkedQueue<Item> searchItemByCategory(String category) {
//        return this.inventory.searchItemByCategory(category);
//    }
//
//    /**
//     * This method is used to search the store's inventory for items that matches the param keyword.
//     * @param keyword - the keyword of the wanted item */
//    public ConcurrentLinkedQueue<Item> searchItemByKeyWord(String keyword)  {
//        return this.inventory.searchItemByKeyWord(keyword);
//    }


    public Collection<Item> searchAndFilter(String keyWord, String itemName, String category, Double ratingItem,
                                                       Double ratingStore, Double maxPrice, Double minPrice) {
        Spelling spelling = new Spelling();
        if(keyWord != null)
            keyWord = spelling.correct(keyWord.toLowerCase());
        if(itemName != null)
            itemName = spelling.correct(itemName.toLowerCase());
        if(category != null)
            category = spelling.correct(category.toLowerCase());
        Collection<Item> search = searchItems(keyWord, itemName, category);
        return filterItems(search, ratingItem, ratingStore, maxPrice, minPrice);
    }

    //    /**
//     * This method searches the store's inventory for an item
//     * @param name - the name of the item
//     * @param category - the category of the item
//     * @param subCategory - the sub category of the item
//     * @exception  ItemNotFound  */
    public Collection<Item> searchItems(String keyWord, String itemName, String category) {

        Collection<Item> result = new HashSet<>(inventory.getItems().keySet());
        boolean itemValue = itemName != null && !itemName.trim().isEmpty();
        boolean categoryValue = category != null && !category.trim().isEmpty();
        boolean keyWordValue = keyWord != null && !keyWord.trim().isEmpty();
        if(!itemValue && !categoryValue && !keyWordValue)
            return result;
        if (itemValue)
            result.retainAll(inventory.searchItemByName(itemName));
        if (categoryValue)
            result.retainAll(inventory.searchItemByCategory(category));
        if (keyWordValue)
            result.retainAll(inventory.searchItemByKeyWord(keyWord));
        return result;
    }


    public Collection<Item> filterItems(Collection<Item> items, Double ratingItem, Double ratingStore,
                                                   Double maxPrice, Double minPrice) {
        Collection<Item> result = new HashSet<>(items);
        if(ratingItem != null)
            result.retainAll(inventory.filterByRating(items, ratingItem));
        if(ratingStore != null && rating < ratingStore)
            result = new HashSet<>();
        if(minPrice != null && maxPrice != null)
            result.retainAll(inventory.filterByPrice(items, minPrice, maxPrice));
        return result;

    }

    /**
     * This method searches the inventory by name, category and sub-Category
     *
     * @param name        - name of the wanted item
     * @param category    - the category of the wanted item
     * @param subCategory - the sub category of the wanted item
     * @throws ItemNotFoundException - when there are no item that matches the giving parameters.
     */
    public Item getItem(String name, String category, String subCategory) throws ItemException {
        return inventory.getItem(name, category, subCategory);
    }

    /**
     * This method searches the store's inventory by name, category and sub-Category
     *
     * @param itemId- id of the wanted item
     * @throws ItemNotFoundException - when there are no item that matches the giving parameters.
     */
    public Item searchItemById(int itemId) throws ItemException {
        return this.inventory.searchItem(itemId);
    }

//    /**
//     * This method is used to filter the store's inventory for items that their price is between start price and end price.
//     * @param startPrice - the startPrice of the items price
//     * @param endPrice - the endPrice of the items price
//     * @exception ItemNotFoundException - On non existing item with params startPrice and endPrice*/
//    public ConcurrentLinkedQueue<Item> filterByPrice(double startPrice, double endPrice) throws ItemException {
//        return this.inventory.filterByPrice(startPrice, endPrice);
//    }

    //    /**
//     * This method is used to filter the store's inventory for items that their price is between start price and end price.
//     *
//     *  @param startPrice - the startPrice of the items price
//     * @param endPrice - the endPrice of the items price */
    public Collection<Item> filterByPrice(Collection<Item> items, double startPrice, double endPrice) {
        if (items != null)
            return this.inventory.filterByPrice(items, startPrice, endPrice);
        return inventory.filterByPrice(startPrice, endPrice);
    }

    //    /**
//     * This method is used to filter the store's inventory for items that their ratings are equal or above the giving rating.
//     * @param rating - the keyword of the wanted item
//     * @exception ItemNotFoundException - On non existing item with param rating or greater*/
    public Collection<Item> filterByRating(Collection<Item> items, double rating) {
        if (items != null)
            return inventory.filterByRating(items, rating);
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
     *
     * @param itemId - id of the item in the inventory
     * @param amount - the amount of the item to check
     * @throws WrongAmountException when the amount is illegal
     */
    public boolean checkAmount(int itemId, int amount) throws ItemException {
        return inventory.checkAmount(itemId, amount);
    }

    //    /**
//     * This method decreases the amount of the item in the store's inventory by param quantity.
//     * @param name - name of the wanted item
//     * @param category - category of the wanted item
//     * @param subCategory - the sub category of the wanted item
//     * @param quantity - the quantity of the wanted item
//     * @exception WrongAmountException - when the amount is illegal */
//    public void decreaseByQuantity(int itemId, int quantity) throws ItemException {
//        this.inventory.decreaseByQuantity(itemId, quantity);
//    }

    /**
     * This method removes an item from the store's inventory
     *
     * @param itemID- id of the item
     * @throws ItemNotFoundException - when the wanted item does not exist in the inventory
     */
    public Item removeItem(int itemID) throws ItemException {
        return this.inventory.removeItem(itemID);
    }

    /**
     * This method displays the items in the store's inventory
     * * @param name - name of the wanted item
     */
    public String toString() {
        return inventory.toString();
    }

    public PurchasePolicy getPurchasePolicy() {
        return purchasePolicy;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public void setDiscountPolicy(DiscountPolicy discountPolicy) { this.discountPolicy = discountPolicy; }

    public void setPurchasePolicy(PurchasePolicy purchasePolicy) { this.purchasePolicy = purchasePolicy; }


    public void changeItem(int itemID, String newSubCategory, Integer newQuantity, Double newPrice) throws ItemException {
        inventory.changeItemDetails(itemID, newSubCategory, newQuantity, newPrice);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setNotActive(){
        if(isActive == false)
            return;
        this.isActive = false;
        observable.notifyStoreStatus(String.valueOf(id), isActive);
    }

    public void setActive(){
        if(isActive == true)
            return;
        this.isActive = true;
        observable.notifyStoreStatus(String.valueOf(id), isActive);
    }

    //TODO remember to deal with policies and types in a furure version
    public double processBasketAndCalculatePrice(Basket basket, StringBuilder details, DiscountPolicy storeDiscountPolicy, Collection<Offer> userOffers) throws ItemException, PolicyException { // TODO should get basket
        if(userOffers != null) {
            for (Offer offer: userOffers) {
                if(offer.getQuantity() == 0)
                    basket.removeItem(offer.getItem());
                if (!basket.getItems().containsKey(offer.getItem())) {
                    userOffers.remove(offer);
                    storeOffers.values().remove(offer);
                }
            }
        }
        return inventory.calculate(basket, details, storeDiscountPolicy, userOffers);
    }

    //TODO make an exception for this
    public void rollBack(Map<Item, Integer> items) {
        for (Map.Entry<Item, Integer> entry: items.entrySet()) {
            inventory.getItems().replace(entry.getKey(), inventory.getItems().get(entry.getKey()) + entry.getValue());
        }
        unlockItems(items.keySet());
    }

    public void unlockItems(Set<Item> items) {
        for (Item item: items) {
            item.unlock();
        }
    }

    public Observable getObservable() {
        return observable;
    }

    public void addPurchase(String purchaseDetails) {
        purchases.add(purchaseDetails);
    }

    public void addTotalValuePerDay(String date, double value) {
        totalValuePerDay.put(date, value);
    }

    public void updateTotalValuePerDay(String date, double value) {
        totalValuePerDay.replace(date, value);
    }

    public Map<String, Double> getTotalValuePerDay() { return this.totalValuePerDay; }

    public Collection<String> getPurchaseHistory() { return purchases; }

    public void notifyPurchase( User buyer, Map<Item, Integer> basket) {
        observable.notifyPurchase(this, buyer, basket);
    }

    public void notifyNewOffer(Offer offer) { observable.notifyNewOffer(offer); }

    public void notifyApprovedOffer(Offer offer) { observable.notifyApprovedOffer(offer); }

    public void notifyDeclinedOffer(Offer offer) { observable.notifyDeclinedOffer(offer); }

    public void notifyCounterOffer(Offer offer) { observable.notifyCounterOffer(offer); }

    public void subscribe(Subscriber subscriber) {
        observable.subscribe(subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        observable.unsubscribe(subscriber);
    }

    public void removeOwnerOrManager(Subscriber remover, Subscriber toRemove){
        observable.notifyRoleRemove(remover, toRemove, this.id);
    }

    public void notifyItemOpinion(Subscriber subscriber, Review review) {
        observable.notifyItemReview(subscriber, review);
    }

    public void setObservable(Observable observable) { this.observable = observable; }

    public void addOffer(Subscriber subscriber, Item item, int quantity, double price) {
        this.storeOffers.put(offerIdCounter.getAndIncrement(), new Offer(subscriber, item, quantity, price));
    }

    public Collection<String> getOffers() {
        Collection<String> offers = new LinkedList<>();
        for (Map.Entry<Integer, Offer> offer: storeOffers.entrySet()) {
            offers.add("offer id: " + offer.getKey() + offer.getValue().toString());
        }
        return offers;
    }

    public Offer getOfferById(int offerId) throws OfferNotExistsException {
        if(!storeOffers.containsKey(offerId))
            throw new OfferNotExistsException();
        return storeOffers.get(offerId);
    }

    public Map<Integer, Offer> getStoreOffers() { return storeOffers; }

    //for appointing store owner or manager
    public void appointRole(Subscriber subscriber, Subscriber target, String role) {
        observable.notifyRoleAppointment(subscriber, target, this.id, role);
    }

    public Offer searchOfferByItemAndSubscriber(Subscriber subscriber, Item item) {
        for (Offer offer: this.storeOffers.values()) {
            if(offer.getSubscriber() == subscriber && offer.getItem() == item)
                return offer;
        }
        return null;
    }
}
