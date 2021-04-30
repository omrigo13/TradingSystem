package presenatation;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import exceptions.InvalidConnectionIdException;
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

    public Handler handlePurchasePost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            tradingSystemService.purchaseCart(RequestUtil.getConnectionID(ctx));
            model.put("purchase", true);
            ctx.render(Path.Template.ROOT, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }
    };

    public Handler handleShowBasketPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        try{
            model.put("basket", tradingSystemService.showBasket(RequestUtil.getConnectionID(ctx), RequestUtil.getStoreID(ctx)));
            ctx.render(Path.Template.SHOWBASKET, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }catch (Exception e) {
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
        }catch (Exception e) {
            model.put("updateFailed", true);
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
            model.put("authenticationSucceeded", true);
            model.put("currentUser", RequestUtil.getQueryUsername(ctx));
            if (RequestUtil.getQueryLoginRedirect(ctx) != null) {
                ctx.redirect(RequestUtil.getQueryLoginRedirect(ctx));
            }
            ctx.render(Path.Template.ROOT, model);
        }catch (InvalidConnectionIdException ex) {
            ctx.render(Path.Template.INVALID_CONNECTION, model);
        }
            catch (Exception e) {
            model.put("authenticationFailed", true);
            ctx.render(Path.Template.LOGIN, model);
        }
    };

    public Handler handleLogoutPost = ctx -> {
        ctx.sessionAttribute("currentUser", null);
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

}
