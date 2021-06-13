package config_Tests;

import exceptions.InvalidActionException;
import main.Config;
import main.Main;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import persistence.RepoMock;

import static org.testng.Assert.assertThrows;


public class ConfigTests {

    @BeforeClass
    public void beforeClass() {
        RepoMock.enable();
    }

    public static void main(String[] args) throws InvalidActionException, ClassNotFoundException {
        badScript1();

        badScript2();


    }

    /**
     subscriber who is not the store owner and has no permissions to the store, tries to appoint an owner to the store.
     **/
    @Test
    static void badScript1() throws ClassNotFoundException, InvalidActionException {
        Config cfg = new Config();

        cfg.adminName = "Admin";
        cfg.adminPassword = "123";
        cfg.port = 80;
        cfg.sslPort = 443;
        cfg.stateFileAddress = "Dev/config/BadConfigScript1.java";
        cfg.startupScript = "BadConfigScript1";
        cfg.paymentSystem = "externalServices.PaymentSystemMock";
        cfg.deliverySystem = "externalServices.DeliverySystemMock";
        assertThrows(RuntimeException.class, ()-> Main.run(cfg, 0));

    }

    /**
     trying to add to basket an item which the store does not have.
     **/
    @Test
    static void badScript2() throws ClassNotFoundException, InvalidActionException {
        Config cfg = new Config();

        cfg.adminName = "Admin";
        cfg.adminPassword = "123";
        cfg.port = 80;
        cfg.sslPort = 443;
        cfg.stateFileAddress = "Dev/config/BadConfigScript2.java";
        cfg.startupScript = "BadConfigScript2";
        cfg.paymentSystem = "externalServices.PaymentSystemMock";
        cfg.deliverySystem = "externalServices.DeliverySystemMock";
        assertThrows(RuntimeException.class, ()-> Main.run(cfg, 0));

    }


}
