package org.game.bot.database;

import org.game.bot.database.models.GamesEntity;
import org.game.bot.database.models.PlayersEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.intellij.lang.annotations.Language;

import javax.persistence.NoResultException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBController {

    private static boolean addPlayer(String playerName) {
        PlayersEntity player = new PlayersEntity(playerName);
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.save(player);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean updatePlayer(String playerName) {
        int id = getIdByName(playerName);
        if (id == -1)
            return addPlayer(playerName);
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            PlayersEntity player = session.get(PlayersEntity.class, id);
            player.setGamesFinished(player.getGamesFinished() + 1);
            session.update(player);
            session.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean addGame(String leaderName, String keyWord, int associationsGuessed) {
        int leaderId = getIdByName(leaderName);
        if (leaderId == -1)
            return false;
        GamesEntity game = new GamesEntity(leaderId, keyWord, associationsGuessed);
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.save(game);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<PlayersEntity> getPlayersList(int nPlayers) {
        @Language("HQL") String queryString = """
                from PlayersEntity
                order by gamesFinished desc
                """;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            return session.createQuery(queryString,PlayersEntity.class).setMaxResults(nPlayers).list();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<GamesEntity> getGamesList(int nGames){
        @Language("HQL") String queryString = """
                from GamesEntity
                order by associationsGuessed desc
                """;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            session.beginTransaction();
            return session.createQuery(queryString,GamesEntity.class).setMaxResults(nGames)
                    .list();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getIdByName(String playerName) {
        @Language("HQL") String queryString = "select playerId from PlayersEntity where username = '" + playerName + "'";
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            Query<Integer> query = session.createQuery(queryString, Integer.class);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean containsPlayer(String playerName) {
        return getIdByName(playerName) != -1;
    }

    public static void shutdown() {
      //  HibernateUtil.shutdown();
    }

    public static SessionFactory getSessionFactory() throws URISyntaxException {
        return HibernateUtil.getSessionFactory();
    }

    private static class HibernateUtil {
        static SessionFactory sessionFactory;

        static SessionFactory buildSessionFactory() throws URISyntaxException {
            Configuration configuration = new Configuration();
            Map<String,String> info = getInfo();
            for(var t : info.entrySet())
                configuration.setProperty(t.getKey(),t.getValue());


            configuration.configure(); //"hibernate.cfg.xml"
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).configure();
            sessionFactory = configuration.buildSessionFactory(builder.build());
            return sessionFactory;
        }

        static Map<String,String> getInfo() throws URISyntaxException {
            Map<String,String> map = new HashMap<>();
            URI dbUri = new URI(System.getenv("HEROKU_POSTGRESQL_BROWN_URL"));
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
            map.put("hibernate.connection.driver_class","org.postgresql.Driver");
            map.put("hibernate.connection.url",dbUrl);
            map.put("hibernate.connection.username",username);
            map.put("hibernate.connection.password",password);
            map.put("hibernate.current_session_context_class","thread");
           // map.put("hibernate.hmb2ddl.auto","create");
            return map;
        }

        static SessionFactory getSessionFactory() throws URISyntaxException {
            if (sessionFactory == null)
                sessionFactory = buildSessionFactory();
            return sessionFactory;
        }

        /*static void shutdown() {
            // Close caches and connection pools
            getSessionFactory().close();
        }*/
    }
}
