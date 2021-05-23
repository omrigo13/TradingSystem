import exceptions.InvalidActionException;
import service.TradingSystemService;

public class BadConfigScript2 {


    /**
     trying to add to basket an item which the store does not have.
     **/
    @SuppressWarnings("unused")
    public static void run(TradingSystemService tradingSystemService) throws InvalidActionException {
        String userName1 = "Lidor", userName2 = "Omri";
        String password = "123";
        tradingSystemService.register(userName1, password);
        tradingSystemService.register(userName2, password);
        String connId1 = tradingSystemService.connect();
        tradingSystemService.login(connId1, userName1, password);
        String connId2 = tradingSystemService.connect();
        tradingSystemService.login(connId2, userName2, password);
        String storeId1 = tradingSystemService.openNewStore(connId1, "Store1");

        String product1 = tradingSystemService.addProductToStore(connId1, storeId1, "milk", "DairyProducts", "sub1", 10, 6.5);

        tradingSystemService.addItemToBasket(connId1, storeId1, "90", 2);



    }

}
