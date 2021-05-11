package externalServices;

import exceptions.PaymentSystemException;

public class PaymentSystemBasicImpl implements PaymentSystem {

    @Override
    public boolean connect() throws PaymentSystemException {
        return false;
    }

    @Override
    public boolean pay(PaymentData data) {
        return true;
    }

    @Override
    public boolean cancel(PaymentData data) throws PaymentSystemException {
        return false;
    }
}
