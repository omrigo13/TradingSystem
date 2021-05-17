package externalServices;

public class DeliveryData {

    private final String name;
    private final String address;
    private final String city;
    private final String country;
    private final int zip;
    private int transactionId;
    private boolean delivered;

    public DeliveryData() { //TODO should be removed after fix purchase cart
        name = "";
        address = "";
        city = "";
        country = "";
        zip = 0;
    }

    public DeliveryData(String name, String address, String city, String country, int zip) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.zip = zip;
        this.delivered = false;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() { return city; }

    public String getCountry() { return country; }

    public int getZip() { return zip; }

    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getTransactionId() { return transactionId; }

    public void setDelivered() {this.delivered = true; }

    public void setNotDelivered() {this.delivered = false; }

    public boolean isDelivered() { return this.delivered; }
}
