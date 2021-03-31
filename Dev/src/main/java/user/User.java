package user;

import authentication.LoginException;
import authentication.UserAuthentication;
import permissions.Command;
import permissions.Permission;
import persistence.Carts;

import java.util.Collection;

public interface User {
    Carts getPersistence();

    String getUserName();

    void setUserName(String userName);

    void setBaskets(Collection<Basket> baskets);

    UserAuthentication getUserAuthentication();

    void login(String userName, String password) throws LoginException;

    void logout() throws LogoutGuestException;

    void changeState(State state);

    Basket getBasket(String storeID);

    Collection<Basket> getCart();

    void doCommand(Command command) throws Exception;

    void addPermission(Permission permission);

    void deletePermission(Permission permission);
}
