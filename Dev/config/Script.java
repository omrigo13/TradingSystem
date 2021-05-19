import exceptions.InvalidActionException;
import service.TradingSystemService;

public class Script {

    @SuppressWarnings("unused")
    public static void run(TradingSystemService tradingSystemService){
        try {
            String userName = "Barak";
            String password = "123";
            tradingSystemService.register(userName, password);
            String connId = tradingSystemService.connect();
            tradingSystemService.login(connId, userName, password);
            String storeId = tradingSystemService.openNewStore(connId, "eBabe");
            tradingSystemService.addProductToStore(connId, storeId, "Cake", "Food", "Yummy", 10, 20.0);
            tradingSystemService.logout(connId);
            System.out.println("good register");
        } catch (InvalidActionException e) {
            System.out.println("bad register");
        }
    }
}
