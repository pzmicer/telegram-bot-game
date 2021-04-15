package org.game.bot;

import org.game.bot.commands.*;
import org.game.bot.exceptions.InvalidCommandFormatException;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.User;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommandsTest {

    @Autowired
    ReplyMessageService service;

    private static User user;

    @BeforeAll
    static void setUser() {
        user = new User();
        user.setId(1L);
    }

    @Test
    void startCommandTest() throws InvalidCommandFormatException {
        StartCommand command = new StartCommand(null);
        var result = command.execute(user, service);
        String resultString = result.get(0).getText();
        assertEquals("Добро пожаловать. Для просмотра списка команд введите /help", resultString);
    }

    @Test
    void createRoomCommandTest() throws InvalidCommandFormatException {
        CreateRoomCommand command = new CreateRoomCommand(null);
        command.execute(user, service);
        assertEquals(1, Room.rooms.size());
        assertEquals(user, Room.findUser(user).get().getValue().getUsers().get(0));
        assertFalse(Room.findUser(user).get().getValue().isInGame());
    }

    @Test
    void exitCommandTest() throws InvalidCommandFormatException {
        CreateRoomCommand createCommand = new CreateRoomCommand(null);
        createCommand.execute(user, service);
        ExitCommand command = new ExitCommand(null);
        command.execute(user, service);
        assertFalse(Room.findUser(user).isPresent());
    }

    @Test
    void joinCommandTest() throws InvalidCommandFormatException {
        ExitCommand exitCommand = new ExitCommand(null);
        exitCommand.execute(user, service);
        CreateRoomCommand createCommand = new CreateRoomCommand(null);
        createCommand.execute(user, service);
        User anotherUser = new User();
        anotherUser.setId(2L);
        String key = Room.findUser(user).get().getKey();
        JoinCommand command = new JoinCommand(key);
        command.execute(anotherUser, service);
        assertEquals(2, Room.rooms.get(key).getUsers().size());
    }

    @Test
    void startGameCommandTest() throws InvalidCommandFormatException {
        ExitCommand exitCommand = new ExitCommand(null);
        exitCommand.execute(user, service);
        CreateRoomCommand createCommand = new CreateRoomCommand(null);
        createCommand.execute(user, service);
        User anotherUser = new User();
        User anotherSecondUser = new User();
        anotherUser.setId(3L);
        anotherSecondUser.setId(4L);
        String key = Room.findUser(user).get().getKey();
        JoinCommand command = new JoinCommand(key);
        command.execute(anotherUser, service);
        command.execute(anotherSecondUser, service);

        StartGameCommand startGameCommand = new StartGameCommand(null);
        startGameCommand.execute(anotherUser, service);
        assertTrue(Room.rooms.get(key).getLeader() != null);
        assertTrue(Room.rooms.get(key).isInGame());
        assertEquals(0, Room.rooms.get(key).getCurrentLetterIndex());
    }

    @Test
    void setKeywordCommandTest() throws InvalidCommandFormatException {
        ExitCommand exitCommand = new ExitCommand(null);
        exitCommand.execute(user, service);
        CreateRoomCommand createCommand = new CreateRoomCommand(null);
        createCommand.execute(user, service);
        User anotherUser = new User();
        User anotherSecondUser = new User();
        anotherUser.setId(7L);
        anotherSecondUser.setId(8L);
        String key = Room.findUser(user).get().getKey();
        JoinCommand command = new JoinCommand(key);
        command.execute(anotherUser, service);
        command.execute(anotherSecondUser, service);
        StartGameCommand startGameCommand = new StartGameCommand(null);
        startGameCommand.execute(anotherUser, service);
        User leader = Room.rooms.get(key).getLeader();

        String word = "абвгдэйка";
        SetKeywordCommand setKeywordCommand = new SetKeywordCommand(word);
        setKeywordCommand.execute(leader, service);
        assertEquals(word, Room.rooms.get(key).getKeyword());
    }

    @Test
    void makeAssociationCommandTest() throws InvalidCommandFormatException {
        ExitCommand exitCommand = new ExitCommand(null);
        exitCommand.execute(user, service);
        CreateRoomCommand createCommand = new CreateRoomCommand(null);
        createCommand.execute(user, service);
        User anotherUser = new User();
        User anotherSecondUser = new User();
        anotherUser.setId(5L);
        anotherSecondUser.setId(6L);
        String key = Room.findUser(user).get().getKey();
        JoinCommand command = new JoinCommand(key);
        command.execute(anotherUser, service);
        command.execute(anotherSecondUser, service);
        StartGameCommand startGameCommand = new StartGameCommand(null);
        startGameCommand.execute(anotherUser, service);
        String word = "абвгдэйка";
        User leader = Room.rooms.get(key).getLeader();
        SetKeywordCommand setKeywordCommand = new SetKeywordCommand(word);
        setKeywordCommand.execute(leader, service);

        MakeAssociationCommand makeAssociationCommand = new MakeAssociationCommand("абц майонез такой");
        User notLeader;
        if (user != leader)
            notLeader = user;
        else
            notLeader = anotherUser;
        makeAssociationCommand.execute(notLeader, service);
        assertEquals(1, Room.rooms.get(key).getAssociations().size());
        makeAssociationCommand = new MakeAssociationCommand("ерунда");
        makeAssociationCommand.execute(notLeader, service);
        makeAssociationCommand = new MakeAssociationCommand("ерунда какая-то");
        makeAssociationCommand.execute(notLeader, service);
        assertEquals(1, Room.rooms.get(key).getAssociations().size());
        makeAssociationCommand = new MakeAssociationCommand("абвгдэйка передача такая");
        makeAssociationCommand.execute(notLeader, service);
        assertFalse(Room.rooms.get(key).isInGame());
    }

    @Test
    void guessCommandTest() throws InvalidCommandFormatException {
        ExitCommand exitCommand = new ExitCommand(null);
        exitCommand.execute(user, service);
        CreateRoomCommand createCommand = new CreateRoomCommand(null);
        createCommand.execute(user, service);
        User anotherUser = new User();
        User anotherSecondUser = new User();
        anotherUser.setId(9L);
        anotherSecondUser.setId(10L);
        String key = Room.findUser(user).get().getKey();
        JoinCommand command = new JoinCommand(key);
        command.execute(anotherUser, service);
        command.execute(anotherSecondUser, service);
        StartGameCommand startGameCommand = new StartGameCommand(null);
        startGameCommand.execute(anotherUser, service);
        String word = "абвгдэйка";
        User leader = Room.rooms.get(key).getLeader();
        SetKeywordCommand setKeywordCommand = new SetKeywordCommand(word);
        setKeywordCommand.execute(leader, service);
        User notLeader;
        if (user != leader)
            notLeader = user;
        else
            notLeader = anotherUser;
        MakeAssociationCommand makeAssociationCommand = new MakeAssociationCommand("абц майонез такой");
        makeAssociationCommand.execute(notLeader, service);

        makeAssociationCommand = new MakeAssociationCommand("абрикос фрукт");
        makeAssociationCommand.execute(notLeader, service);

        GuessCommand guessCommand = new GuessCommand(notLeader.getId().toString() + " абрикос");
        guessCommand.execute(leader, service);

    }
}