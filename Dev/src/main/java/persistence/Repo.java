package persistence;

public class Repo {
    private static Repo repo_instance = null;

    private BasketDAO basketDAO;
    private InventoryDAO inventoryDAO;
    private ItemDAO itemDAO;
    private StoreDAO storeDAO;
    private SubscriberDAO subscriberDAO;
    private UserDAO userDAO;
    private OwnerPermissionDAO ownerPermissionDAO;

    private Repo() {
        basketDAO = new BasketDAO();
        inventoryDAO = new InventoryDAO();
        itemDAO = new ItemDAO();
        storeDAO = new StoreDAO();
        subscriberDAO = new SubscriberDAO();
        userDAO = new UserDAO();
        ownerPermissionDAO = new OwnerPermissionDAO();
    }

    public static Repo getInstance(){
        if(repo_instance == null)
            repo_instance = new Repo();
        return repo_instance;
    }

    public BasketDAO getBasketDAO() {
        return basketDAO;
    }

    public InventoryDAO getInventoryDAO() {
        return inventoryDAO;
    }

    public ItemDAO getItemDAO() {
        return itemDAO;
    }

    public StoreDAO getStoreDAO() {
        return storeDAO;
    }

    public SubscriberDAO getSubscriberDAO() {
        return subscriberDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public OwnerPermissionDAO getOwnerPermissionDAO() {
        return ownerPermissionDAO;
    }
}
