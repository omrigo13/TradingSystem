package permissions;

import store.Store;

public abstract class Command {
    protected final Store store;


    public Command(Store store) {
        this.store = store;
    }

    public Store getStore(){
        return this.store;
    }

    public abstract void doCommand() throws Exception;
}
