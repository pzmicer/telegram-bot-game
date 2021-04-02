package org.game.bot;

import org.game.bot.commands.CreateRoomCommand;
import org.game.bot.commands.ExitCommand;
import org.game.bot.commands.JoinCommand;
import org.game.bot.commands.StartCommand;
import org.game.bot.service.ReplyMessageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.User;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

@SpringBootTest
public class CommandsTest {

    @Autowired
    ReplyMessageService service;

    private static User user;

    @BeforeAll
    static void setUser(){
        user = new User();
        user.setId(1L);
    }

    @Test
    void startCommandTest(){

        StartCommand command = new StartCommand("");
        var result = command.execute(user, service);

        String resultString = result.get(0).getText();
        assertEquals("Добро пожаловать. Для просмотра списка команд введите /help", resultString);
    }

    @Test
    void createRoomCommand(){
        CreateRoomCommand command = new CreateRoomCommand("");
        command.execute(user, service);

        assertEquals(1, Room.rooms.size());
        assertEquals(user, Room.checkUser(user).get().getValue().getUsers().get(0));
        assertFalse(Room.checkUser(user).get().getValue().isInGame());

    }

    @Test
    void exitCommand(){
        CreateRoomCommand createCommand = new CreateRoomCommand("");
        createCommand.execute(user, service);
        ExitCommand command = new ExitCommand("");
        command.execute(user, service);

        assertFalse(Room.checkUser(user).isPresent());

    }

    @Test
    void joinCommand(){
        CreateRoomCommand createCommand = new CreateRoomCommand("");
        createCommand.execute(user, service);
        User anotherUser = new User();
        anotherUser.setId(2L);
        String key = Room.checkUser(user).get().getKey();

        JoinCommand command = new JoinCommand(key);
        command.execute(anotherUser, service);

        assertEquals(2, Room.rooms.get(key).getUsers().size());
    }
}
