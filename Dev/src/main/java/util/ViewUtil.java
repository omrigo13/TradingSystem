package util;

import io.javalin.http.Context;
import io.javalin.http.ErrorHandler;

import java.util.HashMap;
import java.util.Map;

import static util.RequestUtil.*;

public class ViewUtil {

    public static Map<String, Object> baseModel(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageBundle(getSessionLocale(ctx)));
        model.put("currentUser", getSessionCurrentUser(ctx));
        model.put("admin", getSessionAdmin(ctx));
        model.put("notifications", getSessionNotifications(ctx));
        return model;
    }

    public static ErrorHandler NotFound = ctx -> {
        ctx.render(Path.Template.NotFound, baseModel(ctx));
    };

}
