package externalServices;

import exceptions.PaymentSystemException;
import org.testng.annotations.Test;

public class PaymentSystemTest {

    @Test
    void pay() throws PaymentSystemException {
        PaymentData paymentData = new PaymentData(0, null);
        PaymentSystem paymentSystem = new PaymentSystemBasicImpl();
        paymentSystem.pay(paymentData);
    }
}
