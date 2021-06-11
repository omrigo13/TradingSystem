package notifications;

import user.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class MessageNotification extends Notification{
    @Transient
    private User sender;
    private String message;

    public MessageNotification(User sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public MessageNotification() {

    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void notifyNotification() {

    }

    @Override
    public String toString() {
        return "MessageNotification{" +
                "sender=" + sender +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public String print() {
        return null;
    }
}
