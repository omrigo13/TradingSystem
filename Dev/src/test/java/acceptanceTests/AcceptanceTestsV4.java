package acceptanceTests;

import exceptions.InvalidActionException;
import exceptions.NoPermissionException;
import exceptions.NotLoggedInException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import persistence.RepoMock;
import service.TradingSystemService;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class AcceptanceTestsV4 {
    private static TradingSystemService service;

    private String admin1Id, founderStore1Id, store1Manager1Id, subs1Id, guest1Id;
    private String storeId1;
    private String store1FounderUserName = "store1FounderUserName", store1Manager1UserName = "Store1Manager1UserName", subs1UserName = "subs1UserName";
    private String date;

    @BeforeClass
    public void beforeClass() {
        RepoMock.enable();
    }

    @BeforeMethod
    void setUp() throws InvalidActionException {
        service = Driver.getService("Admin1", "ad123"); //params are details of system manager to register into user authenticator
        admin1Id = service.connect();
        service.login(admin1Id, "Admin1", "ad123");
        date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    void setUpGuest() throws InvalidActionException {
        guest1Id = service.connect();
    }

    void setUpSubscriber1() throws InvalidActionException {
        subs1Id = service.connect();
        service.register("subs1UserName", "1234");
        service.login(subs1Id, "subs1UserName", "1234");
    }

    void setUpStore1Founder() throws InvalidActionException {
        founderStore1Id = service.connect();
        service.register("store1FounderUserName", "1234");
        service.login(founderStore1Id, "store1FounderUserName", "1234"); //storeId1 founder
    }

    void setUpStore1Manager() throws InvalidActionException {
        store1Manager1Id = service.connect();
        service.register("Store1Manager1UserName", "1234");
        service.login(store1Manager1Id, "Store1Manager1UserName", "1234");
        service.appointStoreManager(founderStore1Id, store1Manager1UserName, storeId1);
    }

    void setUpStore1() throws InvalidActionException {
        setUpStore1Founder();
        storeId1 = service.openNewStore(founderStore1Id, "store1");
        setUpStore1Manager();
    }

    @Test
    void get_visitors_by_guest_subscriber_owner_manager() throws InvalidActionException {
        setUpGuest();
        setUpSubscriber1();
        setUpStore1();

        assertThrows(NotLoggedInException.class, () -> service.getTotalVisitorsByAdminPerDay(guest1Id, date));
        assertThrows(NoPermissionException.class, () -> service.getTotalVisitorsByAdminPerDay(subs1Id, date));
        assertThrows(NoPermissionException.class, () -> service.getTotalVisitorsByAdminPerDay(store1Manager1Id, date));
        assertThrows(NoPermissionException.class, () -> service.getTotalVisitorsByAdminPerDay(founderStore1Id, date));
    }

    @Test
    void get_visitors_by_admin() throws InvalidActionException {
        assertEquals("{subscribers=0, guests=1, owners=0, managers=0, admins=1}", service.getTotalVisitorsByAdminPerDay(admin1Id, date).toString());
    }

    @Test
    void add_1_guest_get_visitors_by_admin() throws InvalidActionException {
        service.connect();
        assertEquals("{subscribers=0, guests=2, owners=0, managers=0, admins=1}", service.getTotalVisitorsByAdminPerDay(admin1Id, date).toString());
    }

    @Test
    void add_many_guest_get_visitors_by_admin() throws InvalidActionException {
        for(int i = 0; i < 10; i++)
            service.connect();
        assertEquals("{subscribers=0, guests=11, owners=0, managers=0, admins=1}", service.getTotalVisitorsByAdminPerDay(admin1Id, date).toString());
    }

    @Test
    void add_1_subscriber_get_visitors_by_admin() throws InvalidActionException {
        setUpSubscriber1();
        assertEquals("{subscribers=1, guests=2, owners=0, managers=0, admins=1}", service.getTotalVisitorsByAdminPerDay(admin1Id, date).toString());
    }

    @Test
    void logout_login_subscriber_get_visitors_by_admin() throws InvalidActionException {
        setUpSubscriber1();
        service.logout(subs1Id);
        service.login(subs1Id, subs1UserName, "1234");
        assertEquals("{subscribers=2, guests=2, owners=0, managers=0, admins=1}", service.getTotalVisitorsByAdminPerDay(admin1Id, date).toString());
    }

    @Test
    void add_1_owner_get_visitors_by_admin() throws InvalidActionException {
        setUpStore1();
        service.login(founderStore1Id, store1FounderUserName, "1234");
        assertEquals("{subscribers=2, guests=3, owners=1, managers=0, admins=1}", service.getTotalVisitorsByAdminPerDay(admin1Id, date).toString());
    }

    @Test
    void logout_login_owner_get_visitors_by_admin() throws InvalidActionException {
        setUpStore1();
        service.login(founderStore1Id, store1FounderUserName, "1234");
        service.logout(founderStore1Id);
        service.login(founderStore1Id, store1FounderUserName, "1234");
        assertEquals("{subscribers=2, guests=3, owners=2, managers=0, admins=1}", service.getTotalVisitorsByAdminPerDay(admin1Id, date).toString());
    }

    @Test
    void add_1_manager_get_visitors_by_admin() throws InvalidActionException {
        setUpStore1();
        service.login(store1Manager1Id, store1Manager1UserName, "1234");
        assertEquals("{subscribers=2, guests=3, owners=0, managers=1, admins=1}", service.getTotalVisitorsByAdminPerDay(admin1Id, date).toString());
    }

    @Test
    void logout_login_manager_get_visitors_by_admin() throws InvalidActionException {
        setUpStore1();
        service.login(store1Manager1Id, store1Manager1UserName, "1234");
        service.logout(store1Manager1Id);
        service.login(store1Manager1Id, store1Manager1UserName, "1234");
        assertEquals("{subscribers=2, guests=3, owners=0, managers=2, admins=1}", service.getTotalVisitorsByAdminPerDay(admin1Id, date).toString());
    }

    @Test
    void logout_login_admin_get_visitors_by_admin() throws InvalidActionException {
        service.logout(admin1Id);
        service.login(admin1Id, "Admin1", "ad123");
        assertEquals("{subscribers=0, guests=1, owners=0, managers=0, admins=2}", service.getTotalVisitorsByAdminPerDay(admin1Id, date).toString());
    }

    @Test
    void multiple_logout_login_get_visitors_by_admin() throws InvalidActionException {
        setUpStore1();
        setUpSubscriber1();
        for(int i = 0; i < 10; i++)
            service.connect();
        for(int i = 0; i < 5; i++) {
            service.logout(subs1Id);
            service.login(subs1Id, subs1UserName, "1234");
        }
        for(int i = 0; i < 7; i++) {
            service.login(store1Manager1Id, store1Manager1UserName, "1234");
            service.logout(store1Manager1Id);
            service.login(store1Manager1Id, store1Manager1UserName, "1234");
        }
        for(int i = 0; i < 9; i++) {
            service.login(founderStore1Id, store1FounderUserName, "1234");
            service.logout(founderStore1Id);
            service.login(founderStore1Id, store1FounderUserName, "1234");
        }
        for(int i = 0; i < 6; i++) {
            service.logout(admin1Id);
            service.login(admin1Id, "Admin1", "ad123");
        }
        assertEquals("{subscribers=8, guests=14, owners=18, managers=14, admins=7}", service.getTotalVisitorsByAdminPerDay(admin1Id, date).toString());
    }
}
