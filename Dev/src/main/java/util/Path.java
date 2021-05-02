package util;

public class Path {

    public static class Web {
        public static final String HOME = "/layout";
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
        public static final String LOGOUT = "/logout";
        public static final String PURCHASE = "/purchase";
        public static final String SHOWBASKET = "/showBasket";
        public static final String UPDATEPRODUCTAMOUNTINBASKET = "/updateProductAmountInBasket";
        public static final String PURCHASEHISTORY = "/purchaseHistory";
        public static final String CART = "/cart";
        public static final String SEARCH = "/search";
        public static final String OPENNEWSTORE = "/openNewStore";
        public static final String ADDITEMTOSTORE = "/addItemToStore";
        public static final String UPDATEPRODUCTDETAILS = "/updateProductDetails";
        public static final String PERMISSIONSFORMANAGER = "/permissionsForManager";
        public static final String ALLOWMANAGERTOUPDATEPRODUCTS = "/allowManagerToUpdateProducts";
        public static final String DISABLEMANAGERFROMUPDATEPRODUCTS = "/disableManagerFromUpdateProducts";
        public static final String ALLOWMANAGERTOEDITPOLICIES = "/allowManagerToEditPolicies";
        public static final String DISABLEMANAGERFROMEDITPOLICIES = "/disableManagerFromEditPolicies";
        public static final String ALLOWMANAGERTOGETHISTORY = "/allowManagerToGetHistory";
        public static final String DISABLEMANAGERFROMGETHISTORY = "/disableManagerFromGetHistory";
        public static final String APPOINTREMOVEMANAGEROROWNER = "/appointRemoveManagerOrOwner";
        public static final String APPOINTSTOREMANAGER = "/appointStoreManager";
        public static final String REMOVEMANAGER = "/removeManager";
        public static final String APPOINTSTOREOWNER = "/appointStoreOwner";
        public static final String REMOVEOWNER = "/removeOwner";
        public static final String GETITEMS = "/getItems";
        public static final String ADDITEMTOBASKET = "/addItemToBasket";
        public static final String WRITEOPINIONONPRODUCT = "/writeOpinionOnProduct";
        public static final String ADMINACTIONS = "/adminActions";
        public static final String GETSTORESINFO = "/getStoresInfo";
        public static final String GETERRORLOG = "/getErrorLog";
        public static final String GETEVENTLOG = "/getEventLog";
        public static final String GETSTOREDETAILS = "/getStoreDetails";
        public static final String GETITEMSBYSTORE = "/getItemsByStore";
        public static final String SHOWSTAFFINFO = "/showStaffInfo";
        public static final String SALESHISTORY = "/salesHistory";
        public static final String STOREPOLICIES = "/storePolicies";
        public static final String DELETEPRODUCTFROMSTORE = "/deleteProductFromStore";
        public static final String NotFound = "/notfound";
        public static final String ROOT = "/";
    }

    public static class Template {
        public static final String LOGIN = "/velocity/login/login.vm";
        public static final String REGISTER = "/velocity/register/register.vm";
        public static final String NotFound = "/velocity/notfound.vm";
        public static final String ROOT = "/velocity/root.vm";
        public static final String Cart = "/velocity/cart/cart.vm";
        public static final String GETITEMS = "/velocity/store/getItems.vm";
        public static final String GETSTOREDETAILS = "/velocity/store/getStoreDetails.vm";
        public static final String ADMINACTIONS = "/velocity/login/adminActions.vm";
        public static final String DELETEPRODUCTFROMSTORE = "/velocity/store/deleteProductFromStore.vm";
        public static final String UPDATEPRODUCTDETAILS = "/velocity/store/updateProductDetails.vm";
        public static final String ADDITEMTOBASKET = "/velocity/cart/addItemToBasket.vm";
        public static final String WRITEOPINIONONPRODUCT = "/velocity/store/writeOpinionOnProduct.vm";
        public static final String SHOWBASKET = "/velocity/cart/showBasket.vm";
        public static final String UPDATEPRODUCTAMOUNTINBASKET = "velocity/cart/updateProductAmountInBasket.vm";
        public static final String PURCHASEHISTORY = "velocity/cart/purchaseHistory.vm";
        public static final String OPENNEWSTORE = "velocity/store/openNewStore.vm";
        public static final String ADDITEMTOSTORE = "velocity/store/addItemToStore.vm";
        public static final String PERMISSIONSFORMANAGER = "velocity/store/permissionsForManager.vm";
        public static final String APPOINTREMOVEMANAGEROROWNER = "velocity/store/appointRemoveManagerOrOwner.vm";
        public static final String INVALID_CONNECTION = "/velocity/invalidConnection.vm";
    }
}
