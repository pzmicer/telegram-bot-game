/*
package org.game.bot;

import org.game.bot.commands.*;
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
    void startCommandTest() {
        StartCommand command = new StartCommand(service);
        var result = command.execute(user1, null);
        String resultString = result.get(0).getText();
        assertEquals("Добро пожаловать. Для просмотра списка команд введите /help", resultString);
    }

    @Test
    @Order(2)
    void createRoomCommandTest() {
        CreateRoomCommand command = new CreateRoomCommand(service);
        command.execute(user1, null);
        assertEquals(1, Room.rooms.size());
        assertEquals(user1, Room.findUser(user1).get().getValue().getUsers().get(0));
        assertFalse(Room.findUser(user1).get().getValue().isInGame());
    }

    @Test
    @Order(3)
    void exitCommandTest() {
        ExitCommand command = new ExitCommand(service);
        command.execute(user1, null);
        assertFalse(Room.findUser(user1).isPresent());
    }

    @Test
    @Order(4)
    void joinCommandTest() {
        CreateRoomCommand createCommand = new CreateRoomCommand(service);
        createCommand.execute(user1, null);
        String key = Room.findUser(user1).get().getKey();
        JoinCommand command = new JoinCommand(service);
        command.execute(user2, key);
        command.execute(user3, key);
        assertEquals(3, Room.rooms.get(key).getUsers().size());
    }

    @Test
    @Order(5)
    void startGameCommandTest() {
        String key = Room.findUser(user1).get().getKey();

        StartGameCommand startGameCommand = new StartGameCommand(service);
        startGameCommand.execute(user2, null);
        assertNotNull(Room.rooms.get(key).getLeader());
        assertTrue(Room.rooms.get(key).isInGame());
        assertEquals(0, Room.rooms.get(key).getCurrentLetterIndex());
    }

    @Test
    @Order(6)
    void setKeywordCommandTest() {
        String key = Room.findUser(user1).get().getKey();
        User leader = Room.rooms.get(key).getLeader();

        String word = "абвгдэйка";
        SetKeywordCommand setKeywordCommand = new SetKeywordCommand(service);
        setKeywordCommand.execute(leader, word);
        assertEquals(word, Room.rooms.get(key).getKeyword());
    }

    @Test
    @Order(7)
    void makeAssociationCommandTest() {
        String key = Room.findUser(user1).get().getKey();
        User leader = Room.rooms.get(key).getLeader();

        User notLeader;
        if (user1 != leader)
            notLeader = user1;
        else
            notLeader = user2;

        assertEquals(0, Room.rooms.get(key).getAssociations().size());
        new MakeAssociationCommand(service).execute(notLeader, "абц майонез такой");
        assertEquals(1, Room.rooms.get(key).getAssociations().size());
        new MakeAssociationCommand(service).execute(notLeader, "ерунда какая-то");
        assertEquals(1, Room.rooms.get(key).getAssociations().size());
    }

    @Test
    @Order(8)
    void guessCommandTest() {
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

        new GuessCommand(service, null).execute(leader, Room.rooms.get(key).getUsers().indexOf(notLeader) + " абц");

        assertEquals("а", Room.rooms.get(key).getCurrentPrefix());
        assertEquals(0, Room.rooms.get(key).getAssociations().size());

        new MakeAssociationCommand(service).execute(notLeader, "аррррргх звуки злости");
        assertEquals(1, Room.rooms.get(key).getAssociations().size());
        new GuessCommand(service, null)
                .execute(anotherPlayer, Room.rooms.get(key).getUsers().indexOf(notLeader) + " аррррргх");
        assertEquals("аб", Room.rooms.get(key).getCurrentPrefix());
        assertEquals(0, Room.rooms.get(key).getAssociations().size());
    }
}*/
