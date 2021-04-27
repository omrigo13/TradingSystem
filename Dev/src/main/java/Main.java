import exceptions.InvalidActionException;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
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
            post(Path.Web.LOGIN, tradingSystem.handleLoginPost);
            post(Path.Web.LOGOUT, tradingSystem.handleLogoutPost);
            post(Path.Web.SEARCH, tradingSystem.handleSearchPost);
        });

        app.error(404, ViewUtil.NotFound);
    }

}
