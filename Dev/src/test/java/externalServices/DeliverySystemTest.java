package externalServices;

import exceptions.DeliverySystemException;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

public class DeliverySystemTest {

    private DeliverySystem deliverySystem;

    @BeforeMethod
    void setUp() throws DeliverySystemException {
        MockitoAnnotations.openMocks(this);
        deliverySystem = new DeliverySystemBasicImpl();
    }

    @Test
    void connectDeliverySystem() throws DeliverySystemException {
        deliverySystem.connect();
    }
    @Test
    void pay() throws DeliverySystemException {
        int tId;
        DeliveryData deliveryData = new DeliveryData("name", "address", "city" , "country", 12345);
        tId = deliveryData.getTransactionId();
        assertEquals( 0, tId);
        assertFalse(deliveryData.isDelivered());
        deliverySystem.deliver(deliveryData);
        tId = deliveryData.getTransactionId();
        assertTrue( tId >= 10000 && tId <= 100000);
        assertTrue(deliveryData.isDelivered());
    }

    @Test
    void cancel() throws DeliverySystemException {
        int tId;
        DeliveryData deliveryData = new DeliveryData("name", "address", "city" , "country", 12345);
        tId = deliveryData.getTransactionId();
        assertEquals( 0, tId);
        deliverySystem.cancel(deliveryData);
        assertFalse(deliveryData.isDelivered());
    }

    @Test
    void payAndCancel() throws DeliverySystemException {
        int tId;
        DeliveryData deliveryData = new DeliveryData("name", "address", "city" , "country", 12345);
        assertFalse(deliveryData.isDelivered());
        deliverySystem.deliver(deliveryData);
        tId = deliveryData.getTransactionId();
        assertTrue(deliveryData.isDelivered());
        deliverySystem.cancel(deliveryData);
        assertTrue( tId >= 10000 && tId <= 100000);
        assertFalse(deliveryData.isDelivered());
    }
}
