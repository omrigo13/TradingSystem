package persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Repo {

    private static Repo repo_instance = null;
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");
    private static EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
    private Repo() {
    }

    public static Repo getInstance(){
        if(repo_instance == null)
            repo_instance = new Repo();
        return repo_instance;
    }

    public static EntityManager getEm() {
        return em;
    }
}
