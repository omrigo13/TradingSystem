public interface TradingSystemService {

    // Topics: system, guest, subscriber

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



    // Topics: store owner, store manager, system manager

    /* Get info of all stores owners and managers, and the products in every store */
    boolean getStoresInfo();

    /* Get all products of the store, with store id. */
    boolean getProductsByStore(String storeId);




    // Topics: service level, external systems

}
