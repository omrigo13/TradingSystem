package externalServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliverySystemTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void deliver() throws Exception {
        DeliveryData deliveryData = new DeliveryData(null, null);
        DeliverySystem deliverySystem = new DeliverySystemBasicImpl();
        deliverySystem.deliver(deliveryData);
    }
}