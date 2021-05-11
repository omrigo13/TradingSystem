package externalServices;

import exceptions.PaymentSystemException;
import org.testng.annotations.Test;

public class PaymentSystemTest {

    @Test
    void pay() throws PaymentSystemException {
        PaymentData paymentData = new PaymentData("1234", 1, 2022, "a", "001", "000000018");
        PaymentSystem paymentSystem = new PaymentSystemBasicImpl();
        paymentSystem.pay(paymentData);
    }
}
