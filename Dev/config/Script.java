import exceptions.InvalidActionException;
import service.TradingSystemService;

public class Script {

    @SuppressWarnings("unused")
    public static void run(TradingSystemService tradingSystemService) throws InvalidActionException {
        String userName1 = "Tal", userName2 = "Omri", userName3 = "Noa";
        String u1 = "U1", u2 = "U2", u3 = "U3", u4 = "U4";
        String password = "123";
        tradingSystemService.register(userName1, password);
        tradingSystemService.register(userName2, password);
        tradingSystemService.register(userName3, password);
        tradingSystemService.register(u1, password);
        tradingSystemService.register(u2, password);
        tradingSystemService.register(u3, password);
        tradingSystemService.register(u4, password);
        String u1Conn = tradingSystemService.connect();
        tradingSystemService.login(u1Conn, u1, password);
        String s1Store = tradingSystemService.openNewStore(u1Conn, "S1");
        tradingSystemService.addProductToStore(u1Conn, s1Store, "Wine", "drinks", "alcoholic drinks", 40, 35);
        tradingSystemService.appointStoreOwner(u1Conn, u4, s1Store);
        String connId1 = tradingSystemService.connect();
        tradingSystemService.login(connId1, userName1, password);
        String storeId1 = tradingSystemService.openNewStore(connId1, "eBay");
        tradingSystemService.appointStoreOwner(connId1, userName2, storeId1);
        String itemId1 = tradingSystemService.addProductToStore(connId1, storeId1, "Bamba", "snacks", "yummy", 20, 30.0);
        tradingSystemService.logout(connId1);
        String connId2 = tradingSystemService.connect();
        tradingSystemService.login(connId2, userName2, password);
        tradingSystemService.appointStoreOwner(connId2, userName3, storeId1);
        String itemId2 = tradingSystemService.addProductToStore(connId2, storeId1, "Chips", "snacks", "yummy", 50, 5.7);
        tradingSystemService.logout(connId2);
        String connId3 = tradingSystemService.connect();
        tradingSystemService.login(connId3, userName3, password);
        tradingSystemService.addItemToBasket(connId3, storeId1, itemId1, 10);
        tradingSystemService.addItemToBasket(connId3, storeId1, itemId2, 5);
        tradingSystemService.purchaseCart(connId3, "65376254673", 2, 2022, "Noa", "123", "763732432", "Noa", "Zabar", "Raanana", "Israel", 64357632);
        tradingSystemService.logout(connId3);
    }
}
