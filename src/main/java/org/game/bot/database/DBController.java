package org.game.bot.database;

import org.game.bot.database.models.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class DBController {

    static final String ENV_VAR_NAME = "DATABASE_URL";

    public static boolean addPlayer(String name) throws URISyntaxException {
        SessionFactory factory = SessionFactoryContainer.getSessionFactory();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Player player = new Player();
        player.setName(name);
        try {
            session.save(player);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
            factory.close();
        }
        return true;
    }


    private static class SessionFactoryContainer {
        static SessionFactory sessionFactory;

        private static SessionFactory buildSessionFactory() throws URISyntaxException {
            Configuration configuration = new Configuration();
            Map<String, String> info = getEnvVarData();
            for (var t : info.entrySet())
                configuration.setProperty(t.getKey(), t.getValue());
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
                    applySettings(configuration.getProperties()).configure();
            return configuration.buildSessionFactory(builder.build());
        }

        private static Map<String, String> getEnvVarData() throws URISyntaxException {
            Map<String, String> map = new HashMap<>();
            URI dbUri = new URI(System.getenv(ENV_VAR_NAME));
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() +
                    dbUri.getPath() + "?sslmode=require";
            map.put("hibernate.connection.url", dbUrl);
            map.put("hibernate.connection.username", username);
            map.put("hibernate.connection.password", password);
            return map;
        }

        static SessionFactory getSessionFactory() throws URISyntaxException {
            if (sessionFactory == null)
                sessionFactory = buildSessionFactory();
            return sessionFactory;
        }
    }
}