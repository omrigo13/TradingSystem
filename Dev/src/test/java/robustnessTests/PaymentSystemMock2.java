package robustnessTests;

import exceptions.PaymentSystemException;
import externalServices.PaymentData;
import externalServices.PaymentSystem;

import java.util.HashMap;
import java.util.LinkedList;

public class PaymentSystemMock2 implements PaymentSystem {

    private HashMap<String, LinkedList<Double>> payments = new HashMap<>();
    public HashMap<String, LinkedList<Double>> getPayments() {
        return payments;
    }


    @Override
    public void connect() throws PaymentSystemException {
        throw new PaymentSystemException();
    }

    @Override
    public void pay(PaymentData data) throws PaymentSystemException {
        throw new PaymentSystemException();

//        if(!payments.keySet().contains(data.getId()))
//            payments.put(data.getId(), new LinkedList<>());
//        payments.get(data.getId()).add(data.getPaymentValue());
    }

    @Override
    public void cancel(PaymentData data) throws PaymentSystemException {
        throw new PaymentSystemException();
    }
}
