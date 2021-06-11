package persistence;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import store.Item;

public class DatabaseConfigBuilder {
    public Configuration buildConfiguration() {
        Configuration configuration = new Configuration();

        configuration
                .setProperty(Environment.URL, "jdbc:mysql://localhost:3306/ts")
                .setProperty(Environment.USER, "root")
                .setProperty(Environment.PASS, "1234")

                .setProperty(Environment.SHOW_SQL, "false")
                .setProperty(Environment.GLOBALLY_QUOTED_IDENTIFIERS, "true")
                .setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect")
                .setProperty(Environment.DRIVER, "com.mysql.cj.jdbc.Driver")
                .setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

        configuration
                .addAnnotatedClass(Item.class); //todo

        return configuration;
    }
}
