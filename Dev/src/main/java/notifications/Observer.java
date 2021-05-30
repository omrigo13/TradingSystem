package notifications;

import java.util.Map;

public interface Observer {

    void notify(Notification notification);

    void notifyVisitors(Map<String, Integer> visitors);
}
