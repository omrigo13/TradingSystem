package externalServices;

import exceptions.PaymentSystemException;

public interface PaymentSystem {

    boolean connect() throws PaymentSystemException;

    boolean pay(PaymentData data) throws PaymentSystemException;

    boolean cancel(PaymentData data) throws PaymentSystemException;
}
