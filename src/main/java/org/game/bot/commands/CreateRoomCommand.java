package org.game.bot.commands;

import org.game.bot.exceptions.InvalidCommandFormatException;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;


public class CreateRoomCommand extends Command {

    public CreateRoomCommand(String args) throws InvalidCommandFormatException {
        noArgsRequired(args);
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        if (Room.findUser(user).isPresent()) {
            return List.of(service.getMessage(user.getId(), "inRoomException"));
        }
        String roomID = Room.createRoom();
        Room.rooms.get(roomID).addUser(user);
        return List.of(service.getMessage(user.getId(), "joinPerson", roomID));
    }
}