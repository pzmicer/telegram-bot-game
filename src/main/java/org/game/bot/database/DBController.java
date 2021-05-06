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
import java.util.List;

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
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            session.beginTransaction();
            return session.createQuery(queryString,PlayersEntity.class).setMaxResults(nPlayers)
                    .list();
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
        HibernateUtil.shutdown();
    }

    private static class HibernateUtil {
        static SessionFactory sessionFactory;

        static SessionFactory buildSessionFactory() {
            Configuration configuration = new Configuration();
            configuration.configure(); //"hibernate.cfg.xml"
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().configure();
            sessionFactory = configuration.buildSessionFactory(builder.build());
            return sessionFactory;
        }

        static SessionFactory getSessionFactory() {
            if (sessionFactory == null)
                sessionFactory = buildSessionFactory();
            return sessionFactory;
        }

        static void shutdown() {
            // Close caches and connection pools
            getSessionFactory().close();
        }
    }
}
