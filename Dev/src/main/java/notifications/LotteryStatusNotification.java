package notifications;

import javax.persistence.Entity;

@Entity
public class LotteryStatusNotification extends Notification{
    @Override
    public void notifyNotification() {

    }

    @Override
    public String print() {
        return null;
    }


}
