package acceptanceTests;

import exceptions.PaymentSystemException;
import externalServices.PaymentData;
import externalServices.PaymentSystem;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class PaymentSystemMock implements PaymentSystem {

//    private double amount; //last price purchased
//    private boolean isSucceed;
//    private Collection<String> payments = new LinkedList<>(); //list of usernames
    private HashMap<String, LinkedList<Double>> payments = new HashMap<>();

//    public void setSucceed(boolean succeed) {
//        isSucceed = succeed;
//    }


    public HashMap<String, LinkedList<Double>> getPayments() {
        return payments;
    }


    @Override
    public boolean connect() throws PaymentSystemException {
        return false;
    }

    @Override
    public boolean pay(PaymentData data) throws PaymentSystemException {
//        if(!isSucceed)
//            throw  new PaymentSystemException();
        if(!payments.keySet().contains(data.getId()))
            payments.put(data.getId(), new LinkedList<>());
        payments.get(data.getId()).add(data.getPaymentValue());

        return true;
    }

    @Override
    public boolean cancel(PaymentData data) throws PaymentSystemException {
        return false;
    }
}