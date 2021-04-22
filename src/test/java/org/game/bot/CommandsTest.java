package org.game.bot;

import org.game.bot.commands.*;
import org.game.bot.exceptions.InvalidCommandFormatException;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.User;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommandsTest {

    @Autowired
    ReplyMessageService service;

    private static User user1;
    private static User user2;
    private static User user3;

    @BeforeAll
    static void setUser() {
        user1 = new User();
        user1.setId(1L);
        user2 = new User();
        user2.setId(2L);
        user3 = new User();
        user3.setId(3L);
    }

    @Test
    @Order(1)
    void startCommandTest() throws InvalidCommandFormatException {
        StartCommand command = new StartCommand(null);
        var result = command.execute(user1, service);
        String resultString = result.get(0).getText();
        assertEquals("Добро пожаловать. Для просмотра списка команд введите /help", resultString);
    }

    @Test
    @Order(2)
    void createRoomCommandTest() throws InvalidCommandFormatException {
        CreateRoomCommand command = new CreateRoomCommand(null);
        command.execute(user1, service);
        assertEquals(1, Room.rooms.size());
        assertEquals(user1, Room.findUser(user1).get().getValue().getUsers().get(0));
        assertFalse(Room.findUser(user1).get().getValue().isInGame());
    }

    @Test
    @Order(3)
    void exitCommandTest() throws InvalidCommandFormatException {
        ExitCommand command = new ExitCommand(null);
        command.execute(user1, service);
        assertFalse(Room.findUser(user1).isPresent());
    }

    @Test
    @Order(4)
    void joinCommandTest() throws InvalidCommandFormatException {
        CreateRoomCommand createCommand = new CreateRoomCommand(null);
        createCommand.execute(user1, service);
        String key = Room.findUser(user1).get().getKey();
        JoinCommand command = new JoinCommand(key);
        command.execute(user2, service);
        command.execute(user3, service);
        assertEquals(3, Room.rooms.get(key).getUsers().size());
    }

    @Test
    @Order(5)
    void startGameCommandTest() throws InvalidCommandFormatException {
        String key = Room.findUser(user1).get().getKey();
        JoinCommand command = new JoinCommand(key);

        StartGameCommand startGameCommand = new StartGameCommand(null);
        startGameCommand.execute(user2, service);
        assertNotNull(Room.rooms.get(key).getLeader());
        assertTrue(Room.rooms.get(key).isInGame());
        assertEquals(0, Room.rooms.get(key).getCurrentLetterIndex());
    }

    @Test
    @Order(6)
    void setKeywordCommandTest() throws InvalidCommandFormatException {
        String key = Room.findUser(user1).get().getKey();
        User leader = Room.rooms.get(key).getLeader();

        String word = "абвгдэйка";
        SetKeywordCommand setKeywordCommand = new SetKeywordCommand(word);
        setKeywordCommand.execute(leader, service);
        assertEquals(word, Room.rooms.get(key).getKeyword());
    }

    @Test
    @Order(7)
    void makeAssociationCommandTest() throws InvalidCommandFormatException {
        String key = Room.findUser(user1).get().getKey();
        User leader = Room.rooms.get(key).getLeader();

        User notLeader;
        if (user1 != leader)
            notLeader = user1;
        else
            notLeader = user2;

        assertEquals(0, Room.rooms.get(key).getAssociations().size());
        new MakeAssociationCommand("абц майонез такой").execute(notLeader, service);
        assertEquals(1, Room.rooms.get(key).getAssociations().size());
        new MakeAssociationCommand("ерунда какая-то").execute(notLeader, service);
        assertEquals(1, Room.rooms.get(key).getAssociations().size());
    }

    @Test
    @Order(8)
    void guessCommandTest() throws InvalidCommandFormatException {
        String key = Room.findUser(user1).get().getKey();
        User leader = Room.rooms.get(key).getLeader();

        User notLeader, anotherPlayer;
        if (user1 != leader)
            notLeader = user1;
        else
            notLeader = user2;

        if(user3 != leader)
            anotherPlayer = user3;
        else
            anotherPlayer = user2;

        new GuessCommand(Room.rooms.get(key).getUsers().indexOf(notLeader) + " абц").execute(leader, service);

        assertEquals("а", Room.rooms.get(key).getCurrentPrefix());
        assertEquals(0, Room.rooms.get(key).getAssociations().size());

        new MakeAssociationCommand("аррррргх звуки злости").execute(notLeader, service);
        assertEquals(1, Room.rooms.get(key).getAssociations().size());
        new GuessCommand(Room.rooms.get(key).getUsers().indexOf(notLeader) + " аррррргх")
                .execute(anotherPlayer, service);
        assertEquals("аб", Room.rooms.get(key).getCurrentPrefix());
        assertEquals(0, Room.rooms.get(key).getAssociations().size());
    }
}