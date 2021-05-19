package presenatation;

import authentication.UserAuthentication;
import exceptions.*;
import io.javalin.http.Handler;
import service.TradingSystemService;
import service.TradingSystemServiceImpl;
import tradingSystem.TradingSystemBuilder;
import tradingSystem.TradingSystemImpl;
import user.AdminPermission;
import user.Subscriber;
import util.Path;
import util.RequestUtil;
import util.ViewUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TradingSystem {
    private TradingSystemService tradingSystemService;

    public TradingSystem() throws InvalidActionException {
        // work around for the system initialization
        UserAuthentication userAuthentication = new UserAuthentication();
        String userName = "Admin";
        String password = "123";
        userAuthentication.register(userName, password);
        ConcurrentHashMap<String, Subscriber> subscribers = new ConcurrentHashMap<>();
        AtomicInteger subscriberIdCounter = new AtomicInteger();
        Subscriber admin = new Subscriber(subscriberIdCounter.getAndIncrement(), userName);
        admin.addPermission(AdminPermission.getInstance());
        subscribers.put(userName, admin);
        tradingSystem.TradingSystem build = new TradingSystemBuilder().setUserName(userName).setPassword(password)
                .setSubscriberIdCounter(subscriberIdCounter).setSubscribers(subscribers).setAuth(userAuthentication).build();
        //map.clear();
        tradingSystemService = new TradingSystemServiceImpl(new TradingSystemImpl(build));
    }

    public Handler serveHomePage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.ROOT, model);
    };

    public Handler serveShowBasketPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.SHOWBASKET, model);
    };

    public Handler serveUpdateProductAmountInBasket = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.UPDATEPRODUCTAMOUNTINBASKET, model);
    };

    public Handler serveRootPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        //model.put("connectID", false);
        ctx.render(Path.Template.ROOT, model);
    };

    public Handler serveCartPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.Cart, model);
    };

    public Handler servePurchaseHistoryPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.PURCHASEHISTORY, model);
    };

    public Handler serveOpenNewStorePage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.OPENNEWSTORE, model);
    };

    public Handler handleOpenNewStorePost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("storeID", tradingSystemService.openNewStore(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreName(ctx)));
            ctx.render(Path.Template.OPENNEWSTORE, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("openStoreFailed", true);
            ctx.render(Path.Template.OPENNEWSTORE, model);
        }
    };

    public Handler serveAddItemToBasketPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.ADDITEMTOBASKET, model);
    };

    public Handler handleAddItemToBasketPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.addItemToBasket(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getProductID(ctx), RequestUtil.getAmount(ctx));
            model.put("success", true);
            ctx.render(Path.Template.ADDITEMTOBASKET, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (InvalidStoreIdException e) {
            model.put("InvalidStoreId", true);
            ctx.render(Path.Template.ADDITEMTOBASKET, model);
        }catch (ItemNotFoundException e) {
            model.put("ItemNotFound", true);
            ctx.render(Path.Template.ADDITEMTOBASKET, model);
        }
    };

    public Handler serveGetItemsPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.GETITEMS, model);
    };

    public Handler handleGetItemsPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("items", tradingSystemService.getItems(RequestUtil.getKeyWord(ctx), RequestUtil.getProduceName(ctx), RequestUtil.getCategory(ctx), RequestUtil.getSubCategory(ctx), RequestUtil.getRatingItem(ctx), RequestUtil.getRatingStore(ctx), RequestUtil.getMaxPrice(ctx),RequestUtil.getMinPrice(ctx)));
            ctx.render(Path.Template.GETITEMS, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("itemsFailed", true);
            ctx.render(Path.Template.GETITEMS, model);
        }
    };

    public Handler serveWriteOpinionOnProductPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.WRITEOPINIONONPRODUCT, model);
    };

    public Handler handleWriteOpinionOnProductPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.writeOpinionOnProduct(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getProductID(ctx), RequestUtil.getDesc(ctx));
            model.put("success", true);
            ctx.render(Path.Template.WRITEOPINIONONPRODUCT, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("Failed", true);
            ctx.render(Path.Template.WRITEOPINIONONPRODUCT, model);
        }
    };

    public Handler serveAddItemToStorePage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.ADDITEMTOSTORE, model);
    };

    public Handler handleAddItemToStorePost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("itemID", tradingSystemService.addProductToStore(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getProduceName(ctx), RequestUtil.getCategory(ctx), RequestUtil.getSubCategory(ctx), RequestUtil.getAmount(ctx), RequestUtil.getPrice(ctx)));
            ctx.render(Path.Template.ADDITEMTOSTORE, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("addItemToStoreFailed", true);
            ctx.render(Path.Template.ADDITEMTOSTORE, model);
        }
    };

    public Handler serveGetStoreDetailsPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.GETSTOREDETAILS, model);
    };

    public Handler handleGetItemsBtStorePost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("details", tradingSystemService.getItemsByStore(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx)));
            ctx.render(Path.Template.GETSTOREDETAILS, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.GETSTOREDETAILS, model);
        }
    };

    public Handler handleShowStaffPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("details", tradingSystemService.showStaffInfo(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx)));
            ctx.render(Path.Template.GETSTOREDETAILS, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.GETSTOREDETAILS, model);
        }
    };

    public Handler handleGetSalesHistoryPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("details", tradingSystemService.getSalesHistoryByStore(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx)));
            ctx.render(Path.Template.GETSTOREDETAILS, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.GETSTOREDETAILS, model);
        }
    };

    public Handler handleGetStorePoliciesPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("details", tradingSystemService.getStorePolicies(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx)));
            ctx.render(Path.Template.GETSTOREDETAILS, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.GETSTOREDETAILS, model);
        }
    };

    public Handler handleGetStoresInfoPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("info", tradingSystemService.getStoresInfo(RequestUtil.getConnectionID(ctx)));
            ctx.render(Path.Template.ADMINACTIONS, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.ADMINACTIONS, model);
        }
    };

    public Handler handleGetErrorLogPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("info", tradingSystemService.getErrorLog(RequestUtil.getConnectionID(ctx)));
            ctx.render(Path.Template.ADMINACTIONS, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.ADMINACTIONS, model);
        }
    };

    public Handler handleGetEventLogPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("info", tradingSystemService.getEventLog(RequestUtil.getConnectionID(ctx)));
            ctx.render(Path.Template.ADMINACTIONS, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.ADMINACTIONS, model);
        }
    };

    public Handler serveAdminActionsPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.ADMINACTIONS, model);
    };

    public Handler serveDeleteProductFromStorePage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.DELETEPRODUCTFROMSTORE, model);
    };

    public Handler handleDeleteProductFromStorePost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.deleteProductFromStore(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getProductID(ctx));
            model.put("success", true);
            ctx.render(Path.Template.DELETEPRODUCTFROMSTORE, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.DELETEPRODUCTFROMSTORE, model);
        }
    };

    public Handler serveUpdateProductDetailsPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.UPDATEPRODUCTDETAILS, model);
    };

    public Handler handleUpdateProductDetailsPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.updateProductDetails(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getProductID(ctx), RequestUtil.getSubCategory(ctx), RequestUtil.getAmount(ctx), RequestUtil.getPrice(ctx));
            model.put("success", true);
            ctx.render(Path.Template.UPDATEPRODUCTDETAILS, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.UPDATEPRODUCTDETAILS, model);
        }
    };

    public Handler handlePurchasePost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
            ctx.render(Path.Template.PURCHASECART, model);
    };

    public Handler handlePurchaseCartPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.purchaseCart(RequestUtil.getConnectionID(ctx),ctx.formParam("card_number"), Integer.parseInt(ctx.formParam("month")), Integer.parseInt(ctx.formParam("year")), ctx.formParam("holder"), ctx.formParam("ccv"), ctx.formParam("id"), ctx.formParam("name"), ctx.formParam("address"), ctx.formParam("city"), ctx.formParam("country"), Integer.parseInt(ctx.formParam("zip")));
            model.put("success", true);
            ctx.render(Path.Template.PURCHASECART, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.PURCHASECART, model);
        }
    };

    public Handler servePurchaseCartPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.PURCHASECART, model);
    };

    public Handler handleShowBasketPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("basket", tradingSystemService.showBasket(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx)));
            ctx.render(Path.Template.SHOWBASKET, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (InvalidStoreIdException e) {
            model.put("showBasketFailed", true);
            ctx.render(Path.Template.SHOWBASKET, model);
        }
    };

    public Handler handleUpdateProductAmountInBasketPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.updateProductAmountInBasket(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getProductID(ctx), RequestUtil.getAmount(ctx));
            model.put("updateSucceeded", true);
            ctx.render(Path.Template.UPDATEPRODUCTAMOUNTINBASKET, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (ItemException e) {
            model.put("ItemException", true);
            ctx.render(Path.Template.UPDATEPRODUCTAMOUNTINBASKET, model);
        }catch (InvalidStoreIdException e) {
            model.put("InvalidStoreId", true);
            ctx.render(Path.Template.UPDATEPRODUCTAMOUNTINBASKET, model);
        }
    };

    public Handler handleSearchPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("search", tradingSystemService.getItems(RequestUtil.getSearchBox(ctx),null,null,null,null,null,null,null));
            ctx.render(Path.Template.ROOT, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }
    };

    public Handler handleCartPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("cart", tradingSystemService.showCart(RequestUtil.getConnectionID(ctx)));
            ctx.render(Path.Template.Cart, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }
    };

    public Handler handlePurchaseHistoryPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("purchaseHistory", tradingSystemService.getPurchaseHistory(RequestUtil.getConnectionID(ctx)));
            ctx.render(Path.Template.PURCHASEHISTORY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("HistoryFailed", true);
            ctx.render(Path.Template.ROOT, model);
        }
    };

    public Handler serveRegisterPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.REGISTER, model);
    };

    public Handler handleNotFoundPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        //model.put("connectID", tradingSystemService.connect());
        ctx.render(Path.Template.NotFound, model);
    };

    public Handler handleRootPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        model.put("connectID", tradingSystemService.connect());
        ctx.render(Path.Template.ROOT, model);
    };

    public Handler handleRegisterPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try {
            tradingSystemService.register(RequestUtil.getQueryUsername(ctx), RequestUtil.getQueryPassword(ctx));
            model.put("authenticationSucceeded", true);
            if (RequestUtil.getQueryLoginRedirect(ctx) != null) {
                ctx.redirect(RequestUtil.getQueryLoginRedirect(ctx));
            }
            ctx.render(Path.Template.REGISTER, model);
        }catch (Exception e){
            model.put("authenticationFailed", true);
            ctx.render(Path.Template.REGISTER, model);
        }
    };

    public Handler serveLoginPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        model.put("loggedOut", RequestUtil.removeSessionAttrLoggedOut(ctx));
        ctx.render(Path.Template.LOGIN, model);
    };

    public Handler handleLoginPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try {
            tradingSystemService.login(RequestUtil.getConnectionID(ctx), RequestUtil.getQueryUsername(ctx), RequestUtil.getQueryPassword(ctx));
            ctx.sessionAttribute("currentUser", RequestUtil.getQueryUsername(ctx));
            if(tradingSystemService.isAdmin(RequestUtil.getConnectionID(ctx)))
            {
                ctx.sessionAttribute("admin", "true");
            }
            Collection<String> notifications = tradingSystemService.getNotifications(RequestUtil.getConnectionID(ctx));
            ctx.sessionAttribute("notifications", notifications);
            model.put("notifications", notifications);
            model.put("authenticationSucceeded", true);
            model.put("currentUser", RequestUtil.getQueryUsername(ctx));
            model.put("admin", tradingSystemService.isAdmin(RequestUtil.getConnectionID(ctx)));
            if (RequestUtil.getQueryLoginRedirect(ctx) != null) {
                ctx.redirect(RequestUtil.getQueryLoginRedirect(ctx));
            }
            ctx.render(Path.Template.ROOT, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }
            catch (SubscriberDoesNotExistException e) {
            model.put("SubscriberDoesNotExist", true);
            ctx.render(Path.Template.LOGIN, model);
        }catch (WrongPasswordException e) {
            model.put("WrongPassword", true);
            ctx.render(Path.Template.LOGIN, model);
        }
    };

    public Handler handleLogoutPost = ctx -> {
        tradingSystemService.logout(RequestUtil.getConnectionIDLogout(ctx));
        ctx.sessionAttribute("currentUser", null);
        ctx.sessionAttribute("admin", null);
        ctx.sessionAttribute("notifications", null);
        ctx.sessionAttribute("loggedOut", "true");
        ctx.redirect(Path.Web.LOGIN);
    };

    public Handler servePermissionsForManagerPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
    };

    public Handler handleAllowManagerToUpdateProductsPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.allowManagerToUpdateProducts(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getManagerUserName(ctx));
            model.put("succeeded", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }
    };

    public Handler handleDisableManagerFromUpdateProductsPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.disableManagerFromUpdateProducts(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getManagerUserName(ctx));
            model.put("succeeded", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }
    };

    public Handler handleAllowManagerToEditPoliciesPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.allowManagerToEditPolicies(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getManagerUserName(ctx));
            model.put("succeeded", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }
    };

    public Handler handleDisableManagerFromEditPoliciesPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.disableManagerFromEditPolicies(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getManagerUserName(ctx));
            model.put("succeeded", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }
    };

    public Handler handleAllowManagerToGetHistoryPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.allowManagerToGetHistory(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getManagerUserName(ctx));
            model.put("succeeded", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }
    };

    public Handler handleDisableManagerFromGetHistoryPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.disableManagerFromGetHistory(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getManagerUserName(ctx));
            model.put("succeeded", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.PERMISSIONSFORMANAGER, model);
        }
    };

    public Handler serveAppointRemoveManagerOrOwnerPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.APPOINTREMOVEMANAGEROROWNER, model);
    };

    public Handler handleAppointStoreOwnerPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.appointStoreOwner(RequestUtil.getConnectionID(ctx), RequestUtil.getQueryUsername(ctx), RequestUtil.getStoreID(ctx));
            model.put("succeeded", true);
            ctx.render(Path.Template.APPOINTREMOVEMANAGEROROWNER, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.APPOINTREMOVEMANAGEROROWNER, model);
        }
    };

    public Handler handleAppointStoreManagerPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.appointStoreManager(RequestUtil.getConnectionID(ctx), RequestUtil.getQueryUsername(ctx), RequestUtil.getStoreID(ctx));
            model.put("succeeded", true);
            ctx.render(Path.Template.APPOINTREMOVEMANAGEROROWNER, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.APPOINTREMOVEMANAGEROROWNER, model);
        }
    };

    public Handler handleRemoveOwnerPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.removeOwner(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getQueryUsername(ctx));
            model.put("succeeded", true);
            ctx.render(Path.Template.APPOINTREMOVEMANAGEROROWNER, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.APPOINTREMOVEMANAGEROROWNER, model);
        }
    };

    public Handler handleRemoveManagerPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.removeManager(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getQueryUsername(ctx));
            model.put("succeeded", true);
            ctx.render(Path.Template.APPOINTREMOVEMANAGEROROWNER, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.APPOINTREMOVEMANAGEROROWNER, model);
        }
    };

    public Handler serveAssignRemovePolicyPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.ASSIGNREMOVEPOLICY, model);
    };

    public Handler handleAssignStorePurchasePolicyPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.assignStorePurchasePolicy(RequestUtil.getPolicyID(ctx),RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx));
            model.put("success", true);
            ctx.render(Path.Template.ASSIGNREMOVEPOLICY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.ASSIGNREMOVEPOLICY, model);
        }
    };

    public Handler handleRemovePolicyPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.removePolicy(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getPolicyID(ctx));
            model.put("success", true);
            ctx.render(Path.Template.ASSIGNREMOVEPOLICY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.ASSIGNREMOVEPOLICY, model);
        }
    };

    public Handler handleAssignStoreDiscountPolicyPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.assignStoreDiscountPolicy(RequestUtil.getPolicyID(ctx),RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx));
            model.put("success", true);
            ctx.render(Path.Template.ASSIGNREMOVEPOLICY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.ASSIGNREMOVEPOLICY, model);
        }
    };

    public Handler handleRemoveDiscountPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.removeDiscount(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getPolicyID(ctx));
            model.put("success", true);
            ctx.render(Path.Template.ASSIGNREMOVEPOLICY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.ASSIGNREMOVEPOLICY, model);
        }
    };

    public Handler serveComplexPoliciesPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.COMPLEXPOLICIES, model);
    };

    public Handler handleAndPolicyPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("success", tradingSystemService.andPolicy(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getPolicyID(ctx), RequestUtil.getPolicyID2(ctx)));
            ctx.render(Path.Template.COMPLEXPOLICIES, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.COMPLEXPOLICIES, model);
        }
    };

    public Handler handleOrPolicyPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("success", tradingSystemService.orPolicy(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getPolicyID(ctx), RequestUtil.getPolicyID2(ctx)));
            ctx.render(Path.Template.COMPLEXPOLICIES, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.COMPLEXPOLICIES, model);
        }
    };

    public Handler handleXorPolicyPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("success", tradingSystemService.xorPolicy(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getPolicyID(ctx), RequestUtil.getPolicyID2(ctx)));
            ctx.render(Path.Template.COMPLEXPOLICIES, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.COMPLEXPOLICIES, model);
        }
    };

    public Handler handleMakePlusDiscountPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("success", tradingSystemService.makePlusDiscount(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getPolicyID(ctx), RequestUtil.getPolicyID2(ctx)));
            ctx.render(Path.Template.COMPLEXPOLICIES, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.COMPLEXPOLICIES, model);
        }
    };

    public Handler handleMakeMaxDiscountPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("success", tradingSystemService.makeMaxDiscount(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getPolicyID(ctx), RequestUtil.getPolicyID2(ctx)));
            ctx.render(Path.Template.COMPLEXPOLICIES, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.COMPLEXPOLICIES, model);
        }
    };

    public Handler serveMakeBasketPurchasePolicyPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.MAKEBASKETPURCHASEPOLICY, model);
    };

    public Handler handleMakeBasketPurchasePolicyPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("success", tradingSystemService.makeBasketPurchasePolicy(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getAmount(ctx)));
            ctx.render(Path.Template.MAKEBASKETPURCHASEPOLICY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.MAKEBASKETPURCHASEPOLICY, model);
        }
    };

    public Handler serveGetStoreDiscountsPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.GETSTOREDISCOUNTS, model);
    };

    public Handler handleGetStoreDiscountsPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("discounts", tradingSystemService.getStoreDiscounts(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx)));
            ctx.render(Path.Template.GETSTOREDISCOUNTS, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.GETSTOREDISCOUNTS, model);
        }
    };

    public Handler handleMakeQuantityPolicyPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("items", tradingSystemService.getItemsByStore(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx)));
            model.put("storeID", RequestUtil.getStoreID(ctx));
            ctx.render(Path.Template.MAKEQUANTITYPOLICY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.MAKEQUANTITYPOLICY, model);
        }
    };

    public Handler handleDoQuantityPolicyPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("policy", tradingSystemService.makeQuantityPolicy(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getItems(ctx), RequestUtil.getMinQuantity(ctx), RequestUtil.getMaxQuantity(ctx)));
            ctx.render(Path.Template.MAKEQUANTITYPOLICY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.MAKEQUANTITYPOLICY, model);
        }
    };

    public Handler serveMakeQuantityPolicyPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.MAKEQUANTITYPOLICY, model);
    };

    public Handler handleMakeTimePolicyPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("items", tradingSystemService.getItemsByStore(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx)));
            model.put("storeID", RequestUtil.getStoreID(ctx));
            ctx.render(Path.Template.MAKETIMEPOLICY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.MAKETIMEPOLICY, model);
        }
    };

    public Handler handleDoTimePolicyPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("policy", tradingSystemService.makeTimePolicy(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getItems(ctx), RequestUtil.getTime(ctx)));
            ctx.render(Path.Template.MAKETIMEPOLICY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.MAKETIMEPOLICY, model);
        }
    };

    public Handler serveMakeTimePolicyPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.MAKETIMEPOLICY, model);
    };

    public Handler handleMakeQuantityDiscountPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("items", tradingSystemService.getItemsByStore(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx)));
            model.put("storeID", RequestUtil.getStoreID(ctx));
            ctx.render(Path.Template.MAKEQUANTITYDISCOUNT, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.MAKEQUANTITYDISCOUNT, model);
        }
    };

    public Handler handleDoQuantityDiscountPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("discount", tradingSystemService.makeQuantityDiscount(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getDiscount(ctx),  RequestUtil.getItems(ctx), RequestUtil.getPolicyID(ctx)));
            ctx.render(Path.Template.MAKEQUANTITYDISCOUNT, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.MAKEQUANTITYDISCOUNT, model);
        }
    };

    public Handler serveMakeQuantityDiscountPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.MAKEQUANTITYDISCOUNT, model);
    };

    public Handler handleGetTotalIncomeByStorePerDayPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("income", tradingSystemService.getTotalIncomeByStorePerDay(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx), RequestUtil.getDate(ctx)));
            ctx.render(Path.Template.GETTOTALINCOMEBYSTOREPERDAY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.GETTOTALINCOMEBYSTOREPERDAY, model);
        }
    };

    public Handler serveGetTotalIncomeByStorePerDayPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.GETTOTALINCOMEBYSTOREPERDAY, model);
    };

    public Handler handleGetTotalIncomeByAdminPerDayPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("income", tradingSystemService.getTotalIncomeByAdminPerDay(RequestUtil.getConnectionID(ctx), RequestUtil.getDate(ctx)));
            ctx.render(Path.Template.GETTOTALINCOMEBYADMINPERDAY, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
            model.put("failed", true);
            ctx.render(Path.Template.GETTOTALINCOMEBYADMINPERDAY, model);
        }
    };

    public Handler serveGetTotalIncomeByAdminPerDayPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        ctx.render(Path.Template.GETTOTALINCOMEBYADMINPERDAY, model);
    };

}
