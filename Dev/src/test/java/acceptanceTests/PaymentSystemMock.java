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
    public void payBack(PaymentData data) {

    }

    @Override
    public boolean pay(PaymentData data) throws PaymentSystemException {
//        if(!isSucceed)
//            throw  new PaymentSystemException();
        if(!payments.keySet().contains(data.getUsername()))
            payments.put(data.getUsername(), new LinkedList<>());
        payments.get(data.getUsername()).add(data.getPaymentValue());

        return true;
    }
}