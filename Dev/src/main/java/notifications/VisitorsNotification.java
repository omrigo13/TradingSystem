package notifications;

import org.json.JSONObject;
import java.util.Map;

public class VisitorsNotification extends Notification {

    private Map<String, Integer> visitors;

    public VisitorsNotification(Map<String, Integer> visitors) {
        this.visitors = visitors;
    }

    @Override
    public void notifyNotification() {

    }

    @Override
    public String print() {
        return new JSONObject(this.visitors).toString();
    }
}
