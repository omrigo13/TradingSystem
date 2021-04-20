package externalServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliverySystemTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void deliver() {
        DeliveryData deliveryData = new DeliveryData();
        DeliverySystem deliverySystem = new DeliverySystemBasicImpl();
        deliverySystem.deliver(deliveryData);
    }
}