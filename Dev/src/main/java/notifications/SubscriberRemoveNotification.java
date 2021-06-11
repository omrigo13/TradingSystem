package notifications;

import user.Subscriber;

import javax.persistence.Entity;

@Entity
public class SubscriberRemoveNotification extends Notification{
    @Override
    public void notifyNotification() {


    }

    @Override
    public String print() {
        return null;
    }
}
