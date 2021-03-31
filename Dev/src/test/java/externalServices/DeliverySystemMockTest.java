package externalServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliverySystemMockTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void deliver() {
        DeliveryData deliveryData = new DeliveryData();
        DeliverySystem deliverySystem = new DeliverySystemMock();
        deliverySystem.deliver(deliveryData);
    }
}