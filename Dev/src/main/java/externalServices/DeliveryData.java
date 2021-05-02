package externalServices;

public class DeliveryData {

    private String username;
    private String address;

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public DeliveryData(String username, String address) {
        this.username = username;
        this.address = address;
    }
}
