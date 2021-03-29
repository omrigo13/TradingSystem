public interface TradingSystemService {


    // ***********************************************************************
    // Topics: system, guest, subscriber
    // ***********************************************************************


    /* Initialize system and define a system manager.
    params: manager details */
    boolean initializeSystem(String userName, String pass);

    /* Login to system */
    boolean login(String userName, String pass);

    /* Logout from system */
    boolean logout(String userName, String pass);

    /* Get product by filter. */
    boolean getProduct(String productId, String desc, double price);

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


    // ***********************************************************************
    // Topics: store owner, store manager, system manager
    // ***********************************************************************


    /* Get info of all stores owners and managers, and the products in every store */
    boolean getStoresInfo();

    /* Get all products of the store, with store id. */
    boolean getProductsByStore(String storeId);

    /* creates a new store. username is the founder and owner. */
    boolean openNewStore(String userName, String newStoreName);

    /* appoints a new store manager. assignor is an owner of the store, assignee is the username of the new store manager
    * pre-condition: assignee is not a manager in this store */
    boolean appointStoreManager(String assignor, String assignee, String storeId);

    /* adds a product to a store */
    boolean addProductToStore(String storeId, String productName, int quantity, double price);

    /* deletes a product from a store */
    boolean deleteProductFromStore(String storeId, String productName);

    /* updates a product details of a store */
    boolean updateProductDetails(String storeId, String productName, int quantity, double price);

    /* appoints a new store owner. assignor is an owner of the store, assignee is the username of a new store owner
     * pre-condition: assignee is not an owner in this store */
    boolean appointStoreOwner(String assignor, String assignee, String storeId);

    /* defines a manager's permissions in a specific store
    * pre-condition: the owner who defines the permissions is the assignor of the manager*/
    boolean defineManagerPermissions(String storeId, String managerUserName, String permissions);

    /* removes a manager's permissions in a specific store
     * pre-condition: the owner who defines the permissions is the assignor of the manager*/
    boolean removeManager(String storeId, String managerUserName);

    /* shows store staff information and their permissions in the store */
    boolean showStaffInfo(String storeId);

    /* shows sales History of a specific store by permissions: system manager / store owner / store manager */
    boolean getSalesHistory(String storeId);


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
