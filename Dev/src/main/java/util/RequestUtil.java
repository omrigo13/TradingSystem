package util;


import io.javalin.http.Context;

import java.util.Collection;
import java.util.LinkedList;

public class RequestUtil {

    public static String getQueryLocale(Context ctx) {
        return ctx.queryParam("locale");
    }

    public static String getQueryUsername(Context ctx) {
        return ctx.formParam("username");
    }

    public static String getKeyWord(Context ctx) {
        return ctx.formParam("keyWord");
    }

    public static String getDate(Context ctx) {
        String [] date = ctx.formParam("date").split("-");
        String result = date[2] + "/" + date[1] + "/" + date[0];
        return result;
    }

    public static Double getRatingItem(Context ctx) {
        String num = ctx.formParam("ratingItem");
        if(!num.equals(""))
            return Double.parseDouble(ctx.formParam("ratingItem"));
        return null;
    }

    public static Double getRatingStore(Context ctx) {
        String num = ctx.formParam("ratingStore");
        if(!num.equals(""))
            return Double.parseDouble(ctx.formParam("ratingStore"));
        return null;
    }

    public static int getMinQuantity(Context ctx) {
        return Integer.parseInt(ctx.formParam("minQuantity"));
    }

    public static int getMaxQuantity(Context ctx) {
        return Integer.parseInt(ctx.formParam("maxQuantity"));
    }

    public static Collection<String> getItems(Context ctx) {
        Collection<String> items = ctx.formParams("items").subList(0, ctx.formParams("items").size());
        Collection<String> result = new LinkedList<>();
        for (String item: items) {
            result.add(item.split("id:")[1].split(" ")[0]);
        }
        return result;
    }

    public static Double getMaxPrice(Context ctx) {
        String num = ctx.formParam("maxPrice");
        if(!num.equals(""))
            return Double.parseDouble(ctx.formParam("maxPrice"));
        return null;
    }

    public static String getTime(Context ctx) {
        return ctx.formParam("time");
    }

    public static Double getMinPrice(Context ctx) {
        String num = ctx.formParam("minPrice");
        if(!num.equals(""))
            return Double.parseDouble(ctx.formParam("minPrice"));
        return null;
    }

    public static String getConnectionID(Context ctx) {
        return ctx.formParam("connectionID");
    }

    public static String getConnectionIDLogout(Context ctx) {
        return ctx.formParam("connectionIDLogout");
    }

    public static String getQueryPassword(Context ctx) {
        return ctx.formParam("password");
    }

    public static String getStoreID(Context ctx) {
        return ctx.formParam("storeID");
    }

    public static String getDesc(Context ctx) {
        return ctx.formParam("desc");
    }

    public static int getDiscount(Context ctx) {
        return Integer.parseInt(ctx.formParam("discount"));
    }

    public static String getProductID(Context ctx) {
        return ctx.formParam("productID");
    }

    public static String getProduceName(Context ctx) {
            return ctx.formParam("productName");
    }

    public static String getCategory(Context ctx) {
        return ctx.formParam("category");
    }

    public static String getSubCategory(Context ctx) {
        return ctx.formParam("subCategory");
    }

    public static String getManagerUserName(Context ctx) {
        return ctx.formParam("managerUserName");
    }

    public static Double getPrice(Context ctx) {
        return Double.parseDouble(ctx.formParam("price"));
    }

    public static String getSearchBox(Context ctx) {
        return ctx.formParam("searchBox");
    }

    public static int getAmount(Context ctx) {
        return Integer.parseInt(ctx.formParam("amount"));
    }

    public static int getPolicyID(Context ctx) {
        return Integer.parseInt(ctx.formParam("policyID"));
    }

    public static int getPolicyID2(Context ctx) {
        return Integer.parseInt(ctx.formParam("policyID2"));
    }

    public static String getQueryLoginRedirect(Context ctx) {
        return ctx.queryParam("loginRedirect");
    }

    public static String getSessionLocale(Context ctx) {
        return (String) ctx.sessionAttribute("locale");
    }
    public static String getStoreName(Context ctx) {
        return ctx.formParam("storeName");
    }

    public static String getSessionCurrentUser(Context ctx) {
        return (String) ctx.sessionAttribute("currentUser");
    }

    public static String getSessionAdmin(Context ctx) {
        return (String) ctx.sessionAttribute("admin");
    }

    public static boolean removeSessionAttrLoggedOut(Context ctx) {
        String loggedOut = ctx.sessionAttribute("loggedOut");
        ctx.sessionAttribute("loggedOut", null);
        return loggedOut != null;
    }

    public static String removeSessionAttrLoginRedirect(Context ctx) {
        String loginRedirect = ctx.sessionAttribute("loginRedirect");
        ctx.sessionAttribute("loginRedirect", null);
        return loginRedirect;
    }

}
