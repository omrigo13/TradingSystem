package externalServices;

import exceptions.PaymentSystemException;

public interface PaymentSystem {
    boolean pay(PaymentData data) throws PaymentSystemException;

    void payBack(PaymentData data);
}
