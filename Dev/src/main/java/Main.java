import exceptions.InvalidActionException;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import presenatation.TradingSystem;
import util.Filters;
import util.HerokuUtil;
import util.Path;
import util.ViewUtil;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {

    // Declare dependencies
    public static presenatation.TradingSystem tradingSystem;

    public static void main(String[] args) throws InvalidActionException {

        // Instantiate your dependencies
        tradingSystem = new TradingSystem();

        Javalin app = Javalin.create(config -> {
            config.server(() -> {
                Server server = new Server();
                ServerConnector sslConnector = new ServerConnector(server, getSslContextFactory());
                sslConnector.setPort(443);
                ServerConnector connector = new ServerConnector(server);
                connector.setPort(80);
                server.setConnectors(new Connector[]{sslConnector, connector});
                return server;
            });
            config.addStaticFiles("/public");
            config.registerPlugin(new RouteOverviewPlugin("/routes"));
        }).start(HerokuUtil.getHerokuAssignedPort());

        app.routes(() -> {
            before(Filters.handleLocaleChange);
            //before(LoginController.ensureLoginBeforeViewingBooks);
            get(Path.Web.HOME, tradingSystem.serveHomePage);
            get(Path.Web.REGISTER, tradingSystem.serveRegisterPage);
            post(Path.Web.REGISTER, tradingSystem.handleRegisterPost);
            post(Path.Web.NotFound, tradingSystem.handleNotFoundPost);
            get(Path.Web.ROOT, tradingSystem.serveRootPage);
            post(Path.Web.ROOT, tradingSystem.handleRootPost);
            //get(Path.Web.BOOKS, BookController.fetchAllBooks);
            //get(Path.Web.ONE_BOOK, BookController.fetchOneBook);
            post(Path.Web.PURCHASE, tradingSystem.handlePurchasePost);
            get(Path.Web.LOGIN, tradingSystem.serveLoginPage);
            get(Path.Web.SHOWBASKET, tradingSystem.serveShowBasketPage);
            post(Path.Web.SHOWBASKET, tradingSystem.handleShowBasketPost);
            get(Path.Web.UPDATEPRODUCTAMOUNTINBASKET, tradingSystem.serveUpdateProductAmountInBasket);
            post(Path.Web.UPDATEPRODUCTAMOUNTINBASKET, tradingSystem.handleUpdateProductAmountInBasketPost);
            get(Path.Web.CART, tradingSystem.serveCartPage);
            post(Path.Web.CART, tradingSystem.handleCartPost);
            get(Path.Web.PURCHASEHISTORY, tradingSystem.servePurchaseHistoryPage);
            post(Path.Web.PURCHASEHISTORY, tradingSystem.handlePurchaseHistoryPost);
            get(Path.Web.OPENNEWSTORE, tradingSystem.serveOpenNewStorePage);
            post(Path.Web.OPENNEWSTORE, tradingSystem.handleOpenNewStorePost);
            get(Path.Web.ADDITEMTOSTORE, tradingSystem.serveAddItemToStorePage);

            post(Path.Web.ALLOWMANAGERTOUPDATEPRODUCTS, tradingSystem.handleAllowManagerToUpdateProductsPost);
            post(Path.Web.DISABLEMANAGERFROMUPDATEPRODUCTS, tradingSystem.handleDisableManagerFromUpdateProductsPost);
            post(Path.Web.ALLOWMANAGERTOEDITPOLICIES, tradingSystem.handleAllowManagerToEditPoliciesPost);
            post(Path.Web.DISABLEMANAGERFROMEDITPOLICIES, tradingSystem.handleDisableManagerFromEditPoliciesPost);
            post(Path.Web.ALLOWMANAGERTOGETHISTORY, tradingSystem.handleAllowManagerToGetHistoryPost);
            post(Path.Web.DISABLEMANAGERFROMGETHISTORY, tradingSystem.handleDisableManagerFromGetHistoryPost);
            get(Path.Web.PERMISSIONSFORMANAGER, tradingSystem.servePermissionsForManagerPage);

            post(Path.Web.GETITEMS, tradingSystem.handleGetItemsPost);
            get(Path.Web.GETITEMS, tradingSystem.serveGetItemsPage);

            post(Path.Web.ADDITEMTOBASKET, tradingSystem.handleAddItemToBasketPost);
            get(Path.Web.ADDITEMTOBASKET, tradingSystem.serveAddItemToBasketPage);

            post(Path.Web.WRITEOPINIONONPRODUCT, tradingSystem.handleWriteOpinionOnProductPost);
            get(Path.Web.WRITEOPINIONONPRODUCT, tradingSystem.serveWriteOpinionOnProductPage);

            post(Path.Web.GETITEMSBYSTORE, tradingSystem.handleGetItemsBtStorePost);
            post(Path.Web.SHOWSTAFFINFO, tradingSystem.handleShowStaffPost);
            post(Path.Web.SALESHISTORY, tradingSystem.handleGetSalesHistoryPost);
            post(Path.Web.STOREPOLICIES, tradingSystem.handleGetStorePoliciesPost);
            get(Path.Web.GETSTOREDETAILS, tradingSystem.serveGetStoreDetailsPage);

            post(Path.Web.GETSTORESINFO, tradingSystem.handleGetStoresInfoPost);
            post(Path.Web.GETERRORLOG, tradingSystem.handleGetErrorLogPost);
            post(Path.Web.GETEVENTLOG, tradingSystem.handleGetEventLogPost);
            get(Path.Web.ADMINACTIONS, tradingSystem.serveAdminActionsPage);

            post(Path.Web.DELETEPRODUCTFROMSTORE, tradingSystem.handleDeleteProductFromStorePost);
            get(Path.Web.DELETEPRODUCTFROMSTORE, tradingSystem.serveDeleteProductFromStorePage);

            post(Path.Web.UPDATEPRODUCTDETAILS, tradingSystem.handleUpdateProductDetailsPost);
            get(Path.Web.UPDATEPRODUCTDETAILS, tradingSystem.serveUpdateProductDetailsPage);

            post(Path.Web.APPOINTSTOREOWNER, tradingSystem.handleAppointStoreOwnerPost);
            post(Path.Web.APPOINTSTOREMANAGER, tradingSystem.handleAppointStoreManagerPost);
            post(Path.Web.REMOVEOWNER, tradingSystem.handleRemoveOwnerPost);
            post(Path.Web.REMOVEMANAGER, tradingSystem.handleRemoveManagerPost);
            get(Path.Web.APPOINTREMOVEMANAGEROROWNER, tradingSystem.serveAppointRemoveManagerOrOwnerPage);

            post(Path.Web.ADDITEMTOSTORE, tradingSystem.handleAddItemToStorePost);
            post(Path.Web.LOGIN, tradingSystem.handleLoginPost);
            post(Path.Web.LOGOUT, tradingSystem.handleLogoutPost);
            post(Path.Web.SEARCH, tradingSystem.handleSearchPost);
        });

        app.error(404, ViewUtil.NotFound);
    }

    private static SslContextFactory getSslContextFactory() {
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStoreType("PKCS12");
        sslContextFactory.setKeyStorePath(Main.class.getResource("/keystore/localhost.p12").toExternalForm());
        sslContextFactory.setKeyStorePassword("password");
        return sslContextFactory;
    }
}
