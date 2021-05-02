package externalServices;

import exceptions.PaymentSystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentSystemTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void pay() throws PaymentSystemException {
        PaymentData paymentData = new PaymentData(0, null);
        PaymentSystem paymentSystem = new PaymentSystemBasicImpl();
        paymentSystem.pay(paymentData);
    }
}