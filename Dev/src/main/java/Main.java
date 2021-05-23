import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import exceptions.SubscriberAlreadyExistsException;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import presenatation.TradingSystem;
import service.TradingSystemService;
import service.TradingSystemServiceImpl;
import tradingSystem.TradingSystemBuilder;
import tradingSystem.TradingSystemImpl;
import user.AdminPermission;
import user.Subscriber;
import util.Filters;
import util.HerokuUtil;
import util.Path;
import util.ViewUtil;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {

    // Declare dependencies
    public static presenatation.TradingSystem tradingSystem;

    public static void main(String[] args) throws InvalidActionException {

        Config cfg = new Config();

        try (InputStream input = new FileInputStream("Dev/config/config.properties")) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            cfg.adminName = prop.getProperty("system.admin.name");
            cfg.adminPassword = prop.getProperty("system.admin.password");
            cfg.port = Integer.parseInt(prop.getProperty("port"));
            cfg.sslPort = Integer.parseInt(prop.getProperty("sslPort"));
            cfg.stateFileAddress = prop.getProperty("stateFileAddress");
            cfg.startupScript = prop.getProperty("startupScript");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        run(cfg);
    }

    public static void run(Config cfg) throws InvalidActionException {

        // work around for the system initialization
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.register(cfg.adminName, cfg.adminPassword);
        ConcurrentHashMap<String, Subscriber> subscribers = new ConcurrentHashMap<>();
        AtomicInteger subscriberIdCounter = new AtomicInteger();
        Subscriber admin = new Subscriber(subscriberIdCounter.getAndIncrement(), cfg.adminName);
        admin.addPermission(AdminPermission.getInstance());
        subscribers.put(cfg.adminName, admin);
        tradingSystem.TradingSystem build = new TradingSystemBuilder().setUserName(cfg.adminName).setPassword(cfg.adminPassword)
                .setSubscriberIdCounter(subscriberIdCounter).setSubscribers(subscribers).setAuth(userAuthentication).build();
        //map.clear();
        TradingSystemService tradingSystemService = new TradingSystemServiceImpl(new TradingSystemImpl(build));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        compiler.run(null, null, null, cfg.stateFileAddress);

        try {
            Class<?> cls = Class.forName(cfg.startupScript, true, ClassLoader.getSystemClassLoader());
            Method method = cls.getMethod("run", TradingSystemService.class);
            method.invoke(null, tradingSystemService);

        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Instantiate your dependencies
        tradingSystem = new TradingSystem(tradingSystemService);

        Javalin app = Javalin.create(config -> {
            config.server(() -> {
                Server server = new Server();
                ServerConnector sslConnector = new ServerConnector(server, getSslContextFactory());
                sslConnector.setPort(cfg.sslPort);
                ServerConnector connector = new ServerConnector(server);
                connector.setPort(cfg.port);
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

            post(Path.Web.ASSIGNSTOREPURCHASEPOLICY, tradingSystem.handleAssignStorePurchasePolicyPost);
            post(Path.Web.REMOVEPOLICY, tradingSystem.handleRemovePolicyPost);
            post(Path.Web.ASSIGNSTOREDISCOUNTPOLICY, tradingSystem.handleAssignStoreDiscountPolicyPost);
            post(Path.Web.REMOVEDISCOUNT, tradingSystem.handleRemoveDiscountPost);
            get(Path.Web.ASSIGNREMOVEPOLICY, tradingSystem.serveAssignRemovePolicyPage);

            post(Path.Web.ANDPOLICY, tradingSystem.handleAndPolicyPost);
            post(Path.Web.ORPOLICY, tradingSystem.handleOrPolicyPost);
            post(Path.Web.XORPOLICY, tradingSystem.handleXorPolicyPost);
            post(Path.Web.MAKEPLUSDISCOUNT, tradingSystem.handleMakePlusDiscountPost);
            post(Path.Web.MAKEMAXDISCOUNT, tradingSystem.handleMakeMaxDiscountPost);
            get(Path.Web.COMPLEXPOLICIES, tradingSystem.serveComplexPoliciesPage);

            post(Path.Web.DELETEPRODUCTFROMSTORE, tradingSystem.handleDeleteProductFromStorePost);
            get(Path.Web.DELETEPRODUCTFROMSTORE, tradingSystem.serveDeleteProductFromStorePage);

            post(Path.Web.MAKEBASKETPURCHASEPOLICY, tradingSystem.handleMakeBasketPurchasePolicyPost);
            get(Path.Web.MAKEBASKETPURCHASEPOLICY, tradingSystem.serveMakeBasketPurchasePolicyPage);

            post(Path.Web.PURCHASECART, tradingSystem.handlePurchaseCartPost);
            get(Path.Web.PURCHASECART, tradingSystem.servePurchaseCartPage);

            post(Path.Web.ADDITEMTOBASKETBYOFFER, tradingSystem.handleAddItemToBasketByOfferPost);
            get(Path.Web.ADDITEMTOBASKETBYOFFER, tradingSystem.serveAddItemToBasketByOfferPage);

            post(Path.Web.GETOFFERSBYSTORE, tradingSystem.handleGetOffersByStorePost);
            get(Path.Web.GETOFFERSBYSTORE, tradingSystem.serveGetOffersByStorePage);

            post(Path.Web.APPROVEOFFER, tradingSystem.handleApproveOfferPost);
            get(Path.Web.APPROVEOFFER, tradingSystem.serveApproveOfferPage);

            post(Path.Web.GETSTOREDISCOUNTS, tradingSystem.handleGetStoreDiscountsPost);
            get(Path.Web.GETSTOREDISCOUNTS, tradingSystem.serveGetStoreDiscountsPage);

            post(Path.Web.GETTOTALINCOMEBYSTOREPERDAY, tradingSystem.handleGetTotalIncomeByStorePerDayPost);
            get(Path.Web.GETTOTALINCOMEBYSTOREPERDAY, tradingSystem.serveGetTotalIncomeByStorePerDayPage);

            post(Path.Web.GETTOTALINCOMEBYADMINPERDAY, tradingSystem.handleGetTotalIncomeByAdminPerDayPost);
            get(Path.Web.GETTOTALINCOMEBYADMINPERDAY, tradingSystem.serveGetTotalIncomeByAdminPerDayPage);

            post(Path.Web.UPDATEPRODUCTDETAILS, tradingSystem.handleUpdateProductDetailsPost);
            get(Path.Web.UPDATEPRODUCTDETAILS, tradingSystem.serveUpdateProductDetailsPage);

            post(Path.Web.MAKEQUANTITYPOLICY, tradingSystem.handleMakeQuantityPolicyPost);
            post(Path.Web.DOQUANTITYPOLICY, tradingSystem.handleDoQuantityPolicyPost);
            get(Path.Web.MAKEQUANTITYPOLICY, tradingSystem.serveMakeQuantityPolicyPage);

            post(Path.Web.MAKETIMEPOLICY, tradingSystem.handleMakeTimePolicyPost);
            post(Path.Web.DOTIMEPOLICY, tradingSystem.handleDoTimePolicyPost);
            get(Path.Web.MAKETIMEPOLICY, tradingSystem.serveMakeTimePolicyPage);

            post(Path.Web.MAKEQUANTITYDISCOUNT, tradingSystem.handleMakeQuantityDiscountPost);
            post(Path.Web.DOQUANTITYDISCOUNT, tradingSystem.handleDoQuantityDiscountPost);
            get(Path.Web.MAKEQUANTITYDISCOUNT, tradingSystem.serveMakeQuantityDiscountPage);

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
        @SuppressWarnings("deprecation") SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStoreType("PKCS12");
        //noinspection ConstantConditions
        sslContextFactory.setKeyStorePath(Main.class.getResource("/keystore/localhost.p12").toExternalForm());
        sslContextFactory.setKeyStorePassword("password");
        return sslContextFactory;
    }
}