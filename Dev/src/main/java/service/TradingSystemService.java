package service;

import java.util.Collection;

public interface TradingSystemService {
    
    
    // ***********************************************************************
    // Topics: system, guest, subscriber
    // ***********************************************************************

    /* Initialize system and define a system manager.
    params: manager details */
    void initializeSystem(String userName, String pass) throws Exception;

    String connectGuest() throws Exception;

    /* Register to system */
    void register(String userName, String password) throws Exception;

    /* Login to system */
    void login(String userID, String userName, String pass) throws Exception;

    /* Logout from system */
    void logout(String userID) throws Exception;

    /* Get product by filter. */
    Collection<String> getItems(String keyWord, String productName, String category, String subCategory, Double ratingItem, Double ratingStore, Double maxPrice, Double minPrice) throws Exception;
    // TODO to check empty string or null
    // the String in the collection represent item.toString()
    // use spellChecking

    /* Save product in basket of a store. */
    void addItemToBasket(String userID, String storeId, String productId, int amount) throws Exception;

    /* get cart's products. */
    Collection<String> showCart(String userID) throws Exception;

    /* get basket's products. */
    String showBasket(String userID, String storeId) throws Exception;

    /* updates the amount of a product for user from a specific store. if new amount = 0 then the product will be deleted from the basket */
    void updateProductAmountInBasket(String userID, String storeId, String productId, int newAmount) throws Exception;

    /* make purchase for every product in all of the user's baskets */
    void purchaseCart(String userID) throws Exception;

    /* get purchase history of a user by permissions: user himself / system manager */
    Collection<String> getPurchaseHistory(String userID) throws Exception;
    // TODO each String is purchase.toString()
    
    /* enables user to write an opinion on a product he has purchased.
    precondition: the user has purchased the product*/
    void writeOpinionOnProduct(String userID, String storeID, String productId, String desc) throws Exception;


    // ***********************************************************************
    // Topics: store owner, store manager, system manager
    // ***********************************************************************


    /* Get info of all stores owners and managers, and the products in every store
    preconditions: invoker is a system manager. */
    Collection<String> getStoresInfo(String userID) throws Exception;

    /* Get all products of the store, with store id.
    preconditions: invoker is the owner/manager of the store or is a system manager.*/
    Collection<String> getItemsByStore(String userID, String storeId) throws Exception;

    /* creates a new store. username is the founder and owner. */
    // pre-condition: storeName is not null or empty
    String openNewStore(String userID, String newStoreName) throws Exception;

    /* appoints a new store manager. assignor is an owner of the store, assignee is the username of the new store manager
     precondition: assignee is not a manager in this store.
     poscondition: assignee have the permissions of a new store manager, i.e the basic permissions for a manager, which are:
                   get info about roles in the store and their permissions, get info about products in the store,
                   get requests from users and answer them.*/
    void appointStoreManager(String userID, String assigneeUserName, String storeId) throws Exception;

    /* adds a product to a store.
    // returns the product ID
    preconditions: invoker is the store owner or is a manager of it, with permissions to make changes in products. */
    String addProductToStore(String userID, String storeId, String productName, String category, String subCategory, int quantity, double price) throws Exception;

    /* deletes a product from a store 
    preconditions: invoker is the store owner or is a manager of it, with permissions to make changes in products. */
    void deleteProductFromStore(String userID, String storeId, String productID) throws Exception;

    /* updates a product details of a store.
    // if there is null, no need to update the field.
    preconditions: invoker is the store owner or is a manager of it, with permissions to make changes in products.*/
    void updateProductDetails(String userID, String storeId, String productID, String newSubCategory, Integer newQuantity, Double newPrice) throws Exception;

    /* appoints a new store owner. assignor is an owner of the store, assignee is the username of a new store owner
     * pre-condition: assignee is not an owner in this store */
    void appointStoreOwner(String userID, String assigneeUserName, String storeId) throws Exception;

    /*The next block of functions deals with store manager permissions. A new store manager has only the
        basic permissions in the store. */
    //******************************************************************************
    
    /* allows manager to add, delete amd update product in a specific store.
     precondition: assignor is the assignor of the manager.
     postcondition: the manager has permissions to add, delete amd update product in the store. */
    void allowManagerToUpdateProducts(String userID, String storeId, String managerUserName) throws Exception;
    
    /* disables a manager from adding, deleting amd updating product in a specific store.
     pre-condition: assignor is the assignor of the manager
     postcondition: the manager DOESN'T have permissions to add, delete amd update product in the store. */
    void disableManagerFromUpdateProducts(String userID, String storeId, String managerUserName) throws Exception ;
    
    /* allows manager to get info and edit purchase and discount policies in a specific store.
     precondition: assignor is the assignor of the manager.
     postcondition: the manager has permissions to get info and edit purchase and discount policies in the store. */
    void allowManagerToEditPolicies(String userID, String storeId, String managerUserName) throws Exception;
    
    /* disables a manager from getting info and editing purchase and discount policies in a specific store.
     pre-condition: assignor is the assignor of the manager
     postcondition: the manager DOESN'T have permissions to get info and edit purchase and discount policies in the store. */
    void disableManagerFromEditPolicies(String userID, String storeId, String managerUserName) throws Exception;
    
    /* allows manager to get purchases history of the store.
     precondition: assignor is the assignor of the manager.
     postcondition: the manager has permissions to get purchases history in the store. */
    void allowManagerToGetHistory(String userID, String storeId, String managerUserName) throws Exception;
    
    /* disables a manager from getting purchases history of the store.
     pre-condition: assignor is the assignor of the manager
     postcondition: the manager DOESN'T have permissions to get purchases history of the store. */
    void disableManagerFromGetHistory(String userID, String storeId, String managerUserName) throws Exception;
    
    //end of block dealing with store manager permissions
    //******************************************************************************

        
    /* removes a user from the store manager role.
     * pre-condition: the incoker is an owner of the store and is the assignor of the manager*/
    void removeManager(String userID, String storeId, String managerUserName) throws Exception;

    /* shows store staff information and their permissions in the store
    precondition: invoker has the permissions to get the info. */
    Collection<String> showStaffInfo(String userID, String storeId) throws Exception;

    /* shows sales History of a specific store by permissions: system manager / store owner / store manager.
    precondition: invoker has the permissions to get the info. */
    Collection<String> getSalesHistoryByStore(String userID, String storeId) throws Exception;

    
    // ***********************************************************************
    // Topics: service level, external systems
    // ***********************************************************************
    
    /* shows the event log.
    precondition: invoker has the permissions to get the info. */
    Collection<String> getEventLog(String userID) throws Exception;
    
    /* shows the error log.
    precondition: invoker has the permissions to get the info. */
    Collection<String> getErrorLog(String userID) throws Exception;
}
