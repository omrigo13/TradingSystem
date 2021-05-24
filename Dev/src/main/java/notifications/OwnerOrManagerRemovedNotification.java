package notifications;

import user.Subscriber;

public class OwnerOrManagerRemovedNotification extends Notification{

    private Subscriber remover;
    private int storeId;


    public Subscriber getRemover() {
        return remover;
    }

    public void setRemover(Subscriber remover) {
        this.remover = remover;
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
    public String toString() {
        return "OwnerRemovedNotification{" +
                "remover=" + remover +
                ", storeId='" + storeId + '\'' +
                '}';
    }

    @Override
    public String print() {
        return "OwnerRemovedNotification{" +
                "remover=" + remover.getUserName() +
                ", storeId='" + storeId + '\'' +
                '}';
    }
}
