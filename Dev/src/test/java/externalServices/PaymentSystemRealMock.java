package externalServices;

import exceptions.PaymentSystemException;

public class PaymentSystemRealMock implements PaymentSystem {

    private PaymentSystemBasicImpl paymentSystem;
    private long startTime;
    private long endTime;
    private boolean fake;

    public PaymentSystemRealMock() throws PaymentSystemException {
        paymentSystem = new PaymentSystemBasicImpl();
        fake = false;
    }

    @Override
    public void connect() throws PaymentSystemException {
        startTime = System.nanoTime();
    }

    @Override
    public void pay(PaymentData data) throws PaymentSystemException {
        if(fake)
            throw new PaymentSystemException();
        paymentSystem.pay(data);
        endTime = System.nanoTime();
    }

    @Override
    public void cancel(PaymentData data) throws PaymentSystemException {
        if(!fake) {
            paymentSystem.cancel(data);
            endTime = System.nanoTime();
        }
    }

    public long getTime() { return (this.endTime - this.startTime) / 1000000; }

    public void setFake(boolean fake) {this.fake = fake;}
}
