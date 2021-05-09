package notifications;

public class StoreStatusNotification extends Notification{

    private String storeId;
    private boolean isActive;

    public StoreStatusNotification(String storeId, boolean isActive) {
        this.storeId = storeId;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public void notifyNotification() {

    }

    @Override
    public String toString() {
        return "StoreStatusNotification{" +
                "storeId='" + storeId + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
