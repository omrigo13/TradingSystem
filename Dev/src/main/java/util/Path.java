package util;

public class Path {

    public static class Web {
        public static final String HOME = "/layout";
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
        public static final String LOGOUT = "/logout";
        public static final String PURCHASE = "/purchase";
        public static final String SHOWBASKET = "/showBasket";
        public static final String UPDATEPRODUCTAMOUNTINBASKET = "/updateProductAmountInBasket";
        public static final String CART = "/cart";
        public static final String NotFound = "/notfound";
        public static final String ROOT = "/";
        //public static final String BOOKS = "/books";
        //public static final String ONE_BOOK = "/books/:isbn";
    }

    public static class Template {
        public static final String HOME = "/velocity/layout.vm";
        public static final String LOGIN = "/velocity/login/login.vm";
        public static final String REGISTER = "/velocity/register/register.vm";
        //public static final String BOOKS_ALL = "/velocity/book/all.vm";
        //public static final String BOOKS_ONE = "/velocity/book/one.vm";
        public static final String NotFound = "/velocity/notfound.vm";
        public static final String ROOT = "/velocity/root.vm";
        public static final String Cart = "/velocity/cart/cart.vm";
        public static final String SHOWBASKET = "/velocity/cart/showBasket.vm";
        public static final String UPDATEPRODUCTAMOUNTINBASKET = "velocity/cart/updateProductAmountInBasket.vm";
        public static final String INVALID_CONNECTION = "/velocity/invalidConnection.vm";
    }
}
