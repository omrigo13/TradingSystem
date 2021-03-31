package externalServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentSystemMockTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void pay() {
        PaymentData paymentData = new PaymentData();
        PaymentSystem paymentSystem = new PaymentSystemMock();
        paymentSystem.pay(paymentData);
    }
}