package externalServices;

import exceptions.PaymentSystemException;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

public class PaymentSystemTest {

    private PaymentSystem paymentSystem;

    @BeforeMethod
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentSystem = new PaymentSystemBasicImpl();
    }

    @Test
    void connectPaymentSystem() throws PaymentSystemException {
        paymentSystem.connect();
    }
    @Test
    void pay() throws PaymentSystemException {
        int tId;
        PaymentData paymentData = new PaymentData("1234", 1, 2022, "a", "001", "000000018");
        tId = paymentData.getTransactionId();
        assertEquals( 0, tId);
        assertFalse(paymentData.isPaid());
        paymentSystem.pay(paymentData);
        tId = paymentData.getTransactionId();
        assertTrue( tId >= 10000 && tId <= 100000);
        assertTrue(paymentData.isPaid());
    }

    @Test
    void cancel() throws PaymentSystemException {
        int tId;
        PaymentData paymentData = new PaymentData("1234", 1, 2022, "a", "001", "000000018");
        tId = paymentData.getTransactionId();
        assertEquals( 0, tId);
        paymentSystem.cancel(paymentData);
        assertFalse(paymentData.isPaid());
    }

    @Test
    void payAndCancel() throws PaymentSystemException {
        int tId;
        PaymentData paymentData = new PaymentData("1234", 1, 2022, "a", "001", "000000018");
        assertFalse(paymentData.isPaid());
        paymentSystem.pay(paymentData);
        tId = paymentData.getTransactionId();
        assertTrue(paymentData.isPaid());
        paymentSystem.cancel(paymentData);
        assertTrue( tId >= 10000 && tId <= 100000);
        assertFalse(paymentData.isPaid());
    }
}
