package notifications;

import user.Subscriber;

public class AppointRoleNotification extends Notification{

    private Subscriber assignee;
    private String role; //"owner" or "manager" only
    private int storeId;

    public AppointRoleNotification(Subscriber assignee, String role, int storeId) {
        this.assignee = assignee;
        this.role = role;
        this.storeId = storeId;
    }

    public Subscriber getAssignee() {
        return assignee;
    }

    public void setAssignee(Subscriber assignee) {
        this.assignee = assignee;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public void notifyNotification() {

    }
}
