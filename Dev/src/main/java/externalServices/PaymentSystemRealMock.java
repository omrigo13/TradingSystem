package externalServices;

import exceptions.PaymentSystemException;

public class PaymentSystemRealMock implements PaymentSystem {

    private PaymentSystemBasicImpl paymentSystem;
    private long startTime;
    private long endTime;

    public PaymentSystemRealMock() throws PaymentSystemException {
        paymentSystem = new PaymentSystemBasicImpl();
        startTime = System.nanoTime();
    }

    @Override
    public void connect() throws PaymentSystemException {
        paymentSystem.connect();
    }

    @Override
    public void pay(PaymentData data) throws PaymentSystemException {
        paymentSystem.pay(data);
        endTime = System.nanoTime();
    }

    @Override
    public void cancel(PaymentData data) throws PaymentSystemException {
        paymentSystem.cancel(data);
        endTime = System.nanoTime();
    }

    public long getTime() { return (this.endTime - this.startTime) / 1000000000; }
}
