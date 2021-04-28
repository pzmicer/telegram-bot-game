package org.game.bot.database;

import org.game.bot.database.models.GamesEntity;
import org.game.bot.database.models.PlayersEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.intellij.lang.annotations.Language;

public class DBController implements AutoCloseable {

    public boolean addPlayer(String username){
        PlayersEntity player = new PlayersEntity(username);
        try(Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            session.beginTransaction();
            session.save(player);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addGame(String leaderName,String keyWord,int associationsGuessed){
        int leaderId = getIdByName(leaderName);
        if(leaderId==-1)
            return false;
        GamesEntity game = new GamesEntity(leaderId,keyWord,associationsGuessed);
        try(Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            session.beginTransaction();
            session.save(game);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getIdByName(String leaderName){
        @Language("HQL") String queryString = "select playerId from PlayersEntity where username = '"+leaderName+"'";
        try(Session session = HibernateUtil.getSessionFactory().getCurrentSession()){
            session.beginTransaction();
            Query<Integer> query = session.createQuery(queryString,Integer.class);
            return query.getSingleResult();
        }
        catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void close() {
        HibernateUtil.shutdown();
    }

    static class HibernateUtil {
        private static SessionFactory sessionFactory;

        private static SessionFactory buildSessionFactory() {
            Configuration configuration = new Configuration();
            configuration.configure(); //"hibernate.cfg.xml"
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().configure();
            sessionFactory = configuration.buildSessionFactory(builder.build());
            return sessionFactory;
        }

        public static SessionFactory getSessionFactory() {
            if (sessionFactory == null)
                sessionFactory = buildSessionFactory();
            return sessionFactory;
        }

        public static void shutdown() {
            // Close caches and connection pools
            getSessionFactory().close();
        }
    }
}
