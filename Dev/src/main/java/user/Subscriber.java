package user;

import Offer.Offer;
import exceptions.*;
import notifications.*;
import notifications.Observer;
import org.hibernate.annotations.Cascade;
import org.jetbrains.annotations.NotNull;
import persistence.Repo;
import review.Review;
import store.Item;
import store.Store;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
@Entity
public class Subscriber extends User {
    private int id;
    @Id
    private String userName;
    @Transient
    private Set<Permission> permissions; // synchronized manually
    @Transient
    private ConcurrentMap<Store, Collection<Item>> itemsPurchased;
    @ElementCollection
    private Collection<String> purchaseHistory; // synchronized in constructor
    @ElementCollection
    private Map<Notification, Boolean> notifications = new HashMap<>();
    private boolean isLoggedIn = false;
    @Transient
    private Observer observer;
    @Transient
    private Observer adminObserver;

    @Override
    @NotNull
    protected Basket createBasket(Store store) {
        Basket basket = new Basket(this, store, new ConcurrentHashMap<>());

        EntityManager em = Repo.getEm();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.merge(basket);
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


        return basket;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public ConcurrentMap<Store, Collection<Item>> getItemsPurchased() {
        return itemsPurchased;
    }

    public void setItemsPurchased(ConcurrentMap<Store, Collection<Item>> itemsPurchased) {
        this.itemsPurchased = itemsPurchased;
    }

    public void setPurchaseHistory(Collection<String> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public void setNotifications(Map<Notification, Boolean> notifications) {
        this.notifications = notifications;
    }

    public Observer getObserver() {
        return observer;
    }

    public Observer getAdminObserver() {
        return adminObserver;
    }

    public Subscriber(int id, String userName) {
        this(id, userName, new HashSet<>(), new ConcurrentHashMap<>(), new LinkedList<>());
    }

    Subscriber(int id, String userName, Set<Permission> permissions, ConcurrentMap<Store, Collection<Item>> itemsPurchased, Collection<String> purchaseHistory) {
        this.id = id;
        this.userName = userName;
        this.permissions = permissions;
        this.itemsPurchased = itemsPurchased;
        this.purchaseHistory = Collections.synchronizedCollection(purchaseHistory);
    }

    public Subscriber() {

    }

    public String getUserName() {
        return userName;
    }

    @Override
    public Subscriber getSubscriber() {
        return this;
    }

    @Override
    public void addCartToPurchases(Map<Store, String> details) { // TODO unit test

        for (Map.Entry<Store, Basket> entry : baskets.entrySet()) {
            Store store = entry.getKey();
            Basket basket = entry.getValue();
            Collection<Item> itemsPurchasedFromStore = itemsPurchased.computeIfAbsent(store, k -> new HashSet<>());
            itemsPurchasedFromStore.addAll(basket.getItems().keySet());

            Collection<Offer> userOffers = this.getOffers(store);
            for (Map.Entry<Integer, Offer> offer: store.getStoreOffers().entrySet()) {
                if(userOffers.contains(offer.getValue()) && offer.getValue().isApproved()) {
                    itemsPurchasedFromStore = itemsPurchased.computeIfAbsent(store, k -> new HashSet<>());
                    itemsPurchasedFromStore.add(offer.getValue().getItem());
                    store.getStoreOffers().remove(offer.getKey());
                }
            }
        }

        // add each store purchase details string to the user's purchase history collection
        String cartPurchase = "";
        for (Map.Entry<Store, String> entry : details.entrySet())
            //purchaseHistory.add("Store: " + entry.getKey().getName() + "\n" + entry.getValue());
            cartPurchase += "Store: " + entry.getKey().getName() + "\n" + entry.getValue();
        purchaseHistory.add(cartPurchase);

    }

    @Override
    public Collection<Offer> getOffers(Store store) {
        Collection<Offer> userOffers = new LinkedList<>();
        for (Offer offer : store.getStoreOffers().values())
            if (this.getSubscriber().equals(offer.getSubscriber()))
                userOffers.add(offer);
        return userOffers;
    }

    public void addPermission(Permission permission) {

        synchronized (permissions) {
            permissions.add(permission);
        }
    }

    public void removePermission(Permission permission) {

        synchronized (permissions) {
            permissions.remove(permission);
        }
    }

    public boolean havePermission(Permission permission) {

        synchronized (permissions) {
            return permissions.contains(permission);
        }
    }

    public void validatePermission(Permission permission) throws NoPermissionException {

        synchronized (permissions) {
            if (!havePermission(permission))
                throw new NoPermissionException(permission.toString());
        }
    }

    public void validateAtLeastOnePermission(Permission... permissions) throws NoPermissionException {

        synchronized (this.permissions) {
            for (Permission permission : permissions) {
                if (havePermission(permission))
                    return;
            }
            throw new NoPermissionException(Arrays.toString(permissions));
        }
    }

    public void addManagerPermission(Subscriber target, Store store) throws NoPermissionException, AlreadyManagerException {

        synchronized (target.id < id ? target.permissions : permissions) {
            synchronized (target.id < id ? permissions : target.permissions) {

                // check this user has the permission to perform this action
                validatePermission(OwnerPermission.getInstance(store));

                // check if the target is already a manager at this store
                Permission managerPermission = ManagerPermission.getInstance(store);
                if (target.havePermission(managerPermission))
                    throw new AlreadyManagerException(userName);

                // add manager permission to the target
                target.addPermission(managerPermission);

                // give the user permission to delete the new permission that was added to the target
                addPermission(AppointerPermission.getInstance(target, store));
            }
        }
    }

    public void removeManagerPermission(Subscriber target, Store store) throws NoPermissionException {

        removeOwnerPermission(target, store); // removes all store permissions
    }

    public void addOwnerPermission(Store store) {

        synchronized (permissions) {

            addPermission(OwnerPermission.getInstance(store));
            addPermission(ManagerPermission.getInstance(store));
            addPermission(EditPolicyPermission.getInstance(store));
            addPermission(ManageInventoryPermission.getInstance(store));
            addPermission(GetHistoryPermission.getInstance(store));
        }
    }

    public void addOwnerPermission(Subscriber target, Store store) throws NoPermissionException, AlreadyOwnerException {

        synchronized (target.id < id ? target.permissions : permissions) {
            synchronized (target.id < id ? permissions : target.permissions) {

                // check this user has the permission to perform this action
                Permission ownerPermission = OwnerPermission.getInstance(store);
                validatePermission(ownerPermission);

                // check if the target is already an owner at this store
                if (target.havePermission(ownerPermission))
                    throw new AlreadyOwnerException(userName);

                // check if the target is a manager that was appointed by someone else
                ManagerPermission managerPermission = ManagerPermission.getInstance(store);
                if (target.havePermission(managerPermission))
                    validatePermission(AppointerPermission.getInstance(target, store));

                target.addOwnerPermission(store);

                // give the user permission to delete the new permission that was added to the target
                addPermission(AppointerPermission.getInstance(target, store));
            }
        }
    }

    public void removeOwnerPermission(Store store) {

        synchronized (permissions) {

            Collection<Permission> permissionsToRemove = new LinkedList<>();

            // look for any managers or owners that were appointed by this owner for this store and remove their permission
            for (Permission permission : permissions)
                if (permission.getClass() == AppointerPermission.class && ((AppointerPermission)permission).getStore() == store) {
                    Subscriber target = ((AppointerPermission)permission).getTarget();
                    target.removeOwnerPermission(store);

                    permissionsToRemove.add(permission); // store this permission to remove it after the foreach loop
                }

            permissionsToRemove.forEach(permissions::remove);

            removePermission(OwnerPermission.getInstance(store));
            removePermission(EditPolicyPermission.getInstance(store));
            removePermission(ManageInventoryPermission.getInstance(store));
            removePermission(GetHistoryPermission.getInstance(store));
            removePermission(ManagerPermission.getInstance(store));
        }
    }

    public void removeOwnerPermission(Subscriber target, Store store) throws NoPermissionException {

        synchronized (target.id < id ? target.permissions : permissions) {
            synchronized (target.id < id ? permissions : target.permissions) {

                // check this user has the permission to perform this action
                validatePermission(AppointerPermission.getInstance(target, store));

                target.removeOwnerPermission(store);

                // remove this user's permission to change the target's permissions
                removePermission(AppointerPermission.getInstance(target, store));
            }
        }
    }

    public void addInventoryManagementPermission(Subscriber target, Store store) throws NoPermissionException, TargetIsNotManagerException {

        addPermissionToManager(target, store, ManageInventoryPermission.getInstance(store));
    }

    public void removeInventoryManagementPermission(Subscriber target, Store store) throws NoPermissionException, TargetIsOwnerException {

        removePermissionFromManager(target, store, ManageInventoryPermission.getInstance(store));
    }

    public void addEditPolicyPermission(Subscriber target, Store store) throws NoPermissionException, TargetIsNotManagerException {

        addPermissionToManager(target, store, EditPolicyPermission.getInstance(store));
    }

    public void removeEditPolicyPermission(Subscriber target, Store store) throws NoPermissionException, TargetIsOwnerException {

        removePermissionFromManager(target, store, EditPolicyPermission.getInstance(store));
    }

    public void addGetHistoryPermission(Subscriber target, Store store) throws NoPermissionException, TargetIsNotManagerException {

        addPermissionToManager(target, store, GetHistoryPermission.getInstance(store));
    }

    public void removeGetHistoryPermission(Subscriber target, Store store) throws NoPermissionException, TargetIsOwnerException {

        removePermissionFromManager(target, store, GetHistoryPermission.getInstance(store));
    }

    void addPermissionToManager(Subscriber target, Store store, Permission permission) throws NoPermissionException, TargetIsNotManagerException {

        synchronized (target.id < id ? target.permissions : permissions) {
            synchronized (target.id < id ? permissions : target.permissions) {

                // check this user has the permission to perform this action
                validatePermission(AppointerPermission.getInstance(target, store));

                if (!target.havePermission(ManagerPermission.getInstance(store)))
                    throw new TargetIsNotManagerException(target.getUserName(), store.getName());

                // add the permission to the target (if he doesn't already have it)
                target.addPermission(permission);
            }
        }
    }

    void removePermissionFromManager(Subscriber target, Store store, Permission permission) throws NoPermissionException, TargetIsOwnerException {

        synchronized (target.id < id ? target.permissions : permissions) {
            synchronized (target.id < id ? permissions : target.permissions) {

                // check this user has the permission to perform this action
                validatePermission(AppointerPermission.getInstance(target, store));

                if (target.havePermission(OwnerPermission.getInstance(store)))
                    throw new TargetIsOwnerException(target.getUserName(), store.getName());

                target.removePermission(permission);
            }
        }
    }

    public int addStoreItem(Store store, String itemName, String category, String subCategory, int quantity, double price)
            throws NoPermissionException, ItemException {

        // check this user has the permission to perform this action
        validatePermission(ManageInventoryPermission.getInstance(store));

        return store.addItem(itemName, price, category, subCategory, quantity);
    }

    public void removeStoreItem(Store store, int itemId) throws NoPermissionException, ItemException {

        // check this user has the permission to perform this action
        validatePermission(ManageInventoryPermission.getInstance(store));

        store.removeItem(itemId);
    }

    public void updateStoreItem(Store store, int itemId, String newSubCategory, Integer newQuantity, Double newPrice)
            throws NoPermissionException, ItemException {

        // check this user has the permission to perform this action
        validatePermission(ManageInventoryPermission.getInstance(store));

        store.changeItem(itemId,  newSubCategory, newQuantity, newPrice);
    }

    public Collection<Store> getAllStores(Collection<Store> stores) throws NoPermissionException {

        // check this user has the permission to perform this action
        validatePermission(AdminPermission.getInstance());

        return stores;
    }

    public Map<Integer, Item> getStoreItems(Store store) throws NoPermissionException {

        // check this user has the permission to perform this action
        validateAtLeastOnePermission(AdminPermission.getInstance(), ManagerPermission.getInstance(store));

        return store.getItems();
    }

    public String storePermissionsToString(Store store) {

        synchronized (permissions) {

            StringBuilder result = new StringBuilder();

            Permission ownerPermission = OwnerPermission.getInstance(store);
            Permission managerPermission = ManagerPermission.getInstance(store);
            Permission manageInventoryPermission = ManageInventoryPermission.getInstance(store);
            Permission getHistoryPermission = GetHistoryPermission.getInstance(store);
            Permission editPolicyPermission = EditPolicyPermission.getInstance(store);

            if (havePermission(ownerPermission))
                result.append(ownerPermission.toString()).append(" ");
            if (havePermission(managerPermission))
                result.append(managerPermission.toString()).append(" ");
            if (havePermission(manageInventoryPermission))
                result.append(manageInventoryPermission.toString()).append(" ");
            if (havePermission(getHistoryPermission))
                result.append(getHistoryPermission.toString()).append(" ");
            if (havePermission(editPolicyPermission))
                result.append(editPolicyPermission.toString()).append(" ");

            return result.toString();
        }
    }

    public Collection<String> getEventLog(Collection<String> log) throws NoPermissionException {

        // check this user has the permission to perform this action
        validatePermission(AdminPermission.getInstance());

        return log;
    }

    public Collection<String> getSalesHistoryByStore(Store store) throws NoPermissionException {

        validateAtLeastOnePermission(AdminPermission.getInstance(), GetHistoryPermission.getInstance(store));

        return store.getPurchaseHistory();
    }

    public Collection<String> getOffersByStore(Store store) throws NoPermissionException {

        validateAtLeastOnePermission(AdminPermission.getInstance(), ManageInventoryPermission.getInstance(store));

        return store.getOffers();
    }

    public void approveOffer(Store store, int offerId, double price, int storeOwners) throws NoPermissionException, OfferNotExistsException {

        validateAtLeastOnePermission(AdminPermission.getInstance(), ManageInventoryPermission.getInstance(store));

        Offer offer = store.getOfferById(offerId);
        if(price < 0) {
            store.getStoreOffers().remove(offerId);
            store.notifyDeclinedOffer(offer);
            Repo.merge(offer);

            return;
        }
        if(price != 0) {
            offer.setPrice(price);
            if(this.havePermission(OwnerPermission.getInstance(store))){
                offer.addCounteredOwner(this);
            }
        }
        else if (this.havePermission(OwnerPermission.getInstance(store)))
            offer.addApprovedOwner(this);
        if(offer.getApprovedOwners() == storeOwners) {
            offer.approve();
            store.notifyApprovedOffer(offer);
        }
        if(offer.getApprovedOwners() == storeOwners - 1 && offer.getCounteredOwners() == 1) {
            offer.approve();
            store.notifyCounterOffer(offer);
        }
        if(offer.isApproved())
            offer.getSubscriber().getBasket(store).getItems().compute(offer.getItem(), (k, v) -> offer.getQuantity());
//        Repo.merge(store);
        Repo.merge(offer);
    }

    public Collection<String> getPurchaseHistory() {

        return new ArrayList<>(purchaseHistory);
    }

    public void writeOpinionOnProduct(Store store, int itemId, String review) throws ItemException, WrongReviewException {

//        if (review == null || review.trim().isEmpty())
//            throw new WrongReviewException("Review can't be empty or null");

        Item item = store.searchItemById(itemId);
//        if (!itemsPurchased.get(store).contains(item))
//            throw new ItemNotPurchasedException("Item ID: " + itemId + " item name: " + item.getName());

        Review review1 = new Review(store, item, review);
        item.addReview(review1);
        store.notifyItemOpinion(this, review1);

        EntityManager em = Repo.getEm();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.merge(review1);
            em.merge(item);
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

    public void writeOpinionOnProduct2(Store store, int itemId, String review) throws ItemException, WrongReviewException {
//        if (review == null || review.trim().isEmpty())
//            throw new WrongReviewException("Review can't be empty or null");

        Item item = store.searchItemById(itemId);
//        if (!itemsPurchased.get(store).contains(item))
//            throw new ItemNotPurchasedException("Item ID: " + itemId + " item name: " + item.getName());

        Review review1 = new Review(store, item, review);
        item.addReview(review1);

        EntityManager em = Repo.getEm();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
//            em.merge(review1);
//            em.merge(item);
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
        store.notifyItemOpinion(this, review1);



    }


        public void subscribe(Store store){
        store.subscribe(this);
    }

    public void unsubscribe(Store store){
        store.unsubscribe(this);

    }

    //todo: should we return notifications? hot to connect it to the GUI?
//    public PurchaseNotification notifyObserverPurchase(PurchaseNotification notification) {
//        //todo: decide if to postpone the notification
//        return notification;
//    }
//
//    public StoreStatusNotification notifyObserverStoreStatus(StoreStatusNotification notification) {
//        //todo: decide if to postpone the notification
//        return notification;
//    }
//
//    public ItemReviewNotification notifyObserverItemReview(ItemReviewNotification notification) {
//        //todo: decide if to postpone the notification
//        return notification;
//    }
//
//    public void notifyObserverLotteryStatus() {
//        //todo: implement
//
//    }
//
//    public MessageNotification notifyObserverMessage(MessageNotification notification){
//        //todo: implement
//        return notification;
//    }
//
//    public SubscriberRemoveNotification notifyObserverSubscriberRemove(SubscriberRemoveNotification notification){
//        //todo: implement
//        return notification;
//    }

    public Notification notifyNotification(Notification notification){
        if(observer != null) {
            observer.notify(notification);
            this.notifications.put(notification,true);
        }
        else
            this.notifications.put(notification,false);

        Repo.persist(notification);
        Repo.merge(this);

        return notification;
    }

    public void notifyVisitors(Notification notification) {
        if(adminObserver != null) {
            adminObserver.notify(notification);
        }
    }

    public Collection<Notification> checkPendingNotifications() {
        Collection<Notification> collection = new LinkedList<>();
        for (Notification n: this.notifications.keySet()) {
            if(this.notifications.get(n) == false){
                collection.add(n);
                this.notifications.put(n,true);
            }
        }
        return collection;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public Map<Notification, Boolean> getNotifications() {
        return notifications;
    }

    public String getTotalIncomeByStorePerDay(Store store, String date) throws NoPermissionException {

        validateAtLeastOnePermission(AdminPermission.getInstance(), OwnerPermission.getInstance(store));

        double totalValue = store.getTotalValuePerDay().get(date);
        return "store: " + store.getName() + " date: " + date + " total value is: " + totalValue;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public void setAdminObserver(Observer observer) {
        this.adminObserver = observer;
    }

}
