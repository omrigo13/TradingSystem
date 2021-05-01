package notifications;

public abstract class Notification {

    private boolean isShown = false;

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public abstract void notifyNotification();
}
