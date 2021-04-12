package org.game.bot;

import org.game.bot.commands.CreateRoomCommand;
import org.game.bot.commands.ExitCommand;
import org.game.bot.commands.JoinCommand;
import org.game.bot.commands.StartCommand;
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
    void startCommandTest() {
        StartCommand command = new StartCommand(null);
        var result = command.execute(user, service);
        String resultString = result.get(0).getText();
        assertEquals("Добро пожаловать. Для просмотра списка команд введите /help", resultString);
    }

    @Test
    void createRoomCommandTest() {
        CreateRoomCommand command = new CreateRoomCommand(null);
        command.execute(user, service);
        assertEquals(1, Room.rooms.size());
        assertEquals(user, Room.findUser(user).get().getValue().getUsers().get(0));
        assertFalse(Room.findUser(user).get().getValue().isInGame());
    }

    @Test
    void exitCommandTest() {
        CreateRoomCommand createCommand = new CreateRoomCommand(null);
        createCommand.execute(user, service);
        ExitCommand command = new ExitCommand(null);
        command.execute(user, service);
        assertFalse(Room.findUser(user).isPresent());
    }

    @Test
    void joinCommandTest() {
        CreateRoomCommand createCommand = new CreateRoomCommand(null);
        createCommand.execute(user, service);
        User anotherUser = new User();
        anotherUser.setId(2L);
        String key = Room.findUser(user).get().getKey();
        JoinCommand command = new JoinCommand(key);
        command.execute(anotherUser, service);
        assertEquals(2, Room.rooms.get(key).getUsers().size());
    }
}