package org.game.bot.commands;

import org.game.bot.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@BotCommand(name="createroom")
public class CreateRoomCommand extends Command {

    public CreateRoomCommand(String args) {
        super(args);
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        if (Room.checkUser(user).isPresent()) {
            return List.of(service.getReplyMessage(user.getId(), "createRoomException"));
        }
        String roomID = Room.createRoom();
        Room.rooms.get(roomID).addUser(user);
        return List.of(service.getReplyMessage(user.getId(), "joinPerson", roomID));
    }
}