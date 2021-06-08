package externalServices;

import exceptions.PaymentSystemException;

public class PaymentSystemFake implements PaymentSystem {

    private boolean fake;

    public PaymentSystemFake(){
        fake = false;
    }

    @Override
    public void connect() throws PaymentSystemException { }

    @Override
    public void pay(PaymentData data) throws PaymentSystemException {
        if(fake)
            throw new PaymentSystemException();
    }

    @Override
    public void cancel(PaymentData data) throws PaymentSystemException { }

    public void setFake(boolean fake) {this.fake = fake;}
}
