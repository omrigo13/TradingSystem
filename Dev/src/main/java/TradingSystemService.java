public interface TradingSystemService {
    
    
    // ***********************************************************************
    // Topics: system, guest, subscriber
    // ***********************************************************************

    /* Initialize system and define a system manager.
    params: manager details */
    boolean initializeSystem(String userName, String pass);

    /* Register to system */
    void register(String userName, String password);

    /* Login to system */
    boolean login(String userName, String pass);

    /* Logout from system */
    boolean logout(String userName, String pass);

    /* Get product by filter. */
    boolean getProduct(String productId, String productName, double maxPrice, double minPrice);

    /* Save product in basket of a store. */
    boolean addProductToBasket(String storeId, String userName, String productId, int amount);

    /* get cart's products. */
    boolean showCart(String userName);

    /* get basket's products. */
    boolean showBasket(String userName, String storeId);

    /* updates the amount of a product for user from a specific store. if new amount = 0 then the product will be deleted from the basket */
    boolean updateProductAmountInBasket(String userName, String storeId, String productId, int newAmount);

    /* make purchase for every product in all of the user's baskets */
    boolean purchaseCart(String userName);

    /* make purchase for single basket from a store */
    boolean purchaseBasket(String userName, String storeId);

    /* get purchase history of a user by permissions: user himself / system manager */
    boolean getPurchaseHistory(String userName);
    
    /* enables user to write an opinion on a product he has purchased.
    precondition: the user has purchased the product*/
    boolean writeOpinionOnProduct(String userName, String productId, String desc);


    // ***********************************************************************
    // Topics: store owner, store manager, system manager
    // ***********************************************************************


    /* Get info of all stores owners and managers, and the products in every store
    preconditions: invoker is a system manager. */
    boolean getStoresInfo(String invokerUserName);

    /* Get all products of the store, with store id.
    preconditions: invoker is the owner/manager of the store or is a system manager.*/
    boolean getProductsByStore(String invokerUserName, String storeId);

    /* creates a new store. username is the founder and owner. */
    boolean openNewStore(String userName, String newStoreName);

    /* appoints a new store manager. assignor is an owner of the store, assignee is the username of the new store manager
     precondition: assignee is not a manager in this store.
     poscondition: assignee have the permissions of a new store manager, i.e the basic permissions for a manager, which are:
                   get info about roles in the store and their permissions, get info about products in the store,
                   get requests from users and answer them.*/
    boolean appointStoreManager(String assignor, String assignee, String storeId);

    /* adds a product to a store.
    preconditions: invoker is the store owner or is a manager of it, with permissions to make changes in products. */
    boolean addProductToStore(String invokerUserName, String storeId, String productName, int quantity, double price);

    /* deletes a product from a store 
    preconditions: invoker is the store owner or is a manager of it, with permissions to make changes in products. */
    boolean deleteProductFromStore(String invokerUserName, String storeId, String productName);

    /* updates a product details of a store.
    preconditions: invoker is the store owner or is a manager of it, with permissions to make changes in products.*/
    boolean updateProductDetails(String invokerUserName, String storeId, String productName, int quantity, double price);

    /* appoints a new store owner. assignor is an owner of the store, assignee is the username of a new store owner
     * pre-condition: assignee is not an owner in this store */
    boolean appointStoreOwner(String assignor, String assignee, String storeId);

    /*The next block of functions deals with store manager permissions. A new store manager has only the
        basic permissions in the store. */
    //******************************************************************************
    
    /* allows manager to add, delete amd update product in a specific store.
     precondition: assignor is the assignor of the manager.
     postcondition: the manager has permissions to add, delete amd update product in the store. */
    boolean allowManagerToUpdateProducts(String assignor, String storeId, String managerUserName);
    
    /* disables a manager from adding, deleting amd updating product in a specific store.
     pre-condition: assignor is the assignor of the manager
     postcondition: the manager DOESN'T have permissions to add, delete amd update product in the store. */
    boolean disableManagerFromUpdateProducts(String assignor, String storeId, String managerUserName);
    
    /* allows manager to get info and edit purchase and discount policies in a specific store.
     precondition: assignor is the assignor of the manager.
     postcondition: the manager has permissions to get info and edit purchase and discount policies in the store. */
    boolean allowManagerToEditPolicies(String assignor, String storeId, String managerUserName);
    
    /* disables a manager from getting info and editing purchase and discount policies in a specific store.
     pre-condition: assignor is the assignor of the manager
     postcondition: the manager DOESN'T have permissions to get info and edit purchase and discount policies in the store. */
    boolean disableManagerFromEditPolicies(String assignor, String storeId, String managerUserName);
    
    /* allows manager to get purchases history of the store.
     precondition: assignor is the assignor of the manager.
     postcondition: the manager has permissions to get purchases history in the store. */
    boolean allowManagerToGetHistory(String assignor, String storeId, String managerUserName);
    
    /* disables a manager from getting purchases history of the store.
     pre-condition: assignor is the assignor of the manager
     postcondition: the manager DOESN'T have permissions to get purchases history of the store. */
    boolean disableManagerFromGetHistory(String assignor, String storeId, String managerUserName);
    
    //end of block dealing with store manager permitions
    //******************************************************************************

        
    /* removes a user from the store manager role.
     * pre-condition: the incoker is an owner of the store and is the assignor of the manager*/
    boolean removeManager(String invokerUserName, String storeId, String managerUserName);

    /* shows store staff information and their permissions in the store
    precondition: invoker has the permissions to get the info. */
    boolean showStaffInfo(String invokerUserName, String storeId);

    /* shows sales History of a specific store by permissions: system manager / store owner / store manager.
    precondition: invoker has the permissions to get the info. */
    boolean getSalesHistory(String invokerUserName, String storeId);

    
    // ***********************************************************************
    // Topics: service level, external systems
    // ***********************************************************************


    /* makes a payment corresponding to params, via a external payment system */
    boolean makePayment(String payerDetails, String receiverDetails, double price);

    /* requests a supply via external supplying system */
    boolean requestSupply(String addresseeDetails, String productDetails, int amount);

    /* correcting customer spell errors while making search*/
    boolean spellChecking(String searchValue);
}
