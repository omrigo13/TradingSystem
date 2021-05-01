package notifications;

public class StoreStatusNotification extends Notification{

    private boolean isActive;

    public StoreStatusNotification(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
