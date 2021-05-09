package notifications;

import user.User;

public class MessageNotification extends Notification{
    private User sender;
    private String message;

    public MessageNotification(User sender, String message) {
        this.sender = sender;
        this.message = message;
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
}
