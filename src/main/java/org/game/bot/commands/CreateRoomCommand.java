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
        Long chatID = user.getId();
        if (Room.checkUser(chatID).isPresent()) {
            return List.of(service.getReplyMessage(chatID, "createRoomException"));
        }
        String roomID = Room.createRoom();
        Room.rooms.get(roomID).addUser(chatID);
        return List.of(service.getReplyMessage(chatID, "joinPerson", roomID));
    }
}