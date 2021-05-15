package org.game.bot;

import org.flywaydb.core.Flyway;
import org.game.bot.database.DBController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBTest {

    @Test
    void addPlayer() throws URISyntaxException {
        assertTrue(DBController.addPlayer("Pasha"));
    }
}