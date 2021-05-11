package externalServices;

import exceptions.PaymentSystemException;

public interface PaymentSystem {

    void connect() throws PaymentSystemException;

    void pay(PaymentData data) throws PaymentSystemException;

    void cancel(PaymentData data) throws PaymentSystemException;
}
