package store;

public class WrongCategoryException extends ItemException{
    private String msg;

    public WrongCategoryException(String msg) {
        this.msg = msg;
    }
}
