package externalServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentSystemTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void pay() {
        PaymentData paymentData = new PaymentData(0);
        PaymentSystem paymentSystem = new PaymentSystemBasicImpl();
        paymentSystem.pay(paymentData);
    }
}