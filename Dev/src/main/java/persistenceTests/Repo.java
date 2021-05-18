package persistenceTests;

public class Repo {
    private BasketDAO basketDAO;
    private InventoryDAO inventoryDAO;
    private ItemDAO itemDAO;
    private StoreDAO storeDAO;
    private SubscriberDAO subscriberDAO;
    private UserDAO userDAO;
    private OwnerPermissionDAO ownerPermissionDAO;

    public Repo() {
        this.ownerPermissionDAO = new OwnerPermissionDAO();
        this.basketDAO = new BasketDAO();
        this.inventoryDAO = new InventoryDAO();
        this.itemDAO = new ItemDAO();
        this.storeDAO = new StoreDAO();
        this.subscriberDAO = new SubscriberDAO();
        this.userDAO = new UserDAO();
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
