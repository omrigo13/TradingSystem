package notifications;

import user.Subscriber;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class AppointRoleNotification extends Notification{

    public AppointRoleNotification() {

    }

    @Override
    public String toString() {
        return "assignor " + assignor.getUserName() + " assign you to be " + role + " of storeId: " + storeId;
    }
    @ManyToOne
    private Subscriber assignor;
    private String role; //"owner" or "manager" only
    private int storeId;

    public AppointRoleNotification(Subscriber assignor, String role, int storeId) {
        this.assignor = assignor;
        this.role = role;
        this.storeId = storeId;
    }

    public Subscriber getAssignor() {
        return assignor;
    }

    public void setAssignor(Subscriber assignor) {
        this.assignor = assignor;
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

    @Override
    public String print() {
        return "assignor " + assignor.getUserName() + " assign you to be " + role + " of storeId: " + storeId;
    }
}
