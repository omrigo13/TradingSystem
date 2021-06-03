package notifications;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public abstract class Notification {

    private boolean isShown = false;
    @Id
    @GeneratedValue
    private Integer id;

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public abstract void notifyNotification();

    @Override
    public String toString() {
        return "Notification{" +
                "isShown=" + isShown +
                '}';
    }

    public abstract String print();

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
