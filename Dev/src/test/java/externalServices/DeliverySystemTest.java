package externalServices;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DeliverySystemTest {

    @Test
    public void testDeliver() throws Exception {
        DeliveryData deliveryData = new DeliveryData("name", "address", "city" , "country", 12345);
        DeliverySystem deliverySystem = new DeliverySystemBasicImpl();
        deliverySystem.deliver(deliveryData);
    }
}
