package org.game.bot;
import org.flywaydb.core.Flyway;
import org.game.bot.database.DBController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBTest {
    private static Flyway flyway;
    private static DBController dbController;

    @BeforeAll
    static void initialize() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("database.properties"));
            flyway = Flyway.configure().dataSource(
                    properties.getProperty("url"),
                    properties.getProperty("user"),
                    properties.getProperty("password")).load();
            flyway.migrate();
            dbController = new DBController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addPlayer() {
        assertTrue(dbController.addPlayer("Dima"));
    }

    @Test
    void addGame() {
        assertTrue(dbController.addGame("Nancy","kj",3));
    }

    @AfterAll
    static void shutDown() {
        dbController.close();
        flyway.clean();
    }
}
