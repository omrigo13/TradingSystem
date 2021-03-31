package persistence;

import authentication.LoginException;
import authentication.UserAuthentication;
import permissions.Command;
import permissions.Permission;
import user.Basket;
import user.LogoutGuestException;
import user.State;
import user.User;

import java.util.Collection;

public class UserMock implements User {
    private String userName;
    private Collection<Basket> baskets;

    public UserMock(String userName){
        this.userName = userName;
    }

    @Override
    public Carts getPersistence() {
        return null;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public void setUserName(String userName) {

    }

    @Override
    public void setBaskets(Collection<Basket> baskets) {
        this.baskets = baskets;
    }

    @Override
    public UserAuthentication getUserAuthentication() {
        return null;
    }

    @Override
    public void login(String userName, String password) {

    }

    @Override
    public void logout() {

    }

    @Override
    public void changeState(State state) {

    }

    @Override
    public Basket getBasket(String storeID) {
        return null;
    }

    @Override
    public Collection<Basket> getCart() {
        return this.baskets;
    }

    @Override
    public void doCommand(Command command) throws Exception {

    }

    @Override
    public void addPermission(Permission permission) {

    }

    @Override
    public void deletePermission(Permission permission) {

    }

    @Override
    public boolean havePermission(Permission permission) {
        return false;
    }
}
