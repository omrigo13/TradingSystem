package externalServices;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DeliverySystemTest {

    @Test
    public void testDeliver() throws Exception {
        DeliveryData deliveryData = new DeliveryData(null, null);
        DeliverySystem deliverySystem = new DeliverySystemBasicImpl();
        deliverySystem.deliver(deliveryData);
    }
}
